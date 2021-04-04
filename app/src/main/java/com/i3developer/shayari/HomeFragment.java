package com.i3developer.shayari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private CardView categoryCard;
    private CardView gmShayariCard,positiveQuote;
    private FirebaseAuth mAuth;
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
        loadPopularCatFragment();
        loadWishesCatFragment();

        categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CategoryActivity.class));
            }
        });
        gmShayariCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","good_morning_shayari");
                intent.putExtra("name","Good Morning Shayari in Hindi");
                startActivity(intent);
            }
        });
        positiveQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),PositiveQCatActivity.class));
            }
        });
    }

    private void loadPoetFragment() {
        PoetFragment fragment = new PoetFragment();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        fragmentManager.beginTransaction().replace(R.id.main_poet_frame,fragment).commit();
    }
    private void loadPopularCatFragment() {
        PopularCatFragment fragment = new PopularCatFragment();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        fragmentManager.beginTransaction().replace(R.id.main_popular_categories_frame,fragment).commit();
    }
    private void loadWishesCatFragment() {
        WishesCatFragment fragment = new WishesCatFragment();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        fragmentManager.beginTransaction().replace(R.id.main_wishes_categories_frame,fragment).commit();
    }

    private void allInitializations(View view) {
        categoryCard = view.findViewById(R.id.home_category);
        gmShayariCard = view.findViewById(R.id.home_gm_shayari);
        positiveQuote = view.findViewById(R.id.home_positive_quote);
        mAuth = FirebaseAuth.getInstance();
    }
}
