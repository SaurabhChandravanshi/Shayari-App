package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShayariActivity extends AppCompatActivity {

    private String CATEGORY,NAME;
    private DatabaseReference dbReference;
    private List<Object> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shayari);
        allInitializations();
        getShayariFromFirebase();

        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        TextView appBarRight = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_right);
        appBarTitle.setText(NAME);
        appBarLeft.setText("Back");
        appBarRight.setText("Share");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        appBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Download Shayari Book App");
                intent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book App\n"+
                        "https://play.google.com/store/apps/details?id=com.i3developer.shayari");
                startActivity(intent);
            }
        });
    }

    private void allInitializations() {
        CATEGORY = getIntent().getStringExtra("category");
        NAME = getIntent().getStringExtra("name");
        adapter = new ShayariAdapter(dataList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.shayari_recycler);
        progressBar = findViewById(R.id.shayari_pBar);
    }
    private void getShayariFromFirebase() {
        dbReference = FirebaseDatabase.getInstance().getReference(CATEGORY);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        Shayari shayari = dataSnapshot.getValue(Shayari.class);
                        dataList.add(shayari);
                    }
                    Collections.shuffle(dataList);
                    addBannersToRecycler();
                    loadRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                showToast(getApplicationContext(),"Internal error occurred.");
            }
        });
    }

    private void loadRecycler() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void addBannersToRecycler() {
        for(int i=3;i<dataList.size();i=i+3) {
            AdView adView = new AdView(getApplicationContext());
            dataList.add(i,adView);
        }
    }
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}