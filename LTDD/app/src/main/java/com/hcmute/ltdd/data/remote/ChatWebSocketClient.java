package com.hcmute.ltdd.data.remote;

import android.util.Log;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatWebSocketClient {
    private WebSocket webSocket;
    //private final String baseUrl = "ws://192.168.182.130:9099/ws-chat/websocket";
    //private final String baseUrl = "ws://172.31.176.1:9099/ws-chat/websocket";
    private final String baseUrl = "ws://192.168.1.2:9099/ws-chat/websocket";
    private final long conversationId;
    private final ChatMessageListener listener;

    public interface ChatMessageListener {
        void onMessageReceived(JSONObject jsonMessage);
    }

    public ChatWebSocketClient(long conversationId, ChatMessageListener listener) {
        this.conversationId = conversationId;
        this.listener = listener;
        connect();
    }

    private void connect() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(baseUrl).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send("CONNECT\naccept-version:1.1,1.2\n\n\u0000");
                webSocket.send("SUBSCRIBE\nid:sub-0\ndestination:/topic/conversations/" + conversationId + "\n\n\u0000");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (text.contains("MESSAGE")) {
                    try {
                        String payload = text.substring(text.indexOf("\n\n") + 2).trim();
                        JSONObject json = new JSONObject(payload);
                        listener.onMessageReceived(json);
                    } catch (Exception e) {
                        Log.e("WebSocket", "Parse error", e);
                    }
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e("WebSocket", "Connection failed", t);
            }
        });
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Normal closure");
        }
    }
}
