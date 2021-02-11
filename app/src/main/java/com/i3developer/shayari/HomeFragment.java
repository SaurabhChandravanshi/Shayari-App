package com.i3developer.shayari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private CardView categoryCard,addNewCard;
    private CardView gmShayariCard,gnShayariCard;
    private FirebaseAuth mAuth;
    private AdView adView1,adView2,adView3,adView4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allInitializations(view);
        loadPoetFragment();
        loadBannerAds();

        categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CategoryActivity.class));
            }
        });
        addNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getActivity(),SignupActivity.class));
                } else {
                    startActivity(new Intent(getActivity(),CreatePostActivity.class));
                }
            }
        });
        gmShayariCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","good_morning_shayari");
                intent.putExtra("name","Good Morning");
                startActivity(intent);
            }
        });
        gnShayariCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","good_night_shayari");
                intent.putExtra("name","Good Night");
                startActivity(intent);
            }
        });
    }

    private void loadPoetFragment() {
        PoetFragment fragment = new PoetFragment();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        fragmentManager.beginTransaction().replace(R.id.main_poet_frame,fragment).commit();
    }

    private void loadBannerAds() {
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);
        AdRequest adRequest4 = new AdRequest.Builder().build();
        adView4.loadAd(adRequest4);
    }

    private void allInitializations(View view) {
        categoryCard = view.findViewById(R.id.home_category);
        addNewCard = view.findViewById(R.id.home_add_new_shayari);
        gmShayariCard = view.findViewById(R.id.home_gm_shayari);
        gnShayariCard = view.findViewById(R.id.home_gn_shayari);
        mAuth = FirebaseAuth.getInstance();
        adView1 = view.findViewById(R.id.home_banner_ad1);
        adView2 = view.findViewById(R.id.home_banner_ad2);
        adView3 = view.findViewById(R.id.home_banner_ad3);
        adView4 = view.findViewById(R.id.home_banner_ad4);
    }
}
