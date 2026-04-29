package com.mokito.backend.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.mokito.backend.router.EventRouter;
import com.mokito.backend.service.WebSocketSessionManager;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final EventRouter eventRouter;
    private final WebSocketSessionManager sessionManager;
    private final WebSocketDispatcher dispatcher;

    public WebSocketHandler(EventRouter eventRouter,
            WebSocketSessionManager sessionManager, WebSocketDispatcher dispatcher) {
        this.eventRouter = eventRouter;
        this.sessionManager = sessionManager;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode json = new ObjectMapper().readTree(message.getPayload());
        String type = json.path("type").asString();
        eventRouter.route(session, json, type);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        String userId = (String) session.getAttributes().get("userId");

        if (userId != null) {
            sessionManager.removeSession(userId);

            try {
                ObjectMapper mapper = new ObjectMapper();

                ObjectNode payload = mapper.createObjectNode();
                payload.put("type", "user_remove");
                payload.put("userId", userId);

                String message = mapper.writeValueAsString(payload);

                dispatcher.broadcast(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}