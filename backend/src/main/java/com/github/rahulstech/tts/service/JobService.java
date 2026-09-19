package com.github.rahulstech.tts.service;

import com.github.rahulstech.tts.repository.JobRepository;
import com.github.rahulstech.tts.dto.JobStatusResponse;
import com.github.rahulstech.tts.entity.JobEntity;
import com.github.rahulstech.tts.error.HttpException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepo;


    public JobEntity createJob(@Nullable Map<String,Object> params) {
        JobEntity job = JobEntity.builder()
                .status(JobEntity.Status.CREATED)
                .properties(params)
                .build();

        return jobRepo.save(job);
    }

    public JobEntity updateStatus(UUID jobId, JobEntity.Status status) {
        JobEntity job = getJobByIdOrThrowNotFound(jobId);
        job.setStatus(status);
        return jobRepo.saveAndFlush(job);
    }

    public JobEntity setResult(UUID jobId, Map<String,Object> result, JobEntity.Status status) {
        JobEntity job = getJobByIdOrThrowNotFound(jobId);
        job.setStatus(status);
        job.setResult(result);
        return jobRepo.saveAndFlush(job);
    }

    public JobStatusResponse getJobStatus(@NonNull UUID jobId) {
        JobEntity job = getJobByIdOrThrowNotFound(jobId);
        return new JobStatusResponse(job.getId(), job.getStatus().name(), job.getStatus().isFinished());
    }

    public JobEntity getJobByIdOrThrowNotFound(@NonNull UUID jobId) {
        return jobRepo.findById(jobId).orElseThrow(()-> HttpException.notFound("job not found"));
    }
}
