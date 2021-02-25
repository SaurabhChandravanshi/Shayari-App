package com.i3developer.shayari;

import android.content.Intent;
import android.net.Uri;
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
    private ImageView gulzarImage,ghalibImage,rahatIndoriImage,kumarVishwasImage,faizAhmadFaizImage,bashirBadrImage,anamikaJainImage;
    private CardView gulzar,ghalib,rahatIndori,kumarVishwas,faizAhmadFaiz,bashirBadr,anamikaJain;
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
        faizAhmadFaiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","faiz_ahmad_faiz");
                intent.putExtra("name","Faiz Ahmad Faiz");
                startActivity(intent);
            }
        });
        bashirBadr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","bashir_badr");
                intent.putExtra("name","Bashir Badr");
                startActivity(intent);
            }
        });
        anamikaJain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ShayariActivity.class);
                intent.putExtra("category","anamika_jain");
                intent.putExtra("name","Anamika Jain");
                startActivity(intent);
            }
        });
    }

    private void loadPoetImages() {
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/gulzar_image.jpg")).circleCrop().into(gulzarImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/ghalib_image.png")).circleCrop().into(ghalibImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/rahat_indori_image.jpg")).circleCrop().into(rahatIndoriImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/kumar_vishwash_image.jpg")).circleCrop().into(kumarVishwasImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/faiz-ahmad-faiz.png")).circleCrop().into(faizAhmadFaizImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/bashir-badr-image.jpg")).circleCrop().into(bashirBadrImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/anamika-jain.jpg")).circleCrop().into(anamikaJainImage);
    }

    private void allInitialization(View view) {
        gulzarImage = view.findViewById(R.id.poet_gulzar_image);
        ghalibImage = view.findViewById(R.id.poet_ghalib_image);
        rahatIndoriImage = view.findViewById(R.id.poet_rahat_indori_image);
        kumarVishwasImage = view.findViewById(R.id.poet_kumar_vishwas_image);
        faizAhmadFaizImage = view.findViewById(R.id.poet_faiz_ahmad_faiz_image);
        bashirBadrImage = view.findViewById(R.id.poet_bashir_badr_image);
        anamikaJainImage = view.findViewById(R.id.poet_anamika_jain_image);

        gulzar = view.findViewById(R.id.poet_gulzar);
        ghalib = view.findViewById(R.id.poet_ghalib);
        rahatIndori = view.findViewById(R.id.poet_rahat_indori);
        kumarVishwas = view.findViewById(R.id.poet_kumar_vishwas);
        faizAhmadFaiz = view.findViewById(R.id.poet_faiz_ahmad_faiz);
        bashirBadr = view.findViewById(R.id.poet_bashir_badr);
        anamikaJain = view.findViewById(R.id.poet_anamika_jain);
    }
}
