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
    private ImageView mirTaqiMirImage,bakshNasikhImage,allamaIqbalImage;
    private ImageView akbarAllahabadiImage,parveenShakirImage;

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
    }

    private void loadPoetImages() {
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/gulzar_image.jpg")).circleCrop().into(gulzarImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/ghalib_image.png")).circleCrop().into(ghalibImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/rahat_indori_image.jpg")).circleCrop().into(rahatIndoriImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/kumar_vishwash_image.jpg")).circleCrop().into(kumarVishwasImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/faiz-ahmad-faiz.png")).circleCrop().into(faizAhmadFaizImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/bashir-badr-image.jpg")).circleCrop().into(bashirBadrImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/anamika-jain.jpg")).circleCrop().into(anamikaJainImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/baksh_nasikh.jpg")).circleCrop().into(bakshNasikhImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/mir-taqi-mir.jpg")).circleCrop().into(mirTaqiMirImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/allama-iqbal.png")).circleCrop().into(allamaIqbalImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/akbar_allahabadi.jpg")).circleCrop().into(akbarAllahabadiImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/parveen_shakir.jpg")).circleCrop().into(parveenShakirImage);
    }

    private void allInitialization(View view) {
        gulzarImage = view.findViewById(R.id.poet_gulzar_image);
        ghalibImage = view.findViewById(R.id.poet_ghalib_image);
        rahatIndoriImage = view.findViewById(R.id.poet_rahat_indori_image);
        kumarVishwasImage = view.findViewById(R.id.poet_kumar_vishwas_image);
        faizAhmadFaizImage = view.findViewById(R.id.poet_faiz_ahmad_faiz_image);
        bashirBadrImage = view.findViewById(R.id.poet_bashir_badr_image);
        anamikaJainImage = view.findViewById(R.id.poet_anamika_jain_image);
        bakshNasikhImage = view.findViewById(R.id.poet_baksh_nasikh_image);
        mirTaqiMirImage = view.findViewById(R.id.poet_mir_taqi_mir_image);
        allamaIqbalImage = view.findViewById(R.id.poet_allama_iqbal_image);
        akbarAllahabadiImage = view.findViewById(R.id.poet_akbar_allahabadi_image);
        parveenShakirImage = view.findViewById(R.id.poet_parveen_shakir_image);
    }
}
