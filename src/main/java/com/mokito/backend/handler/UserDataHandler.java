package com.mokito.backend.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.mokito.backend.model.entity.CanvasClient;
import com.mokito.backend.model.entity.CursorClient;
import com.mokito.backend.model.entity.PetClient;
import com.mokito.backend.model.entity.UserClient;
import com.mokito.backend.service.PetServices;
import com.mokito.backend.service.WebSocketSessionManager;
import com.mokito.backend.websocket.WebSocketDispatcher;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

@Component
public class UserDataHandler {

    private static final String CURSOR_KEY = "cursor";
    private static final String CANVAS_KEY = "canvas";
    private static final String USER_ID_KEY = "userId";
    private static final String HEIGHT_KEY = "height";
    private static final String WIDTH_KEY = "width";

    private final WebSocketDispatcher dispatcher;
    private final WebSocketSessionManager sessionManager;
    private final PetServices petServices;

    public UserDataHandler(WebSocketDispatcher dispatcher, WebSocketSessionManager sessionManager,
            PetServices petServices) {
        this.dispatcher = dispatcher;
        this.sessionManager = sessionManager;
        this.petServices = petServices;
    }

    public void init(WebSocketSession session, JsonNode json) {
        JsonNode userNode = json.path("user");

        CursorClient cursor = new CursorClient();
        cursor.setX(userNode.path(CURSOR_KEY).path("x").asInt());
        cursor.setY(userNode.path(CURSOR_KEY).path("y").asInt());
        cursor.setSrc(userNode.path(CURSOR_KEY).path("src").asString());

        CanvasClient canvas = new CanvasClient();
        canvas.setHeight(userNode.path(CANVAS_KEY).path(HEIGHT_KEY).asInt());
        canvas.setWidth(userNode.path(CANVAS_KEY).path(WIDTH_KEY).asInt());

        UserClient user = new UserClient();
        user.setUserId(userNode.path(USER_ID_KEY).asString());
        user.setName(userNode.path("name").asString());
        user.setCursor(cursor);
        user.setCanvas(canvas);

        session.getAttributes().put("user", user);
        session.getAttributes().put(USER_ID_KEY, user.getUserId());

        sessionManager.addSession(user.getUserId(), user, session);

        // 1. Notificar a todos que llegó un usuario nuevo
        dispatcher.broadcast(buildUserDataMessage(user).toString());

        // 2. Enviar al recién conectado el estado de todos los que ya estaban
        sessionManager.getAllUsers().forEach(existingUser -> {
            if (existingUser.getUserId().equals(user.getUserId()))
                return;

            try {
                // Cursor del usuario existente
                session.sendMessage(new TextMessage(buildUserDataMessage(existingUser).toString()));

                // Mascota del usuario existente (si tiene)
                PetClient pet = petServices.getPet(existingUser.getUserId());
                if (pet != null) {
                    session.sendMessage(new TextMessage(buildInitPetMessage(pet, existingUser.getUserId()).toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private ObjectNode buildUserDataMessage(UserClient user) {
        ObjectNode out = JsonNodeFactory.instance.objectNode();
        out.put("type", "user_data");

        ObjectNode userJson = JsonNodeFactory.instance.objectNode();
        userJson.put(USER_ID_KEY, user.getUserId());
        userJson.put("name", user.getName());

        ObjectNode cursorJson = JsonNodeFactory.instance.objectNode();
        cursorJson.put("x", user.getCursor().getX());
        cursorJson.put("y", user.getCursor().getY());
        cursorJson.put("src", user.getCursor().getSrc());
        userJson.set(CURSOR_KEY, cursorJson);

        ObjectNode canvasJson = JsonNodeFactory.instance.objectNode();
        canvasJson.put(WIDTH_KEY, user.getCanvas().getWidth());
        canvasJson.put(HEIGHT_KEY, user.getCanvas().getHeight());
        userJson.set(CANVAS_KEY, canvasJson);

        out.set("user", userJson);
        return out;
    }

    private ObjectNode buildInitPetMessage(PetClient pet, String userId) {
        ObjectNode out = JsonNodeFactory.instance.objectNode();
        out.put("type", "init_pet");

        ObjectNode petJson = JsonNodeFactory.instance.objectNode();
        petJson.put("x", pet.getX());
        petJson.put("y", pet.getY());
        petJson.put(USER_ID_KEY, userId);
        out.set("pet", petJson);

        return out;
    }

    public void handle(WebSocketSession session, JsonNode json) {

        String userId = (String) session.getAttributes().get(USER_ID_KEY);

        JsonNode payload = json.get("payload");
        if (payload == null)
            return;

        JsonNode userNode = payload.get("user");
        if (userNode == null)
            return;

        JsonNode cursorNode = userNode.get(CURSOR_KEY);
        if (cursorNode == null)
            return;

        int x = cursorNode.path("x").asInt(0);
        int y = cursorNode.path("y").asInt(0);
        String src = cursorNode.path("src").asString("");

        UserClient user = sessionManager.getUser(userId);
        if (user == null)
            return;

        if (user.getCursor() == null) {
            user.setCursor(new CursorClient());
        }

        user.getCursor().setX(x);
        user.getCursor().setY(y);
        user.getCursor().setSrc(src);

        // Creacion del json
        ObjectNode out = JsonNodeFactory.instance.objectNode();

        out.put("type", "user_data");

        ObjectNode userJson = JsonNodeFactory.instance.objectNode();
        userJson.put(USER_ID_KEY, user.getUserId());
        userJson.put("name", user.getName());

        ObjectNode cursorJson = JsonNodeFactory.instance.objectNode();
        cursorJson.put("x", user.getCursor().getX());
        cursorJson.put("y", user.getCursor().getY());
        cursorJson.put("src", user.getCursor().getSrc());

        userJson.set(CURSOR_KEY, cursorJson);

        ObjectNode canvas = JsonNodeFactory.instance.objectNode();
        canvas.put(WIDTH_KEY, user.getCanvas().getWidth());
        canvas.put(HEIGHT_KEY, user.getCanvas().getHeight());

        userJson.set(CANVAS_KEY, canvas);

        out.set("user", userJson);

        dispatcher.broadcast(out.toString());
    }
}
