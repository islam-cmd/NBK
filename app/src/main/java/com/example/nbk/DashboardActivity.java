package com.example.nbk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.nbk.Customer;
public class DashboardActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser fuser;
    TextView username,user,accnum, pass,  expdate;
    RelativeLayout front , back;
    FrameLayout promoLayout;
ImageView location;
    FrameLayout payLayout;
    FrameLayout recieveLayout;
    FrameLayout scannerLayout;
    String ccl ="";
    ImageView eye;
    String hiddaccnum;
String acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        promoLayout = findViewById(R.id.promo);
        payLayout = findViewById(R.id.pay);
    eye = findViewById(R.id.eye);
        recieveLayout = findViewById(R.id.recieve);
        location = findViewById(R.id.location);
        scannerLayout = findViewById(R.id.scanner);
        username =findViewById(R.id.username);
        user =findViewById(R.id.user);
        accnum =findViewById(R.id.accnum);

        pass =findViewById(R.id.pass);
        expdate =findViewById(R.id.expdate);
        front =findViewById(R.id.front);
        back =findViewById(R.id.back);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("Customers/"+fuser.getUid());
        Log.d("WTH", FirebaseAuth.getInstance().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                float value = dataSnapshot.getValue(float.class);
                Customer customer = dataSnapshot.getValue(Customer.class);
//                customer.Balance = dataSnapshot.c
                customer.FirstName = dataSnapshot.child("FirstName").getValue(String.class);
                customer.LastName = dataSnapshot.child("LastName").getValue(String.class);
                customer.Pin = dataSnapshot.child("Pin").getValue(String.class);
                customer.CreditCardNumber = dataSnapshot.child("CreditCardNumber").getValue(String.class);
                hiddaccnum = Customer.getCreditCardNumber();
                acc = Customer.getCreditCardNumber();
                hiddaccnum  = "****-****-****-"+  hiddaccnum.substring(15);

                customer.ExpDate = dataSnapshot.child("ExpDate").getValue(String.class);
                customer.CCLocation =  dataSnapshot.child("CCLocation").getValue(String.class);
             ccl = Customer.getCCLocation();
                Log.d("TAG", "onDataChange: " +ccl );
                Log.d(Customer.getFirstName() +"", "to String");
                pass.setText(Customer.getPin());
                username.setText(Customer.getFirstName());

                user.setText((Customer.getFirstName() + " " + Customer.getLastName()));
                accnum.setText(hiddaccnum);
                expdate.setText(Customer.getExpDate());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
eye.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(accnum.getText().equals(acc)){
            accnum.setText(hiddaccnum);
        }else {
            accnum.setText(acc);
        }
    }
});
        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                front.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        });
location.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity.this).create();
           alertDialog.setTitle("Location");
            alertDialog.setMessage( "Your card is in " + ccl);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

        });
        alertDialog.show();
    }
});
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                front.setVisibility(View.VISIBLE);
            }
        });
        promoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, PromotionsActivity.class));
            }
        });
        payLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, Pay.class));
            }
        });

        recieveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, Recieve.class));
            }
        });
        scannerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, QRScanner.class));
            }
        });

    }
}