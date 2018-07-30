package com.example.fleming.gc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleming.request.OnlineRequest;
import com.example.fleming.request.form.SocketMessage;
import com.google.gson.Gson;
import com.pusher.java_websocket.client.WebSocketClient;
import com.pusher.java_websocket.drafts.Draft_10;
import com.pusher.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class OnlineActivity extends AppCompatActivity{

    private TextView onlineState;
    private Button goControlB;

    //此webSocket用来向web端传输在线信息、接收对面在线信息、发送下线信息、接受下线信息
    private WebSocketClient mSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        String username = this.getIntent().getExtras().getString("username");
        boolean flag = OnlineRequest.isWebOnline(username);

        //对面在线的情况：向对面发送我上线了，改页面在线标志，改按钮可以点击
        if (flag) {
            onlineState = findViewById(R.id.OnlineState);
            goControlB = findViewById(R.id.goControlB);

            onlineState.setText("在线");
            onlineState.setTextColor(Color.parseColor("#00a600"));

            goControlB.setEnabled(true);

            goControlB.setOnClickListener(bocl);

        }

    }

    //----------------------------------------------------------------------------------------------

    //用于处理Web上线时发来的信息：改页面在线标志，改按钮可以点击
    public void onWebOnLineMessage(){

        onlineState = findViewById(R.id.OnlineState);
        goControlB = findViewById(R.id.goControlB);

        onlineState.setText("在线");
        onlineState.setTextColor(Color.parseColor("#00a600"));

        goControlB.setEnabled(true);

        goControlB.setOnClickListener(bocl);
    }

    //用于处理Web下线时发来的信息：改页面标志，改按钮不可点击
    public void onWebOfflineMessage(){

        onlineState = findViewById(R.id.OnlineState);
        goControlB = findViewById(R.id.goControlB);

        onlineState.setText("未上线");
        onlineState.setTextColor(Color.parseColor("#666666"));

        goControlB.setEnabled(true);

    }

    //----------------------------------------------------------------------------------------------

    //按钮全局点击事件
    public final View.OnClickListener bocl = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };

    //----------------------------------------------------------------------------------------------

    private void initWebSocket(final String username) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                URI uri = null;

                try {
                    uri = new URI("ws://118.25.180.193/SocketHandle/app/"+username);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                mSocketClient = new WebSocketClient(uri,new Draft_10()) {

                    Gson gson = new Gson();

                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Toast.makeText(OnlineActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMessage(String message) {

                        SocketMessage socketMessage = gson.fromJson(message, SocketMessage.class);

                        //能收到两种消息：对面上线，对面下线
                        if ("webIsOnline".equals(socketMessage.getMessageType())){
                            onWebOnLineMessage();
                        } else if ("webIsOffline".equals(socketMessage.getMessageType())) {
                            onWebOfflineMessage();
                        }

                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {

                        SocketMessage socketMessage = new SocketMessage();

                        //连接关闭时发送：我下线了
                        socketMessage.setMessageType("AppIsOffline");
                        socketMessage.setData("");
                        String json = gson.toJson(socketMessage);

                        if(mSocketClient != null){
                            mSocketClient.send(json);
                        }

                    }

                    @Override
                    public void onError(Exception ex) {
                        Toast.makeText(OnlineActivity.this,"连接错误",Toast.LENGTH_SHORT).show();
                    }
                };

            }
        });
    }

}
