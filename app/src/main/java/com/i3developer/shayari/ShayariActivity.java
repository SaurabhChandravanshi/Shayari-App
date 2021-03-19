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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shayari);
        allInitializations();
        loadInterstitialAd();
        getShayariFromFirebase();

       TextView appBarTitle = findViewById(R.id.shayari_app_bar_title);
       appBarTitle.setText(NAME);
    }

    @Override
    public void onBackPressed() {
        if(mInterstitialAd != null) {
            mInterstitialAd.show(ShayariActivity.this);
        }
        super.onBackPressed();
    }
    public void back(View view) {
        finish();
    }
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,getResources().getString(R.string.shayari_interstitial_ad),adRequest,new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd  = null;
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