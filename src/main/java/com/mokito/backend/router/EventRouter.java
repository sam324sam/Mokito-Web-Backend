package com.mokito.backend.router;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.mokito.backend.handler.PetDataHandler;
import com.mokito.backend.handler.UserDataHandler;

import tools.jackson.databind.JsonNode;

@Component
public class EventRouter {

    private final UserDataHandler userDataHandler;
    private final PetDataHandler petDataHandler;

    public EventRouter(UserDataHandler userDataHandler, PetDataHandler petDataHandler) {
        this.userDataHandler = userDataHandler;
        this.petDataHandler = petDataHandler;
    }

    public void route(WebSocketSession session, JsonNode json, String type) {

        if (type.isEmpty())
            return;

        switch (type) {

            case "init_full":
                userDataHandler.init(session, json);
                petDataHandler.init(session, json);
                break;

            case "init":
                userDataHandler.init(session, json);
                break;

            case "user_data":
                userDataHandler.handle(session, json);
                break;

            case "init_pet":
                petDataHandler.init(session, json);
                break;

            case "move_pet":
                petDataHandler.movePet(session, json);
                break;

            default:
                break;
        }
    }
}