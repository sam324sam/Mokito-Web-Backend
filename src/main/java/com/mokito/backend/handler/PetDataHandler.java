package com.mokito.backend.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.mokito.backend.model.entity.PetClient;
import com.mokito.backend.model.entity.UserClient;
import com.mokito.backend.service.PetServices;
import com.mokito.backend.service.WebSocketSessionManager;
import com.mokito.backend.websocket.WebSocketDispatcher;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

@Component
public class PetDataHandler {
    private final WebSocketDispatcher dispatcher;
    private final WebSocketSessionManager sessionManager;
    private final PetServices petServices;

    private static final String PET_MOVE_KEY = "move_pet";
    private static final String USER_ID_KEY = "userId";

    public PetDataHandler(WebSocketDispatcher dispatcher, WebSocketSessionManager sessionManager,
            PetServices petServices) {
        this.dispatcher = dispatcher;
        this.sessionManager = sessionManager;
        this.petServices = petServices;
    }

    public void init(WebSocketSession session, JsonNode json) {
        String userId = (String) session.getAttributes().get(USER_ID_KEY);
        JsonNode userNode = json.path("pet");

        PetClient pet = new PetClient();
        pet.setX(userNode.path("x").asInt());
        pet.setY(userNode.path("y").asInt());

        petServices.addPet(userId, pet);
        // Creacion del json
        ObjectNode out = JsonNodeFactory.instance.objectNode();
        out.put("type", "init_pet");
        ObjectNode petJson = JsonNodeFactory.instance.objectNode();
        petJson.put("x", pet.getX());
        petJson.put("y", pet.getY());
        petJson.put(USER_ID_KEY, userId);

        out.set("pet", petJson);

        dispatcher.broadcast(out.toString());
    }

    public void movePet(WebSocketSession session, JsonNode json) {
        String userId = (String) session.getAttributes().get(USER_ID_KEY);

        JsonNode payload = json.get("payload");
        if (payload == null)
            return;

        JsonNode petMoveNode = payload.get(PET_MOVE_KEY);
        if (petMoveNode == null)
            return;

        UserClient user = sessionManager.getUser(userId);
        if (user == null)
            return;

        PetClient pet = petServices.getPet(userId);

        int x = petMoveNode.path("x").asInt(0);
        int y = petMoveNode.path("y").asInt(0);
        pet.setX(x);
        pet.setY(y);

        // Creacion del json
        ObjectNode out = JsonNodeFactory.instance.objectNode();
        out.put("type", PET_MOVE_KEY);
        ObjectNode petJson = JsonNodeFactory.instance.objectNode();
        petJson.put("x", pet.getX());
        petJson.put("y", pet.getY());
        petJson.put(USER_ID_KEY, userId);

        out.set(PET_MOVE_KEY, petJson);
        dispatcher.broadcast(out.toString());
    }
}
