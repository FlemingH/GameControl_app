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
import com.example.fleming.util.Tools;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button lbutton = findViewById(R.id.lbutton);

        lbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if("".equals(username.getText().toString()) || "".equals(password.getText().toString())){

                    Toast.makeText(LoginActivity.this,"请补全信息",Toast.LENGTH_SHORT).show();

                } else {

                    boolean isAppOnline = OnlineRequest.isAppOnline(username.getText().toString());

                    if(isAppOnline) {
                        Toast.makeText(LoginActivity.this,"账号已在其他地方登录",Toast.LENGTH_SHORT).show();
                    } else {

                        boolean flag = LoginRequest.IsLoginSuccess(username.getText().toString(), password.getText().toString());

                        if (flag) {
                            //直接跳转，在下个页面创建的时候向服务器发送我上线的请求
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, OnlineActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("username",username.getText().toString());

                            intent.putExtras(bundle);

                            startActivityForResult(intent,0);
                        } else {
                            Toast.makeText(LoginActivity.this,"账号或密码输入错误",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

        });

    }
}
