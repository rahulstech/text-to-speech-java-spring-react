package com.github.rahulstech.tts.dto;

public record GeminiTTSAPIResponse(
        Step[] steps
) {
    public record Step(
            Content[] content
    ) {}

    public record Content(
            String data,
            int sample_rate,
            String mime_type
    ) {}
}
