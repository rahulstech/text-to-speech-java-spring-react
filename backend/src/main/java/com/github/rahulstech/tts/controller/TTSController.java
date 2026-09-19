package com.github.rahulstech.tts.controller;

import com.github.rahulstech.tts.dto.CreateSpeechJobResultResponse;
import com.github.rahulstech.tts.dto.JobStatusResponse;
import com.github.rahulstech.tts.dto.TTSRequest;
import com.github.rahulstech.tts.service.TTSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tts")
@RequiredArgsConstructor
public class TTSController {

    private final TTSService ttsSrvc;

    @PostMapping
    public JobStatusResponse createSpeech(@Valid @RequestBody TTSRequest body) {
        return ttsSrvc.createSpeech(body);
    }

    @GetMapping("/job/{job_id}/result")
    public CreateSpeechJobResultResponse getCreateSpeechJobResult(@PathVariable("job_id") UUID jobId) {
        return ttsSrvc.getCreateSpeechJobResult(jobId);
    }
}
