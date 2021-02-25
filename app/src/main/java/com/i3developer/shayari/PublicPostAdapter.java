package com.i3developer.shayari;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PublicPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> dataList;
    private static final int AD_VIEW = 0;
    private static final int RECYCLER_VIEW = 1;
    private FirebaseAuth mAuth;
    private FragmentManager fragmentManager;
    public PublicPostAdapter(List<Object> dataList, FragmentManager fragmentManager) {
        this.dataList = dataList;
        this.fragmentManager = fragmentManager;
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==AD_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_ad_post_list,parent,false);
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
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // display image of author (fetch image url from RT DB).
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(data.getOwnerUID());
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user != null && user.getPicUrl() != null) {
                        Glide.with(myViewHolder.itemView.getContext()).load(user.getPicUrl()).circleCrop().into(myViewHolder.authorImage);
                    }
                    myViewHolder.authorNameTtv.setText(HtmlCompat.fromHtml(user.getName()+"<br><small>A Shayari Book User</small>",HtmlCompat.FROM_HTML_MODE_COMPACT));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //Get Firebase Storage Reference to load Image
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReferenceFromUrl("gs://shayari-5b5f4.appspot.com/"+data.getImagePath());
            // Load image into ImageView
            GlideApp.with(myViewHolder.itemView.getContext()).load(reference).into(myViewHolder.imageView);
            updateLikeCount(data.getLikes(),myViewHolder);
            updateLikeBtn(data.getLikes(),myViewHolder,myViewHolder.likeBtn);

            myViewHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getLikes().contains(mAuth.getUid())) {
                        data.getLikes().remove(mAuth.getUid());
                    } else {
                        data.getLikes().add(mAuth.getUid());
                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Instantiate the RequestQueue.
                                RequestQueue queue = Volley.newRequestQueue(myViewHolder.itemView.getContext());
                                User user = snapshot.getValue(User.class);
                                String param = "title=New like"+"&message=New like received for your post"+"&token="+user.getFcmToken();
                                String url ="http://i3developer.in/SB/SendNotif.php?"+param;
                                // Request a string response from the provided URL.
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // Display the first 500 characters of the response string.
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                // Add the request to the RequestQueue.
                                queue.add(stringRequest);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    updateLikeBtn(data.getLikes(),myViewHolder,myViewHolder.likeBtn);
                    updateLikeCount(data.getLikes(),myViewHolder);
                    updateLikeToFireStore(data.getLikes(),data.getPostId(),data.getOwnerUID());
                }
            });
            myViewHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(myViewHolder.itemView.getContext());
                    Activity activity = (Activity)myViewHolder.itemView.getContext();
                    View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_share_post,null);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    Button shareBtn = dialogView.findViewById(R.id.share_post_share_btn);
                    Button closeBtn = dialogView.findViewById(R.id.share_post_close_btn);
                    Button shareLinkBtn = dialogView.findViewById(R.id.share_post_share_link_btn);
                    closeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    ImageView imageView = dialogView.findViewById(R.id.share_post_image);
                    GlideApp.with(myViewHolder.itemView.getContext()).load(reference).into(imageView);
                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveAndShareImage(myViewHolder.itemView.getContext(),viewToBitmap(imageView));
                        }
                    });
                    shareLinkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            shortenLongLinkAndShare(myViewHolder.itemView.getContext(),data.getDynamicLink());
                        }
                    });
                    alertDialog.show();
                }
            });
            myViewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentFragment commentFragment = new CommentFragment(data.getCommentMap(),data.getPostId(),data.getOwnerUID());
                    commentFragment.show(fragmentManager,commentFragment.getTag());
                }
            });
        }
    }

    public void shortenLongLinkAndShare(Context context,String longUrl) {
        // [START shorten_long_link]
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(longUrl))
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT,"Shayari Book App Post");
                            intent.putExtra(Intent.EXTRA_TEXT,"Like this Post on Shayari Book App\n"+shortLink);
                            context.startActivity(Intent.createChooser(intent,"Share via"));
                        } else {
                            // Error
                            // ...
                            showToast(context,"Failed to Get Link, No Internet Connection");
                        }
                    }
                });
        // [END shorten_long_link]
    }

    private void updateLikeCount(List<String> likes,MyViewHolder myViewHolder) {
        if(likes.size() == 0) {
            myViewHolder.likeCountTttv.setText("Be the first to like this post");
        } else {
            myViewHolder.likeCountTttv.setText(likes.size()+" people like this post");
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
        private ImageView imageView,authorImage;
        private Button shareBtn,likeBtn,commentBtn;
        private TextView likeCountTttv,authorNameTtv;
        public MyViewHolder(View itemView) {
            super(itemView);
            shareBtn = itemView.findViewById(R.id.public_post_list_share);
            likeBtn = itemView.findViewById(R.id.public_post_list_like);
            commentBtn = itemView.findViewById(R.id.public_post_list_comment);
            imageView = itemView.findViewById(R.id.public_post_list_image);
            likeCountTttv = itemView.findViewById(R.id.public_post_list_like_count);
            authorImage = itemView.findViewById(R.id.public_post_list_author_image);
            authorNameTtv = itemView.findViewById(R.id.public_post_list_author_name);
        }
    }
    class MyAdViewHolder extends RecyclerView.ViewHolder {

        private AdView adView;
        public MyAdViewHolder(@NonNull View itemView) {
            super(itemView);
            adView = itemView.findViewById(R.id.recycler_ad_post_list_adView);
        }
    }
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
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
