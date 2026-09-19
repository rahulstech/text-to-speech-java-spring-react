package com.github.rahulstech.tts.service;

import com.github.rahulstech.tts.dto.TTSAPIRequest;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface TTSAPIService {

    CompletableFuture<String> requestTTS(TTSAPIRequest params, @Nullable Runnable onStart);
}
