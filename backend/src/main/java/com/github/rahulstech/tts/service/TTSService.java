package com.github.rahulstech.tts.service;

import com.github.rahulstech.tts.JobRepository;
import com.github.rahulstech.tts.dto.ConvertTextToSpeechRequest;
import com.github.rahulstech.tts.dto.TTSJobResponse;
import com.github.rahulstech.tts.entity.JobEntity;
import com.github.rahulstech.tts.error.HttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TTSService {

    public static final String JOB_PROPERTY_TEXT = "text";
    public static final String JOB_RESULT_ERROR = "error";
    public static final String JOB_RESULT_URI = "uri";

    private final JobRepository jobRepo;

    private final TTSAPIService ttsApiSrvc;

    public TTSJobResponse getJob(UUID id) {
        JobEntity job = jobRepo.findById(id).orElseThrow(()-> HttpException.notFound("job with id "+id+" not found"));
        return TTSJobResponse.fromEntity(job);
    }


    private JobEntity createTTSJob(ConvertTextToSpeechRequest request) {

        Map<String,Object> properties = createTTSJobProperties(request);

        JobEntity job = JobEntity.builder()
                .status(JobEntity.Status.RUNNING)
                .properties(properties)
                .build();

        return  jobRepo.saveAndFlush(job);
    }

    private Map<String, Object> createTTSJobProperties(ConvertTextToSpeechRequest request) {
        return Map.of(
                JOB_PROPERTY_TEXT, request.text()
        );
    }

    public TTSJobResponse convertTextToSpeech(ConvertTextToSpeechRequest request) {

        // create a job
        JobEntity job = createTTSJob(request);

        // request api
        ttsApiSrvc.requestTTS(request.text())
                .whenComplete((uri, error)-> onRequestTTSComplete(job, uri, error));

        // return job
        return TTSJobResponse.fromEntity(job);
    }

    private void onRequestTTSComplete(JobEntity job, String uri, Throwable error) {
        if  (error != null) {
            job.setStatus(JobEntity.Status.FAIL);
            job.setResult(Map.of(
                    JOB_RESULT_ERROR, error.getMessage()
            ));
        }
        else {
            job.setStatus(JobEntity.Status.SUCCESSFUL);
            job.setResult(Map.of(
                    JOB_RESULT_URI, uri
            ));
        }
        jobRepo.saveAndFlush(job);
    }
}
