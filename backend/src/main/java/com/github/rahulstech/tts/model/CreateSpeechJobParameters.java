package com.github.rahulstech.tts.model;

import java.util.Map;

public record CreateSpeechJobParameters(
        String text,
        Language language,
        Gender gender,
        VoiceStyle style
) {

    private static final String KEY_TEXT = "text";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_VOICE_STYLE = "voice_style";

    public static CreateSpeechJobParameters fromMap(Map<String, Object> map) {
        return new CreateSpeechJobParameters(
                (String) map.get(KEY_TEXT),
                (Language) map.get(KEY_GENDER),
                (Gender) map.get(KEY_GENDER),
                (VoiceStyle) map.get(KEY_VOICE_STYLE)
        );
    }

    public Map<String,Object> toMap() {
        return Map.of(
                KEY_TEXT, text,
                KEY_LANGUAGE, language,
                KEY_GENDER, gender,
                KEY_VOICE_STYLE, style
        );
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public enum VoiceStyle {
        CASUAL,
        DEEP,
        JOLLY
    }

    public enum Language {
        BENGALI("bn"),
        ENGLISH("en"),
        GUJARATI("gu"),
        HINDI("hi"),
        KANNADA("kn"),
        KONKANI("ko"),
        MALAYALAM("ms"),
        MARATHI("ma"),
        MAITHILI("mi"),
        ODIA("od"),
        PUNJABI("pu"),
        TAMIL("ta"),
        TELUGU("te"),
        URDU("ur"),
        SINDHI("sd"),
        ;

        private final String code;

        Language(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }
}
