package com.github.rahulstech.tts.service;

import com.github.rahulstech.tts.dto.CreateSpeechJobResultResponse;
import com.github.rahulstech.tts.dto.JobStatusResponse;
import com.github.rahulstech.tts.dto.TTSAPIRequest;
import com.github.rahulstech.tts.dto.TTSRequest;
import com.github.rahulstech.tts.entity.JobEntity;
import com.github.rahulstech.tts.error.GeminiTTSServiceException;
import com.github.rahulstech.tts.error.HttpException;
import com.github.rahulstech.tts.model.CreateSpeechJobParameters;
import com.github.rahulstech.tts.model.CreateSpeechJobResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TTSService {

    private static final Logger log = LoggerFactory.getLogger(TTSService.class);

    private final TTSAPIService ttsApiSrvc;

    private final JobService jobSrvc;

    private JobEntity createTTSJob(TTSRequest request) {

        Map<String,Object> params = new CreateSpeechJobParameters(
                request.text(),
                request.language(),
                request.gender(),
                request.voiceStyle()
        ).toMap();

        return jobSrvc.createJob(params);
    }

    public JobStatusResponse createSpeech(TTSRequest request) {

        // create a job
        JobEntity job = createTTSJob(request);

        // request api
        log.debug(
                "Converting text to speech: text={}, gender={}, voice-style={}, language={}",
                request.text(), request.gender(), request.voiceStyle(), request.language()
        );

        TTSAPIRequest req = createTTSAPIRequest(request);
        ttsApiSrvc.requestTTS(
                req,
                ()-> jobSrvc.updateStatus(job.getId(), JobEntity.Status.RUNNING)
        ).whenComplete((uri, error)-> onRequestTTSComplete(job.getId(), uri, error));

        // return job status
        return new JobStatusResponse(job.getId(), job.getStatus().name(), job.getStatus().isFinished());
    }

    public CreateSpeechJobResultResponse getCreateSpeechJobResult(UUID jobId) {
        JobEntity job = jobSrvc.getJobByIdOrThrowNotFound(jobId);
        CreateSpeechJobResult result = CreateSpeechJobResult.fromEntity(job);
        if (result instanceof CreateSpeechJobResult.Successful) {
            return new CreateSpeechJobResultResponse(jobId, ((CreateSpeechJobResult.Successful) result).getUri());
        }
        else if (result instanceof CreateSpeechJobResult.Failed) {
            throw HttpException.internalServerError("create speech job failed");
        }
        return null;
    }

    private TTSAPIRequest createTTSAPIRequest(TTSRequest request) {
        CreateSpeechJobParameters.Language language = request.language() == null ? CreateSpeechJobParameters.Language.ENGLISH :  request.language();
        CreateSpeechJobParameters.Gender gender = request.gender() == null ? CreateSpeechJobParameters.Gender.FEMALE : request.gender();
        CreateSpeechJobParameters.VoiceStyle style = request.voiceStyle() == null ? CreateSpeechJobParameters.VoiceStyle.CASUAL : request.voiceStyle();

        String languageCode = language.code();
        String voice = getVoice(gender, style);
        return new TTSAPIRequest(request.text(), languageCode, voice);
    }

    private String getVoice(CreateSpeechJobParameters.Gender gender, CreateSpeechJobParameters.VoiceStyle style) {
        String voice;
        if (gender == CreateSpeechJobParameters.Gender.MALE) {
            voice = switch (style) {
                case DEEP -> "Algenib";
                case JOLLY -> "Puck";
                default -> "Achird";
            };
        }
        else {
            voice = switch (style) {
                case DEEP -> "Gacrux";
                case JOLLY -> "Laomedeia";
                default -> "Sulafat";
            };
        }
        return voice;
    }

    private void onRequestTTSComplete(UUID jobId, String uri, Throwable error) {
        CreateSpeechJobResult result;
        JobEntity.Status status;
        if  (error != null) {
            status = JobEntity.Status.FAIL;
            if (error instanceof GeminiTTSServiceException ex) {
                result = new CreateSpeechJobResult.Failed(
                        ex.getStatusCode(),
                        ex.getStatusText(),
                        ex.getMessage()
                );
            }
            else {
                result = new CreateSpeechJobResult.Failed(
                        500,
                        "internal server error",
                        error.getMessage()
                );
            }
        }
        else {
            status = JobEntity.Status.SUCCESSFUL;
            result = new CreateSpeechJobResult.Successful(uri);
        }
        jobSrvc.setResult(jobId, result.toMap(), status);
    }
}
