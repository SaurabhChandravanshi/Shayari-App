package com.i3developer.shayari;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextShayariFragment  extends Fragment {

    private String CATEGORY,NAME;
    private DatabaseReference dbReference;
    private List<Object> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;
    public TextShayariFragment(String CATEGORY,String NAME) {
        this.CATEGORY = CATEGORY;
        this.NAME = NAME;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_shayari,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getShayariFromFirebase();
    }

    private void init(View view) {
        adapter = new ShayariAdapter(dataList);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView = view.findViewById(R.id.text_shayari_recycler);
        progressBar = view.findViewById(R.id.text_shayari_pBar);
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
                    if(getActivity() != null) {
                        addBannersToRecycler();
                    }
                    loadRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                showToast(getActivity(),"Internal error occurred.");
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
            AdView adView = new AdView(getActivity());
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
