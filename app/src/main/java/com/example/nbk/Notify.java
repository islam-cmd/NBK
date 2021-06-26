package com.example.nbk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.common.StringUtils;

import net.glxn.qrgen.android.QRCode;

public class Notify extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextView notification;
    Button accept;
    Button reject;
    Backend be = new Backend();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        notification = findViewById(R.id.notification);
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = database.getReference("Customers/"+UID);

        Intent i = getIntent();
        String[] qrCode = i.getStringArrayExtra("qrArray");

        for(String s : qrCode){
            Log.d("qr123", s);
        }

        if(qrCode.length != 5){
            startActivity(new Intent(Notify.this, DashboardActivity.class));
        }

        float amount = 0;
        try {
            amount = Float.parseFloat(qrCode[2]);
        } catch (NumberFormatException nfe) {
            startActivity(new Intent(Notify.this, DashboardActivity.class));
        }

        String typeFormat = (qrCode[1] == "pay") ? "pay you " : "recieve from you";

        notification.setText(qrCode[4] + " wants to " + typeFormat + ": " + qrCode[2] + " KD. Do you Accept?");

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        float finalAmount = amount;
        accept.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.d("pay/recieve", qrCode[1]);
                if(qrCode[1].equals("pay")){
                    be.UserOnePaysUser2(qrCode[3], UID, finalAmount);
                } else if(qrCode[1].equals("recieve")){
                    be.UserOneRecievesFromUser2(qrCode[3], UID, finalAmount);
                }
                //be.UserOneRecievesFromUser2(qrCode[3], UID, finalAmount);
                startActivity(new Intent(Notify.this, DashboardActivity.class));
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notify.this, DashboardActivity.class));
            }
        });

    }
}