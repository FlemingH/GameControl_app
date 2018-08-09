package com.example.fleming.androidService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.fleming.gc.OnlineActivity;
import com.example.fleming.request.form.SocketMessage;
import com.google.gson.Gson;

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
    private OnlineActivity onlineActivity;
    private Gson gson;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        username = intent.getStringExtra("username");
        handler = new Handler();
        onlineActivity = new OnlineActivity();
        gson = new Gson();
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

            SocketMessage socketMessage = gson.fromJson(text, SocketMessage.class);

            //能收到两种消息：对面上线，对面下线
            if ("webIsOnline".equals(socketMessage.getMessageType())){
                onlineActivity.onWebOnLineMessage();
            } else if ("webIsOffline".equals(socketMessage.getMessageType())) {
                onlineActivity.onWebOfflineMessage();
            }

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);

            new Thread(){
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"连接断开",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }.start();

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
