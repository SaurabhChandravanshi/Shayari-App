package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShayariImageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private ProgressBar progressBar;
    private List<Object> dataList = new ArrayList<>();
    private String CATEGORY,TITLE;
    TextView appBarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shayari_image);
        loadBannerAds();
        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        ImageView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init();
        loadImages();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void init() {
        recyclerView = findViewById(R.id.shayari_img_recycler);
        layoutManager = new GridLayoutManager(this,2);
        adapter = new ShayariImageAdapter(dataList);
        progressBar = findViewById(R.id.shayari_img_pBar);
        CATEGORY = getIntent().getStringExtra("category");
        TITLE = getIntent().getStringExtra("name");
        appBarTitle.setText(TITLE);
    }

    private void loadImages() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("shayariImages").whereArrayContains("categories",CATEGORY).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            ShayariImageData data = documentSnapshot.toObject(ShayariImageData.class);
                            dataList.add(data);
                            loadRecycler();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void loadRecycler() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void loadBannerAds() {
        AdView adView1  = findViewById(R.id.shayari_image_banner_ad1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);
    }
}