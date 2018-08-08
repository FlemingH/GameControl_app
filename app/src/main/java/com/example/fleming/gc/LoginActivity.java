package com.example.fleming.gc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fleming.request.LoginRequest;
import com.example.fleming.request.OnlineRequest;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private boolean isAppOnline;
    private boolean isLoginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button lbutton = findViewById(R.id.lbutton);

        //按钮点击，新建登陆服务，用于验证，跳转页面时关闭服务
        lbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if("".equals(username.getText().toString()) || "".equals(username.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"请补全登录信息",Toast.LENGTH_SHORT).show();
                } else {

                    new Thread(new LoginHandler()).start();

                    if(isAppOnline){
                        Toast.makeText(getApplicationContext(),"账号已在其他地方登陆",Toast.LENGTH_SHORT).show();
                    } else {

                        if(isLoginSuccess){

                            Intent intent = new Intent(LoginActivity.this, OnlineActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("username", username.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(),"账号或密码输入错误",Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });
    }

    class LoginHandler implements Runnable{
        @Override
        public void run() {
            new Thread(new isAppOnlineHandler()).start();
            new Thread(new isLoginSuccessHandler()).start();
        }
    }

    class isLoginSuccessHandler implements Runnable{
        @Override
        public void run() {
            isLoginSuccess = LoginRequest.IsLoginSuccess(username.getText().toString(), password.getText().toString());
        }
    }

    class isAppOnlineHandler implements Runnable{
        @Override
        public void run() {
            isAppOnline = OnlineRequest.isAppOnline(username.getText().toString());
        }
    }

}
