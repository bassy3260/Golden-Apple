package com.aaa.aaa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

        Button sign;
        Button login;

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            //회원가입 버튼
            sign = findViewById(R.id.signin);

            //회원가입 버튼 클릭시, 회원가입 페이지로 이동
            sign.setOnClickListener(v -> {
                Intent intent = new Intent(this, Signup.class);
                startActivity(intent);
            });

            login = findViewById(R.id.login);

            login.setOnClickListener(v -> {
                Intent intent = new Intent(this,BottomActivity.class);
                startActivity(intent);
            });
        }
    }