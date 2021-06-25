package com.example.nbk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class PromotionsActivity extends AppCompatActivity {
FrameLayout filter;
    Context context;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        filter = findViewById(R.id.filter);
        ConstraintLayout constraintLayout = findViewById(R.id.constrain);

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
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
