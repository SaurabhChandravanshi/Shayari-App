package com.i3developer.shayari;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ViewPostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> dataList;
    private static final int AD_VIEW = 0;
    private static final int RECYCLER_VIEW = 1;
    private FirebaseAuth mAuth;
    private FragmentManager fragmentManager;
    public ViewPostsAdapter(List<Object> dataList, FragmentManager fragmentManager) {
        this.dataList = dataList;
        this.fragmentManager = fragmentManager;
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==AD_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_shayari_list,parent,false);
            return new MyAdViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_posts_list, parent, false);
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
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            //Get Firebase Storage Reference to load Image
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReferenceFromUrl("gs://shayari-5b5f4.appspot.com/"+data.getImagePath());
            // Load image into ImageView
            GlideApp.with(myViewHolder.itemView.getContext()).load(reference).centerCrop().into(myViewHolder.imageView);
            updateLikeCount(data.getLikes(),myViewHolder);
            updateLikeBtn(data.getLikes(),myViewHolder,myViewHolder.likeBtn);

            myViewHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getLikes().contains(mAuth.getUid())) {
                        data.getLikes().remove(mAuth.getUid());
                    } else {
                        data.getLikes().add(mAuth.getUid());
                    }
                    updateLikeBtn(data.getLikes(),myViewHolder,myViewHolder.likeBtn);
                    updateLikeCount(data.getLikes(),myViewHolder);
                    updateLikeToFireStore(data.getLikes(),data.getPostId(),data.getOwnerUID());
                }
            });
            myViewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentFragment commentFragment = new CommentFragment(data.getCommentMap(),data.getPostId(),data.getOwnerUID());
                    commentFragment.show(fragmentManager,commentFragment.getTag());
                }
            });

            myViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContextThemeWrapper themeWrapper = new ContextThemeWrapper(myViewHolder.imageView.getContext(),R.style.CustomAlertTheme);
                    AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
                    builder.setMessage("कृपया पुष्टि करें कि आप इस पोस्ट को हटाना चाहते हैं, डिलीट होने पर आपको यह पोस्ट नहीं मिलेगी");
                    builder.setPositiveButton("निररस्त करें",null);
                    builder.setNegativeButton("डीलीट करें", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Delete the Post
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection("posts").document(data.getPostId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showToast(myViewHolder.imageView.getContext(),"डिलीट सफल रहा");
                                            dataList.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast(myViewHolder.imageView.getContext(),"डिलीट असफल रहा");
                                }
                            });
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    private void updateLikeCount(List<String> likes,MyViewHolder myViewHolder) {
        if(likes.size() == 0) {
            myViewHolder.likeCountTttv.setText("Be the first to like this post ❤");
        } else {
            myViewHolder.likeCountTttv.setText(likes.size()+" people like this post ❤");
        }
    }

    private void updateLikeBtn(List<String> list,MyViewHolder viewHolder,Button likeBtn) {
        if(list.contains(mAuth.getUid())) {
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                    viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.ic_heart_filled_24),null,null);
        } else {likeBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.ic_heart_24),null,null);
        }
    }
    private void updateLikeToFireStore(List<String> list,String postId,String ownerId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("posts").document(postId).update("likes",list);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Button deleteBtn,likeBtn,commentBtn;
        private TextView likeCountTttv;
        public MyViewHolder(View itemView) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.view_post_list_delete);
            likeBtn = itemView.findViewById(R.id.view_post_list_like);
            commentBtn = itemView.findViewById(R.id.view_post_list_comment);
            imageView = itemView.findViewById(R.id.view_post_list_image);
            likeCountTttv = itemView.findViewById(R.id.view_post_list_like_count);
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
}
