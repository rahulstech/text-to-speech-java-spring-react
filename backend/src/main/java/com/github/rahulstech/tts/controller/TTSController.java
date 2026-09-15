package com.github.rahulstech.tts.controller;

import com.github.rahulstech.tts.dto.ConvertTextToSpeechRequest;
import com.github.rahulstech.tts.dto.TTSJobResponse;
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

    @GetMapping("/job/{jobId}")
    public TTSJobResponse getJob(@PathVariable UUID jobId) {
        return ttsSrvc.getJob(jobId);
    }

    @PostMapping
    public TTSJobResponse convertTextToSpeech(@Valid @RequestBody ConvertTextToSpeechRequest body) {
        return ttsSrvc.convertTextToSpeech(body);
    }
}
