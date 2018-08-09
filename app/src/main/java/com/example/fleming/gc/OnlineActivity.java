package com.example.fleming.gc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fleming.androidService.OnlineService;
import com.example.fleming.androidService.OnlineSocketService;

public class OnlineActivity extends AppCompatActivity{

    private TextView onlineState;
    private Button goControlB;
    private String username;

    BroadcastMain receiver;

    public class BroadcastMain extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("webIsOnline".equals(intent.getStringExtra("msg"))){
                onWebOnLineMessage();
            } else if ("webIsOffline".equals(intent.getStringExtra("msg"))){
                onWebOfflineMessage();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        username = getIntent().getExtras().getString("username");

        //从Control返回不会调用
        //启动服务：向服务器发送在线信息
        Intent intentOnlineService = new Intent(OnlineActivity.this, OnlineService.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intentOnlineService.putExtras(bundle);
        startService(intentOnlineService);

        //启动服务：向web发送socket
        Intent intentOnlineSocketService = new Intent(OnlineActivity.this, OnlineSocketService.class);
        intentOnlineSocketService.putExtras(bundle);
        startService(intentOnlineSocketService);

        //注册广播
        receiver = new BroadcastMain();
        IntentFilter filter = new IntentFilter();
        filter.addAction("OnlineSocket");
        registerReceiver(receiver, filter);

    }

    //结束这个页面的同时向服务器发送下线请求，也向对面发送下线信息，服务器经过处理，若对面不在线，则不转发
    //交给服务处理
    @Override
    protected void onDestroy() {

        //结束服务器登录服务
        Intent intentOnlineService = new Intent(OnlineActivity.this, OnlineService.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intentOnlineService.putExtras(bundle);
        stopService(intentOnlineService);

        //结束OnlineSocket服务
        Intent intentOnlineSocketService = new Intent(OnlineActivity.this, OnlineSocketService.class);
        intentOnlineSocketService.putExtras(bundle);
        stopService(intentOnlineSocketService);

        super.onDestroy();
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

    //用于处理Web下线时发来的信息：改页面下线标志，改按钮不可点击
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
            Intent intent = new Intent(OnlineActivity.this, ControlActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

}
