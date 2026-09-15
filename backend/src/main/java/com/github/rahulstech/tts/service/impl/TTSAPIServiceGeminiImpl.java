package com.github.rahulstech.tts.service.impl;

import com.github.rahulstech.tts.dto.GeminiTTSAPIResponse;
import com.github.rahulstech.tts.service.S3StorageService;
import com.github.rahulstech.tts.service.TTSAPIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.client.RestClient;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Profile("prod")
public class TTSAPIServiceGeminiImpl implements TTSAPIService {

    private final RestClient apiClient;

    private final S3StorageService storageSrvc;

    public TTSAPIServiceGeminiImpl(
            @Value("${tts.api-key}") String apiKey,
            S3StorageService storageSrvc
    ) {
        this.apiClient = createApiClient(apiKey);
        this.storageSrvc = storageSrvc;
    }

    private RestClient createApiClient(String apiKey) {
        return RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/interactions")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    @Async
    @Override
    public CompletableFuture<String> requestTTS(String text) {

        // make tts api request

        Map<String,Object> req =  Map.of(
                "model", "gemini-3.1-flash-tts-preview",
                "input", text,
                "response_format", Map.of("type","audio"),
                "generation_config", Map.of(
                        "speech_config", List.of(Map.of("voice", "Kore"))
                )
        );

        GeminiTTSAPIResponse res = this.apiClient.post()
                .header("Content-Type", "application/json")
                .body(req)
                .retrieve()
                .body(GeminiTTSAPIResponse.class);

        // extract the raw audio bytes
        String audioBytesBase64 = res.steps()[0].content()[0].data();
        byte[] audioBytes = Base64.getDecoder().decode(audioBytesBase64);

        // convert audio to wav
        try (InputStream source = convertL16ToWav(audioBytes)) {
            String filename = UUID.randomUUID()+".wav";
            String uri = storageSrvc.uploadFile(source, filename, MimeType.valueOf("audio/wav"), source.available());
            return CompletableFuture.completedFuture(uri);
        }
        catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private InputStream convertL16ToWav(byte[] rawBytes) throws IOException {
        AudioFormat format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                24000,
                16,
                1,
                2,
                24000,
                false // important
        );

        try (
                ByteArrayInputStream input = new ByteArrayInputStream(rawBytes);
                AudioInputStream audioInput =
                        new AudioInputStream(
                                input,
                                format,
                                rawBytes.length / format.getFrameSize()
                        );
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {
            AudioSystem.write(
                    audioInput,
                    AudioFileFormat.Type.WAVE,
                    output
            );

            return new ByteArrayInputStream(output.toByteArray());
        }
    }
}
