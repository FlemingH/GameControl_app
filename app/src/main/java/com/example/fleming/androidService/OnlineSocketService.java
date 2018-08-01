package com.example.fleming.androidService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.fleming.gc.OnlineActivity;
import com.example.fleming.request.OnlineRequest;
import com.example.fleming.request.form.SocketMessage;
import com.google.gson.Gson;
import com.pusher.java_websocket.client.WebSocketClient;
import com.pusher.java_websocket.drafts.Draft_10;
import com.pusher.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 只处理上下线socket信息，服务器信息交给另一个服务
 */
public class OnlineSocketService extends Service{

    private WebSocketClient mSocketClient;

    Handler handler = new Handler();

    OnlineActivity onlineActivity = new OnlineActivity();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String username = intent.getStringExtra("username");
        initWebSocket(username);

        //查对面是否在线
        boolean flag = OnlineRequest.isWebOnline(username);

        //对面在线的情况：改页面在线标志，改按钮可以点击，向对面发送socket表明我上线了
        if (flag) {
            onlineActivity.onWebOnLineMessage();

            Gson gson = new Gson();
            SocketMessage socketMessage = new SocketMessage();
            socketMessage.setData(username);
            socketMessage.setMessageType("AppIsOnline");
            String json = gson.toJson(socketMessage);

            if (mSocketClient != null) {
                mSocketClient.send(json);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mSocketClient.close();
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------

    private void initWebSocket(final String username) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                URI uri = null;

                try {
                    uri = new URI("ws://118.25.180.193:8090/SocketHandle/app/"+username);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                mSocketClient = new WebSocketClient(uri, new Draft_10()) {

                    Gson gson = new Gson();

                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
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

                    //在这种情况下，分别是对面不在线然后上线、对面在线然后下线
                    @Override
                    public void onMessage(String message) {

                        SocketMessage socketMessage = gson.fromJson(message, SocketMessage.class);

                        //能收到两种消息：对面上线，对面下线
                        if ("webIsOnline".equals(socketMessage.getMessageType())){
                            onlineActivity.onWebOnLineMessage();
                        } else if ("webIsOffline".equals(socketMessage.getMessageType())) {
                            onlineActivity.onWebOfflineMessage();
                        }

                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {

                        SocketMessage socketMessage = new SocketMessage();

                        //向对面发送的下线消息
                        socketMessage.setMessageType("AppIsOffline");
                        socketMessage.setData(username);
                        String json = gson.toJson(socketMessage);

                        if(mSocketClient != null){
                            mSocketClient.send(json);
                        }

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

                    @Override
                    public void onError(Exception ex) {
                        new Thread(){
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"连接错误",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }.start();

                    }
                };

            }
        });
    }

}
