package com.github.rahulstech.tts.dto;

public record TTSAPIRequest(
        String text,
        String language,
        String voice
) {}
