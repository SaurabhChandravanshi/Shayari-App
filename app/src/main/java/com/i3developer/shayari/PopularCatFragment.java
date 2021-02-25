package com.i3developer.shayari;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class PopularCatFragment extends Fragment {
    private Button allCatBtn;
    private CardView twoLineShayari,loveShayari,sadShayari,birthdayWishesShayari,friendshipShayari,motivationalShayari;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_for_popular_cat,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allInitialization(view);

        twoLineShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","two_line_shayari");
                intent.putExtra("name","2 Line Shayari");
                startActivity(intent);
            }
        });
        loveShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","love_shayari");
                intent.putExtra("name","Love Shayari");
                startActivity(intent);
            }
        });
        sadShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","sad_shayari");
                intent.putExtra("name","Sad Shayari");
                startActivity(intent);
            }
        });
        birthdayWishesShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","birthday_wishes_shayari");
                intent.putExtra("name","Birthday Wishes");
                startActivity(intent);
            }
        });
        motivationalShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","motivational_shayari");
                intent.putExtra("name","Motivational");
                startActivity(intent);
            }
        });
        friendshipShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","friendship_shayari");
                intent.putExtra("name","Friendship");
                startActivity(intent);
            }
        });
        allCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CategoryActivity.class));
            }
        });
    }

    private void allInitialization(View view) {
        twoLineShayari = view.findViewById(R.id.popular_2_lines);
        loveShayari = view.findViewById(R.id.popular_love_shayari);
        sadShayari = view.findViewById(R.id.popular_sad_shayari);
         birthdayWishesShayari = view.findViewById(R.id.popular_birthday_wishes);
        friendshipShayari = view.findViewById(R.id.popular_friendship);
        motivationalShayari = view.findViewById(R.id.popular_motivational);
        allCatBtn = view.findViewById(R.id.popular_header_btn);
    }
}
