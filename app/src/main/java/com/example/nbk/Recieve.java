package com.example.nbk;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.glxn.qrgen.android.QRCode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class Recieve extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Backend backend = new Backend();
    Customer customer;
    Button confirm;
    EditText amount;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve);

//        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //FIX LATER
        String UID = "sflF8HgFLkQDXGe4PaZBmcYKi663";
        DatabaseReference myRef = database.getReference("Customers/"+UID);
        Log.d("WTH", FirebaseAuth.getInstance().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                customer = dataSnapshot.getValue(Customer.class);
                customer.FirstName = dataSnapshot.child("FirstName").getValue(String.class);
                customer.LastName = dataSnapshot.child("LastName").getValue(String.class);
                customer.Balance = dataSnapshot.child("Balance").getValue(float.class);
//                Pay.this.notifyAll();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        amount = findViewById(R.id.amount);
        confirm = findViewById(R.id.idBtnGenerateQR);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = String.valueOf(amount.getText());
                if(value.isEmpty()){
                    return;
                }

                Bitmap myBitmap = QRCode.from(backend.UserRecieveLink(UID, customer.FirstName, customer.LastName, Float.parseFloat(value))).bitmap();
                ImageView myImage = (ImageView) findViewById(R.id.idIVQrcode);
                myImage.setImageBitmap(myBitmap);
            }
        });
    }
}