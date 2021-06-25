package com.example.nbk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PromotionsActivity extends AppCompatActivity {
FrameLayout filter;
    Context context;
    String UID;
    EditText search ;
    private RecyclerView mRecyclerView;

    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Backend be = new Backend();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String keyword = "Travel";
    FrameLayout scanprom;

//    ArrayList<Promotion> promos = new ArrayList<Promotion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //be.collectPromotions(FirebaseAuth.getInstance().getUid());

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        Intent i = getIntent();

        scanprom = findViewById(R.id.scan);
        search = findViewById(R.id.search);
        String uri = i.getStringExtra("category");
        search.setText(uri);
        filter = findViewById(R.id.filter);
        Backend backend = new Backend();
        scanprom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PromotionsActivity.this, QRScanner.class));
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = String.valueOf(search.getText());
                Log.d("TAG", "onClick: WE GOT HERE");
                backend.filterPromotions(UID, temp, mRecyclerView);
            }
        });
        try {
            context = this;
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            backend.collectPromotions(UID, mRecyclerView);
//            getItemList();

//            backend.filterPromotions(UID,"Travel",  mRecyclerView);

            mAdapter.setOnItemClicklListener(new ItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(context, "Clicked item position: " + position, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception ex) {
            Log.e("TAG", ex.getMessage());
        }
    }



            private ArrayList<Promotion> getItemList() {

                ArrayList<Promotion> promos = new ArrayList<Promotion>();
                DatabaseReference myRef = database.getReference("Customers/" + UID + "/Promotions");
                myRef.orderByKey().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        Promotion p = new Promotion(dataSnapshot.getKey(),
                                dataSnapshot.child("Percent").getValue().toString(),
                                dataSnapshot.child("Category").getValue().toString());
                        promos.add(p);
                        Log.d("Promo", p.toString());
                        mAdapter = new ItemAdapter(promos);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
return null;
            }}



