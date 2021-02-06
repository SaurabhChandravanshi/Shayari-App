package com.i3developer.shayari;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

public class PublicPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> dataList;
    private static final int AD_VIEW = 0;
    private static final int RECYCLER_VIEW = 1;
    public PublicPostAdapter(List<Object> dataList) {
        this.dataList = dataList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==AD_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_shayari_list,parent,false);
            return new MyAdViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_post_list, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if(pos!=0) {
            if(pos%3==0) {
                return AD_VIEW;
            }
            return RECYCLER_VIEW;
        }
        return RECYCLER_VIEW;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(dataList.get(position) instanceof AdView) {
            MyAdViewHolder myAdViewHolder = (MyAdViewHolder)holder;
            myAdViewHolder.adView.loadAd(new AdRequest.Builder().build());
        } else {
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            PublicPost data = (PublicPost)dataList.get(position);

            //Get Firebase Storage Reference to load Image
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReferenceFromUrl("gs://shayari-5b5f4.appspot.com/"+data.getImagePath());
            // Load image into ImageView
            GlideApp.with(myViewHolder.itemView.getContext()).load(reference).centerCrop().into(myViewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Button shareBtn,likeBtn,commentBtn;
        public MyViewHolder(View itemView) {
            super(itemView);
            shareBtn = itemView.findViewById(R.id.public_post_list_share);
            likeBtn = itemView.findViewById(R.id.public_post_list_like);
            commentBtn = itemView.findViewById(R.id.public_post_list_comment);
            imageView = itemView.findViewById(R.id.public_post_list_image);
        }
    }
    class MyAdViewHolder extends RecyclerView.ViewHolder {

        private AdView adView;
        public MyAdViewHolder(@NonNull View itemView) {
            super(itemView);
            adView = itemView.findViewById(R.id.recycler_adview);
        }
    }
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    private void copyToClipboard(Context context,String label,String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        showToast(context,"Copied to Clipboard");
    }
    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private void saveAndShareImage(Context context,Bitmap bitmap) {
        try {
            File cacheFile = new File(context.getCacheDir(),"images");
            cacheFile.mkdir();
            FileOutputStream output = new FileOutputStream(cacheFile+"/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();

            // Share Image
            shareImage(context);
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
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }
}
