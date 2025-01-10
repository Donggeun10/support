package com.example.support.controller;

import com.example.support.domain.EventPayload;
import com.example.support.service.SseEmitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/sse")
public class SseController {

    private final SseEmitterService sseEmitterService;

    //응답 mime type 은 반드시 text/event-stream 이여야 한다.
    //클라이언트로 부터 SSE subscription 을 수락한다.
    @Operation(summary = "Server-Sent-Events Subscription", security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping(path = "/subscribe/{user}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable UUID user) {

        String sseUserId = user.toString();
        log.debug("new sse id : {}", sseUserId);
        SseEmitter emitter = sseEmitterService.subscribe(sseUserId);
        return ResponseEntity.ok(emitter);
    }

    //eventPayload 를 SSE 로 연결된 모든 클라이언트에게 broadcasting 한다.
    @Operation(summary = "Server-Event Generation", security = @SecurityRequirement(name = "basicAuth"))
    @PostMapping(path = "/broadcast")
    public ResponseEntity<Void> broadcast(@RequestBody EventPayload eventPayload) {

        sseEmitterService.broadcast(eventPayload);
        return ResponseEntity.ok().build();
    }

}
