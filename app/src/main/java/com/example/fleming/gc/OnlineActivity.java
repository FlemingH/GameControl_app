package com.example.fleming.gc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fleming.request.OnlineRequest;

public class OnlineActivity extends AppCompatActivity{

    private TextView onlineState;
    private Button goControlB;

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

    //用于处理到Web上线时发来的信息：改页面在线标志，改按钮可以点击
    public void onWebOnLineMessage(){

        onlineState = findViewById(R.id.OnlineState);
        goControlB = findViewById(R.id.goControlB);

        onlineState.setText("在线");
        onlineState.setTextColor(Color.parseColor("#00a600"));

        goControlB.setEnabled(true);

        goControlB.setOnClickListener(bocl);
    }

    //按钮全局点击事件
    public final View.OnClickListener bocl = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };

}
