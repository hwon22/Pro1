package com.android.pro1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void newBtnClick(View view) {
       if(view.getId()==R.id.newBtn){
            Intent intent = new Intent(SplashActivity.this,NewActivity.class);
            startActivity(intent);
       }
    }
}
