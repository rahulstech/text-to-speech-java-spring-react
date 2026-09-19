package com.github.rahulstech.tts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rahulstech.tts.model.CreateSpeechJobParameters;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TTSRequest(

        @NotEmpty(message = "text is required")
        @Size(max = 500, message = "text must be within 500 characters")
        String text,

        CreateSpeechJobParameters.Language language,

        CreateSpeechJobParameters.Gender gender,

        @JsonProperty("voice_style")
        CreateSpeechJobParameters.VoiceStyle voiceStyle
) {

}