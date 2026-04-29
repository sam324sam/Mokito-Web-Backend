# Mokito Web Backend
![Java](https://img.shields.io/badge/Java-%23ED8B00?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F?style=flat-square&logo=springboot)
![WebSocket](https://img.shields.io/badge/WebSocket-%23000000?style=flat-square&logo=websocket)
![Maven](https://img.shields.io/badge/Maven-%23C71A36?style=flat-square&logo=apachemaven)
![Lombok](https://img.shields.io/badge/Lombok-%23BC4521?style=flat-square&logo=java)

(Prueba aun no esta terminado el readme)
Backend en Spring Boot para un entorno colaborativo en tiempo real donde múltiples usuarios comparten un canvas con sus cursores y mascotas virtuales sincronizadas vía WebSocket.

---

## Características

- Sincronización de cursores en tiempo real entre todos los usuarios conectados
- Mascotas virtuales por usuario con posición persistente en sesión
- Snapshot de estado completo al conectarse: nuevos usuarios reciben el estado de todos los que ya estaban
- Broadcast automático de entradas y salidas de usuarios
- Limpieza automática de sesiones al desconectarse
- Escalado de coordenadas adaptado a distintas resoluciones de canvas

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 4.0.5 |
| Comunicación | Spring WebSocket |
| Build | Maven 3.6+ |
| Utilidades | Lombok |

---

## Instalación

### Requisitos

- Java 17+
- Maven 3.6+

### Ejecutar en local

```bash
mvn clean compile
mvn spring-boot:run
```

El servidor WebSocket quedará disponible en `ws://localhost:8080/`.

### Build de producción

```bash
mvn clean package
java -jar target/mokito-web-backend-0.0.1-SNAPSHOT.jar
```

### Docker

```bash
mvn spring-boot:build-image
```

---

## 📁 Estructura del Proyecto (Mejorar y descripciones)

```
src/main/java/com/mokito/backend/
├── MokitoWebBackendApplication.java  # Punto de entrada Spring Boot

├── config/
│   ├── CorsConfig.java               # Configura CORS para permitir orígenes cruzados
│   └── WebSocketConfig.java          # Registra el endpoint WebSocket y configura STOMP

├── controller/
│   ├── LoginController.java          # POST /login — genera y devuelve userId único
│   └── UsersController.java          # GET /user/all — lista usuarios conectados; GET /user/ping — latencia

├── handler/
│   ├── UserDataHandler.java          # Maneja init, user_data: registra/actualiza usuarios y notifica broadcast
│   └── PetDataHandler.java           # Maneja init_pet, move_pet: registra/actualiza mascotas y broadcast

├── model/
│   ├── dto/
│   │   └── UserDto.java              # DTO para transferencia de datos de usuario
│   └── entity/
│       ├── UserClient.java           # Entidad usuario (id, nombre, cursor, canvas)
│       ├── PetClient.java            # Entidad mascota (x, y, userId)
│       ├── CursorClient.java         # Entidad cursor (x, y, src imagen)
│       ├── CanvasClient.java         # Entidad canvas (ancho, alto)
│       └── AnimationSpriteClient.java# Sprite animado del usuario

├── router/
│   └── EventRouter.java              # Enruta mensajes WebSocket (init_full, init, user_data, init_pet, move_pet) a los handlers

├── service/
│   ├── WebSocketSessionManager.java  # Gestiona sesiones y usuarios en maps thread-safe (ConcurrentHashMap)
│   └── PetServices.java              # Lógica de negocio para mascotas (crear, actualizar, eliminar)

└── websocket/
    ├── WebSocketHandler.java         # Handler principal: recibe mensajes, los enruta y limpieza al desconectar
    └── WebSocketDispatcher.java      # Envía mensajes a todos (broadcast) o a uno (unicast) con manejo de errores
```

---

### Flujo de conexión (Realizar en canvas y completar)

```
Cliente                          Servidor
  │                                 │
  ├──POST /login ──────────────────►│  Obtiene userId
  │◄── { userId } ──────────────────┤
  │                                 │
  ├──WS connect ───────────────────►│
  ├──{ type: "init", user } ───────►│  Registra usuario
  │◄── snapshot de usuarios/pets ───┤  Estado actual de la sala
  │                                 │
  ├──{ type: "init_pet", pet } ────►│  Registra mascota
  │◄── broadcast init_pet ──────────┤  Todos reciben la nueva mascota
```

---

### Mensajes que envía el cliente

**`init` — Registrar usuario al conectarse**

```json
{
  "type": "init",
  "user": {
    "userId": "user123",
    "name": "Mokito Friend",
    "cursor": { "x": 100, "y": 200, "src": "cursor.png" },
    "canvas": { "width": 1920, "height": 1080 }
  }
}
```

**`init_pet` — Registrar mascota**

```json
{
  "type": "init_pet",
  "pet": { "x": 150, "y": 250 }
}
```

**`user_data` — Actualizar posición del cursor** *(enviado cada network tick)*

```json
{
  "type": "user_data",
  "userId": "user123",
  "payload": {
    "user": {
      "cursor": { "x": 320, "y": 480, "src": "cursor.png" },
      "canvas": { "width": 1920, "height": 1080 }
    }
  }
}
```

**`move_pet` — Actualizar posición de la mascota** *(enviado cada network tick si se movió)*

```json
{
  "type": "move_pet",
  "payload": {
    "pet_move": { "x": 200, "y": 300, "userId": "user123" }
  }
}
```

---

### Mensajes que recibe el cliente (broadcast)

**`user_data` — Estado de un usuario**

```json
{
  "type": "user_data",
  "user": {
    "userId": "user123",
    "name": "Mokito Friend",
    "cursor": { "x": 320, "y": 480, "src": "cursor.png" },
    "canvas": { "width": 1920, "height": 1080 }
  }
}
```

**`init_pet` — Mascota registrada**

```json
{
  "type": "init_pet",
  "pet": { "x": 150, "y": 250, "userId": "user123" }
}
```

**`pet_move` — Mascota moviéndose**

```json
{
  "type": "pet_move",
  "pet_move": { "x": 200, "y": 300, "userId": "user123" }
}
```

**`user_remove` — Usuario desconectado**

```json
{
  "type": "user_remove",
  "userId": "user123"
}
```

---

## Endpoints REST

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/login` | Crea sesión y devuelve `userId` |
| `GET` | `/user/all` | Lista todos los usuarios conectados |
| `GET` | `/user/ping` | Medición de latencia |

---

## Tests

```bash
mvn test
```


Tutorial epico de conexion y uso del cloudfare tunel