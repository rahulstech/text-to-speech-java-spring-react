package com.github.rahulstech.tts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record JobStatusResponse(
        @JsonProperty("job_id")
        UUID jobId,
        String status,
        @JsonProperty("is_finished")
        boolean isFinished
) {}
