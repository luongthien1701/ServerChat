
package com.example.demo.websocket;

import com.example.demo.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        String authorization =
                request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return false;
        }

        String token = authorization.substring(7).trim();

        if (token.isEmpty()) {
            return false;
        }

        try {
            Long userId = jwtService.extractUserId(token);

            if (!jwtService.validateToken(token, userId)) {
                return false;
            }

            // Lưu ID đã xác thực vào WebSocket session
            attributes.put("userId", userId);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        } catch (RuntimeException e) {
            // Bao gồm trường hợp token có subject không phải userId hợp lệ
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        // Không cần xử lý thêm
    }
}
