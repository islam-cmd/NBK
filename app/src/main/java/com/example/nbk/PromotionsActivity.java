package com.example.nbk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PromotionsActivity extends AppCompatActivity {

    Context context;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Backend be = new Backend();
//    ArrayList<Promotion> promos = new ArrayList<Promotion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //be.collectPromotions(FirebaseAuth.getInstance().getUid());
        be.filterPromotions(FirebaseAuth.getInstance().getUid(), "travel");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

                try {
                    context = this;
                    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

                    mLayoutManager = new LinearLayoutManager(this);
                    mRecyclerView.setLayoutManager(mLayoutManager);


                    mAdapter = new ItemAdapter(getItemList());
                    mRecyclerView.setAdapter(mAdapter);
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

            private List<DataModel> getItemList() {
                Log.d("HAAAAAA", "Reached the funvtion");
                List<DataModel> models = new ArrayList<>();
                models.add(new DataModel("Item Title 1", "01 Jan, 2018"));
                models.add(new DataModel("Item Title 2", "02 Jan, 2018"));
                models.add(new DataModel("Item Title 3", "03 Jan, 2018"));
                models.add(new DataModel("Item Title 4", "04 Jan, 2018"));
                models.add(new DataModel("Item Title 5", "05 Jan, 2018"));
                models.add(new DataModel("Item Title 6", "06 Jan, 2018"));
                models.add(new DataModel("Item Title 7", "07 Jan, 2018"));
                models.add(new DataModel("Item Title 8", "08 Jan, 2018"));
                models.add(new DataModel("Item Title 9", "09 Jan, 2018"));
                models.add(new DataModel("Item Title 10", "10 Jan, 2018"));
                models.add(new DataModel("Item Title 11", "11 Jan, 2018"));
                models.add(new DataModel("Item Title 12", "12 Jan, 2018"));

                return models;
            }
        }
