package com.github.rahulstech.tts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ConvertTextToSpeechRequest(
        @NotEmpty(message = "text is required")
        @Size(max = 500, message = "text must be with in 500 characters")
        String text
) {}
