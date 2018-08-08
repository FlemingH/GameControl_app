package com.example.fleming.androidService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 只处理上下线socket信息，服务器信息交给另一个服务
 */
public class OnlineSocketService extends Service{

    private String username;
    private Handler handler;
    private WebSocket mWebSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        username = intent.getStringExtra("username");
        handler = new Handler();
        connect();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mWebSocket.cancel();
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------

    private final class OnlineWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            new Thread(){
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"连接成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }.start();

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
        }
    }

    private void connect(){
        OnlineWebSocketListener listener = new OnlineWebSocketListener();
        Request request = new Request.Builder()
                .url("ws://118.25.180.193:8090/SocketHandle/app/"+username)
                .build();
        OkHttpClient client = new OkHttpClient();

        mWebSocket = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

}
