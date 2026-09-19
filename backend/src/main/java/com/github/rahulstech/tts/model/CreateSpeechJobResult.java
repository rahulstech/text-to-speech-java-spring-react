package com.github.rahulstech.tts.model;

import com.github.rahulstech.tts.entity.JobEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public sealed abstract class CreateSpeechJobResult {

    @Nullable
    public static CreateSpeechJobResult fromEntity(JobEntity entity) {
        JobEntity.Status status = entity.getStatus();
        Map<String,Object> result = entity.getResult();
        if (status == JobEntity.Status.SUCCESSFUL) {
            return Successful.fromMap(result);
        }
        else if (status == JobEntity.Status.FAIL) {
            return Failed.fromMap(result);
        }
        else {
            return null;
        }
    }

    public abstract Map<String,Object> toMap();

    @Getter
    @AllArgsConstructor
    public static final class Successful extends CreateSpeechJobResult {

        private final String uri;

        public static Successful fromMap(Map<String,Object> map) {
            return new Successful((String) map.get("uri"));
        }

        @Override
        public Map<String, Object> toMap() {
            return Map.of(
                    "uri", uri
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static final class Failed extends CreateSpeechJobResult {

        private final Integer statusCode;
        private final String statusText;
        private final String error;


        public static  Failed fromMap(Map<String,Object> map) {
            return new Failed(
                    (Integer) map.get("status_code"),
                    (String) map.get("status_text"),
                    (String) map.get("error")
            );
        }

        @Override
        public Map<String, Object> toMap() {
            return Map.of(
                    "status_code", statusCode,
                    "status_text", statusText,
                    "error", "error"
            );
        }
    }
}
