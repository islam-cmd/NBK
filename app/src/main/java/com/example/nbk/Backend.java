package com.example.nbk;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

// for encryption/decryption
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Backend {

    // https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
    private static final String SECRET_KEY = "akim_sekrit_kiy!!!!!";
    private static final String SALT = "pipirIz!@#gud";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String strToEncrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String strToDecrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String UserPaymentLink(String UID, String firstName, String lastName, float amount){
        return "nbk-pay-"+amount+"-"+encrypt(UID)+"-"+firstName+" "+lastName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String UserRecieveLink(String UID, String firstName, String lastName, float amount){
        return "nbk-recieve-"+amount+"-"+encrypt(UID)+"-"+firstName+" "+lastName;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void UserOnePaysUser2(String UID1Hashed, String UID2, float amount){
        String UID1 = decrypt(UID1Hashed);
        DatabaseReference myRef1 = database.getReference("Customers/"+UID1+"/Balance");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                float value = dataSnapshot.getValue(float.class);
                Log.d("TAG", "Value is: " + value);
                myRef1.setValue(value-amount);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        DatabaseReference myRef2 = database.getReference("Customers/"+UID2+"/Balance");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                float value = dataSnapshot.getValue(float.class);
                Log.d("TAG", "Value is: " + value);
                myRef2.setValue(value+amount);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void UserOneRecievesFromUser2(String UID1Hashed, String UID2, float amount){
        String UID1 = decrypt(UID1Hashed);
        DatabaseReference myRef1 = database.getReference("Customers/"+UID1+"/Balance");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                float value = dataSnapshot.getValue(float.class);
                Log.d("TAG", "Value is: " + value);
                myRef1.setValue(value+amount);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        DatabaseReference myRef2 = database.getReference("Customers/"+UID2+"/Balance");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                float value = dataSnapshot.getValue(float.class);
                Log.d("TAG", "Value is: " + value);
                myRef2.setValue(value-amount);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
