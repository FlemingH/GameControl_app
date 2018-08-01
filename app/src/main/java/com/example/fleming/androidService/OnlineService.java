package com.example.fleming.androidService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.fleming.request.LoginRequest;
import com.example.fleming.request.OnlineRequest;

/**
 * 处理服务器登录的服务
 * 服务启动，向服务器发送上线消息
 * 服务关闭，向服务器发送下线消息
 */
public class OnlineService extends Service{

    private String username;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        username = intent.getStringExtra("username");

        LoginRequest.IAmReady(username);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        OnlineRequest.IAmOut(username);

        super.onDestroy();
    }

}
