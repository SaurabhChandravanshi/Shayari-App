package com.i3developer.shayari;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Collections;
import java.util.List;

public class ShayariImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> dataList;
    public ShayariImageAdapter(List<Object> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shayari_image_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShayariImageData data = (ShayariImageData) dataList.get(position);

        MyViewHolder myViewHolder = (MyViewHolder)holder;
        //Get Firebase Storage Reference to load Image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReferenceFromUrl(data.getImagePath());
        // Load image into ImageView
        GlideApp.with(myViewHolder.itemView.getContext()).load(reference).into(myViewHolder.imageView);



        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myViewHolder.itemView.getContext(),ImageViewerActivity.class);
                intent.putExtra("imagePath", data.getImagePath());
                myViewHolder.itemView.getContext().startActivity(intent);
            }
        });

        myViewHolder.cardView.setCardBackgroundColor(myViewHolder.itemView.getContext().getResources().getColor(android.R.color.black));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shayai_img_list_image);
            cardView = itemView.findViewById(R.id.shayari_image_list_container);
        }
    }

}
