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

import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private CardView categoryCard,addNewCard;
    private CardView gmShayariCard,gnShayariCard,kumarVishwasShayari;
    private CardView gulzarShayari;
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
        kumarVishwasShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","kumar_vishwas_shayari");
                intent.putExtra("name","Kumar Vishwas");
                startActivity(intent);
            }
        });
        gulzarShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","gulzar");
                intent.putExtra("name","Gulzar");
                startActivity(intent);
            }
        });
    }

    private void allInitializations(View view) {
        categoryCard = view.findViewById(R.id.home_category);
        addNewCard = view.findViewById(R.id.home_add_new_shayari);
        gmShayariCard = view.findViewById(R.id.home_gm_shayari);
        gnShayariCard = view.findViewById(R.id.home_gn_shayari);
        mAuth = FirebaseAuth.getInstance();
        kumarVishwasShayari = view.findViewById(R.id.home_kumar_vishwas_shayari);
        gulzarShayari = view.findViewById(R.id.home_gulzar_shayari);
    }
}
