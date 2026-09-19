package com.github.rahulstech.tts.service.impl;

import com.github.rahulstech.tts.dto.TTSAPIRequest;
import com.github.rahulstech.tts.service.TTSAPIService;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Profile("local")
public class TTSAPIServiceServeSampleImpl implements TTSAPIService {

    @Override
    public CompletableFuture<String> requestTTS(TTSAPIRequest params, @Nullable Runnable onStart) {
        return CompletableFuture.completedFuture("http://localhost:8080/generated-audio/sample.wav");
    }
}
