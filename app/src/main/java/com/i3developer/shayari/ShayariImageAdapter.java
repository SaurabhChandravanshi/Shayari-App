package com.i3developer.shayari;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        myViewHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToCache(myViewHolder.itemView.getContext(),viewToBitmap(myViewHolder.imageView));
                //Share Image
                shareImage(myViewHolder.itemView.getContext());
            }
        });
        myViewHolder.waBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToCache(myViewHolder.itemView.getContext(),viewToBitmap(myViewHolder.imageView));
                //Share Image
                shareImageViaWA(myViewHolder.itemView.getContext());
            }
        });
        myViewHolder.fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToCache(myViewHolder.itemView.getContext(),viewToBitmap(myViewHolder.imageView));
                //Share Image
                shareImageViaFB(myViewHolder.itemView.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private Button shareBtn,waBtn,fbBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shayai_img_list_image);
            waBtn = itemView.findViewById(R.id.shayari_img_list_wa_btn);
            shareBtn = itemView.findViewById(R.id.shayari_img_list_share_btn);
            fbBtn = itemView.findViewById(R.id.shayari_img_list_fb_btn);
        }
    }
    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private void saveImageToCache(Context context, Bitmap bitmap) {
        try {
            File cacheFile = new File(context.getCacheDir(),"images");
            cacheFile.mkdir();
            FileOutputStream output = new FileOutputStream(cacheFile+"/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void shareImage(Context context) {
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.i3developer.shayari.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app for more\nhttps://shayariapp.page.link/install");
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }
    private void shareImageViaWA(Context context) {
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.i3developer.shayari.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app for more\nhttps://shayariapp.page.link/install");
            shareIntent.setPackage("com.whatsapp");
            try {
                context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void shareImageViaFB(Context context) {
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.i3developer.shayari.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app for more\nhttps://shayariapp.page.link/install");
            shareIntent.setPackage("com.facebook.katana");
            try {
                context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
