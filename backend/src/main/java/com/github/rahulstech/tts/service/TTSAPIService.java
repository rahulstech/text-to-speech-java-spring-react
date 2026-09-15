package com.github.rahulstech.tts.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TTSAPIService {

    CompletableFuture<String> requestTTS(String text);
}
