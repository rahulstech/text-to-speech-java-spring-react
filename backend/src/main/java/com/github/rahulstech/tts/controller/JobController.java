package com.github.rahulstech.tts.controller;

import com.github.rahulstech.tts.dto.JobStatusResponse;
import com.github.rahulstech.tts.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobSrvc;

    @GetMapping("/{job_id}/status")
    public JobStatusResponse getJobStatus(@PathVariable("job_id")UUID jobId) {
        return jobSrvc.getJobStatus(jobId);
    }
}
