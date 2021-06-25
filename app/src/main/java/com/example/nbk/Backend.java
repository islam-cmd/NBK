package com.example.nbk;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Backend {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ArrayList<Promotion> collectPromotions(String UID, RecyclerView mRecyclerView){

        ArrayList<Promotion> promos = new ArrayList<Promotion>();
        DatabaseReference myRef = database.getReference("Customers/"+UID+"/Promotions");
//        DataSnapshot dataSnapshot = myRef.get().getResult();
        if (mRecyclerView.getAdapter() != null){
            mRecyclerView.setAdapter(null);
        }
        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Promotion p = new Promotion(dataSnapshot.getKey(),
                        dataSnapshot.child("Percent").getValue().toString(),
                        dataSnapshot.child("Category").getValue().toString());
                promos.add(p);

                mRecyclerView.setAdapter(new ItemAdapter(promos));
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
        return promos;
    }


    public ArrayList<Promotion> filterPromotions(String UID, String keyword,  RecyclerView mRecyclerView) {

        ArrayList<Promotion> promos = new ArrayList<Promotion>();
        DatabaseReference myRef = database.getReference("Customers/"+UID+"/Promotions");
        Log.d("Keyword", keyword);
        Log.d("UID", UID);

        Log.d("next", "----");



        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d("Child exists", "true");
                String name = dataSnapshot.getKey();
                String percent = dataSnapshot.child("Percent").getValue().toString();
                String category = dataSnapshot.child("Category").getValue().toString();
                Log.d("name", name);
                Log.d("category", category);
                Log.d("next", "----");


                if(name.toLowerCase().contains(keyword.toLowerCase()) || category.toLowerCase().contains(keyword.toLowerCase())){
                    promos.add(new Promotion(name, percent, category));
                    mRecyclerView.setAdapter(new ItemAdapter(promos));
                }
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
        return promos;
    }

    public void deduct(String UID, float amount){
        DatabaseReference myRef = database.getReference("Customers/"+UID+"/Balance");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                float value = dataSnapshot.getValue(float.class);
                Log.d("TAG", "Value is: " + value);
                myRef.setValue(value-amount);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
