package com.example.nbk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
FrameLayout promo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_dashboard);
        username =findViewById(R.id.username);
        user =findViewById(R.id.user);
        accnum =findViewById(R.id.accnum);
        pass =findViewById(R.id.pass);
        expdate =findViewById(R.id.expdate);
        front =findViewById(R.id.front);
        back =findViewById(R.id.back);
        promo = findViewById(R.id.promo);
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
                customer.FirstName = dataSnapshot.child("FirstName").getValue(String.class);
                customer.LastName = dataSnapshot.child("LastName").getValue(String.class);
                customer.Pin = dataSnapshot.child("Pin").getValue(String.class);
                customer.CreditCardNumber = dataSnapshot.child("CreditCardNumber").getValue(String.class);
                customer.ExpDate = dataSnapshot.child("ExpDate").getValue(String.class);
                Log.d(Customer.getFirstName() +"", "to Strig");
//                Log.d(customers.getPin(), "to Strig");
                pass.setText(Customer.getPin());
                username.setText(Customer.getFirstName());

                user.setText((Customer.getFirstName() + " "+Customer.getLastName()));
                accnum.setText(Customer.getCreditCardNumber());
                expdate.setText(Customer.getExpDate());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                front.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                front.setVisibility(View.VISIBLE);
            }
        });
promo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DashboardActivity.this, PromotionsActivity.class);

        startActivity(intent);
    }
});
    }
}