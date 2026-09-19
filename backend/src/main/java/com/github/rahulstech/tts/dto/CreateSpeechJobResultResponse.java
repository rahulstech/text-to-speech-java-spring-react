package com.github.rahulstech.tts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CreateSpeechJobResultResponse(
        @JsonProperty("json_id")
        UUID jsonId,
        String uri
) {}
