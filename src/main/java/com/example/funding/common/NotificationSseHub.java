package com.example.funding.common;

import com.example.funding.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * NotificationSseHub
 * <p>
 * Server-Sent Events (SSE)를 통해 실시간 알림을 관리하는 허브 클래스.
 * 사용자별로 SseEmitter를 등록하고, 알림을 전송하며, 연결 상태를 관리.
 * </p>
 *
 * @author by: 장민규
 * @since 2025-10-02
 */
@Component
@RequiredArgsConstructor
public class NotificationSseHub {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> heartbeats = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ObjectMapper objectMapper;

    /**
     * 사용자 ID로 SSE 연결 등록
     *
     * @param userId 사용자 ID
     * @param resp   HTTP 응답 객체
     * @return 등록된 SseEmitter
     * @author by: 장민규
     * @since 2025-10-02
     */
    public SseEmitter register(Long userId, HttpServletResponse resp) {
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");
        resp.setHeader("X-Accel-Buffering", "no");

        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(userId, emitter);

        ScheduledFuture<?> hb = scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(":\n\n");
            } catch (IOException e) {
                cleanup(userId, emitter);
            }
        }, 15, 15, TimeUnit.SECONDS);
        heartbeats.put(userId, hb);

        emitter.onTimeout(() -> cleanup(userId, emitter));
        emitter.onCompletion(() -> cleanup(userId, emitter));
        emitter.onError(ex -> cleanup(userId, emitter));

        try {
            emitter.send(SseEmitter.event().name("INIT").data("connected"));
        } catch (IOException e) {
            cleanup(userId, emitter);
        }

        return emitter;
    }

    /**
     * 특정 사용자에게 알림 전송
     *
     * @param noti 전송할 알림 객체
     * @author by: 장민규
     * @since 2025-10-02
     */
    public void sendToUser(Notification noti) {
        SseEmitter emitter = emitters.get(noti.getUserId());
        if (emitter == null) return;
        try {
            String json = objectMapper.writeValueAsString(noti);
            emitter.send(SseEmitter.event().name("NOTIFICATION").data(json));
        } catch (IOException e) {
            cleanup(noti.getUserId(), emitter);
        }
    }

    /**
     * 사용자 ID로 SSE 연결 정리
     *
     * @param userId  사용자 ID
     * @param emitter 정리할 SseEmitter
     * @author by: 장민규
     * @since 2025-10-02
     */
    public void cleanup(Long userId, SseEmitter emitter) {
        try {
            emitter.complete();
        } catch (Exception ignore) {
        }
        emitters.remove(userId, emitter);
        ScheduledFuture<?> hb = heartbeats.remove(userId);
        if (hb != null) hb.cancel(true);
    }
}

