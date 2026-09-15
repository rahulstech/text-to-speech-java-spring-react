package com.github.rahulstech.tts.service.impl;

import com.github.rahulstech.tts.service.TTSAPIService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Profile("local")
public class TTSAPIServiceServeSampleImpl implements TTSAPIService {

    @Override
    public CompletableFuture<String> requestTTS(String text) {
        return CompletableFuture.completedFuture("/content/generated-audio/sample.wav");
    }
}
