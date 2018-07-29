package com.example.fleming.gc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fleming.request.LoginRequest;

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
                boolean flag = LoginRequest.IsLoginSuccess(username.getText().toString(), password.getText().toString());

                if (flag) {
                    Intent intent = new Intent(LoginActivity.this, OnlineActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this,"账号或密码输入错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
