package com.example.nbk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
FrameLayout fl ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS );
        setContentView(R.layout.activity_login);
fl = findViewById(R.id.btn);
fl.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity( new Intent(LoginActivity.this,
                DashboardActivity.class));
    }
});

    }
}