package de.rwth.idsg.steve;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.MimeType;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

@Slf4j
public class ExampleTransactionTests {

    @Test
    public void runTX() {
        WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());
        CustomSessionHandler handler = new CustomSessionHandler();
        client.connect("http://localhost:8080/steve/websocket/CentralSystemService/Charger_1", handler);
        StompSession session = handler.getSession();
        send(session, "[2,\"11706\",\"BootNotification\",{\"chargePointModel\":\"GPGV1.0\",\"chargePointSerialNumber\":\"030201000000000000000253\",\"chargePointVendor\":\"LHK\",\"firmwareVersion\":\"2.1.3-8-LAN\",\"meterSerialNumber\":\"010092040005\",\"meterType\":\"DLT_645_2007\"}]");
        send(session, "[2,\"8165\",\"Heartbeat\",{}]");
    }

    private void send(StompSession session, String message) {
        StompHeaders headers = new StompHeaders();
        headers.setContentType(MimeType.valueOf("application/json"));
        session.send(headers, message);
    }

    @Slf4j
    public static class CustomSessionHandler implements StompSessionHandler {

        @Getter
        private StompSession session;

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.debug("Connected!");
            this.session = session;
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            // stub
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            // stub
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return null;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            log.debug("Handle frame with headers: {}, payload: {}", headers, payload);
        }
    }
}
