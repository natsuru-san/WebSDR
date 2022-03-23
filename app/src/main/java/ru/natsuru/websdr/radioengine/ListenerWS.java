//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;

import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;
import java.util.List;
import java.util.Map;

public class ListenerWS {
    private final MainInit ai;
    public ListenerWS(MainInit ai){
        this.ai = ai;
    }
    //Объект слушателя; надлежит доработать!!!
    private final WebSocketListener wsl = new WebSocketListener() {
        @Override
        public void onStateChanged(WebSocket websocket, WebSocketState newState) {
        }
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        }
        @Override
        public void onConnectError(WebSocket websocket, WebSocketException cause) {
        }
        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
        }
        @Override
        public void onFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onTextFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onPingFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onTextMessage(WebSocket websocket, String text) {
        }
        @Override
        public void onTextMessage(WebSocket websocket, byte[] data) {
            ai.prepareArr(data);
        }
        @Override
        public void onBinaryMessage(WebSocket websocket, byte[] binary) {
            ai.prepareArr(binary);
        }
        @Override
        public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onFrameSent(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) {
        }
        @Override
        public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) {
        }
        @Override
        public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) {
        }
        @Override
        public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) {
        }
        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
        }
        @Override
        public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
        }
        @Override
        public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) {
        }
        @Override
        public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) {
        }
        @Override
        public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) {
        }
        @Override
        public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
        }
        @Override
        public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
        }
        @Override
        public void handleCallbackError(WebSocket websocket, Throwable cause) {
        }
        @Override
        public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) {
        }
    };
    protected WebSocketListener getListener(){
        return wsl;
    }
}
