package com.i3developer.shayari;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class LeadersFragment extends Fragment {
    private ImageView vivekanandImage,mahatamaGandhiImage;
    private ImageView sardarImage,bhagatSinghImages,kalamImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_for_leaders,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allInitialization(view);
        loadLeaderImages();
    }

    private void loadLeaderImages() {
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/vivekanand.png")).circleCrop().into(vivekanandImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/mahatama_gandhi.jpg")).circleCrop().into(mahatamaGandhiImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/sardar.jpg")).circleCrop().into(sardarImage);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/bhagat_singh.jpg")).circleCrop().into(bhagatSinghImages);
        Glide.with(getActivity()).load(Uri.parse("file:///android_asset/kalam.jpg")).circleCrop().into(kalamImage);
    }
    private void allInitialization(View view) {
        vivekanandImage = view.findViewById(R.id.leader_vivekanand_image);
        mahatamaGandhiImage = view.findViewById(R.id.leader_mahatama_gandhi_image);
        sardarImage = view.findViewById(R.id.leader_patel_image);
        bhagatSinghImages = view.findViewById(R.id.leader_bhagat_singh_image);
        kalamImage = view.findViewById(R.id.leader_kalam_image);
    }
}
