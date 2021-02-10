package com.i3developer.shayari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class PoetFragment extends Fragment {
    private ImageView gulzarImage,ghalibImage,rahatIndoriImage,kumarVishwasImage;
    private CardView gulzar,ghalib,rahatIndori,kumarVishwas;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_for_poet,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allInitialization(view);
        loadPoetImages();

        gulzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","gulzar");
                intent.putExtra("name","Gulzar");
                startActivity(intent);
            }
        });
        ghalib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","ghalib_shayari");
                intent.putExtra("name","Ghalib");
                startActivity(intent);
            }
        });
        rahatIndori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","rahat_indori_shayari");
                intent.putExtra("name","Rahat Indori");
                startActivity(intent);
            }
        });
        kumarVishwas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","kumar_vishwas_shayari");
                intent.putExtra("name","Kumar Vishwas");
                startActivity(intent);
            }
        });
    }

    private void loadPoetImages() {
        Glide.with(getActivity()).load(R.drawable.gulzar_image).circleCrop().into(gulzarImage);
        Glide.with(getActivity()).load(R.drawable.ghalib_image).circleCrop().into(ghalibImage);
        Glide.with(getActivity()).load(R.drawable.rahat_indori_image).circleCrop().into(rahatIndoriImage);
        Glide.with(getActivity()).load(R.drawable.kumar_vishwash_image).circleCrop().into(kumarVishwasImage);
    }

    private void allInitialization(View view) {
        gulzarImage = view.findViewById(R.id.poet_gulzar_image);
        ghalibImage = view.findViewById(R.id.poet_ghalib_image);
        rahatIndoriImage = view.findViewById(R.id.poet_rahat_indori_image);
        kumarVishwasImage = view.findViewById(R.id.poet_kumar_vishwas_image);

        gulzar = view.findViewById(R.id.poet_gulzar);
        ghalib = view.findViewById(R.id.poet_ghalib);
        rahatIndori = view.findViewById(R.id.poet_rahat_indori);
        kumarVishwas = view.findViewById(R.id.poet_kumar_vishwas);
    }
}
