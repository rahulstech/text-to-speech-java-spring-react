package com.github.rahulstech.tts.dto;

import com.github.rahulstech.tts.entity.JobEntity;

import java.util.Map;
import java.util.UUID;

public sealed interface TTSJobResponse {

    UUID job_id();

    String status();

    static TTSJobResponse fromEntity(JobEntity entity) {
        Map<String,Object> result = entity.getResult();
        return switch (entity.getStatus()) {
            case SUCCESSFUL -> {
                String uri = null != result ? (String) result.get("uri") : null;
                yield new Success(entity.getId(), uri);
            }
            case FAIL -> {
                String error = null != result ? (String) result.get("error") : null;
                yield new Fail(entity.getId(), error);
            }
            default -> new Others(entity.getId(), entity.getStatus().name());
        };
    }

    record Success(
            UUID job_id,
            String uri
    ) implements TTSJobResponse {
        @Override
        public String status() {
            return JobEntity.Status.SUCCESSFUL.name();
        }
    }

    record Fail(
            UUID job_id,
            String error
    ) implements TTSJobResponse {

        @Override
        public String status() {
            return JobEntity.Status.FAIL.name();
        }
    }

    record Others(
            UUID job_id,
            String status
    ) implements TTSJobResponse {}
}