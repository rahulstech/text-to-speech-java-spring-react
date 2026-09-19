package com.github.rahulstech.tts.error;

import lombok.Getter;

@Getter
public class GeminiTTSServiceException extends RuntimeException {

    private final int statusCode;
    private final String statusText;

    public GeminiTTSServiceException(int statusCode, String statusText, String message) {
        super(message);
        this.statusCode = statusCode;
        this.statusText = statusText;
    }
}
