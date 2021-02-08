package com.i3developer.shayari;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.util.Util;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PublicFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;
    private FrameLayout loginRequiredFrame;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Object> publicPosts = new ArrayList<>();
    private FirebaseFirestore firestoreRef;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_public,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        allInitializations();
        assert args != null;
        boolean onlySinglePost = args.getBoolean("single_post");

        if(mAuth.getCurrentUser() == null) {
            loginRequiredFrame.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            if (onlySinglePost) {
                getSinglePostFromFirestore(args.getString("post_id"));
            } else {
                getPublicPostFromFirestore();
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getActivity(),SignupActivity.class));
                }
                else {
                    startActivity(new Intent(getActivity(),CreatePostActivity.class));
                }
            }
        });
    }

    private void getSinglePostFromFirestore(String documentId) {
        Task<DocumentSnapshot> task = firestoreRef.collection("posts").document(documentId).get();
        task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                publicPosts.add(documentSnapshot.toObject(PublicPost.class));
                Collections.shuffle(publicPosts);
                addBannersToRecycler();
                loadRecyclerView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                showToast(getActivity(), "Post Not Found.");
            }
        });
    }

    private void getPublicPostFromFirestore() {
        firestoreRef.collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                for (DocumentSnapshot snapshot:queryDocumentSnapshots) {
                    publicPosts.add(snapshot.toObject(PublicPost.class));
                }
                Collections.shuffle(publicPosts);
                addBannersToRecycler();
                loadRecyclerView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                showToast(getActivity(), "Failed to get data.");
            }
        });
    }
    private void showToast(Context context, String text) {
        androidx.appcompat.view.ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void loadRecyclerView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void allInitializations() {
        mAuth = FirebaseAuth.getInstance();
        fab = getActivity().findViewById(R.id.public_fab);
        loginRequiredFrame = getActivity().findViewById(R.id.public_login_require_frame);
        recyclerView = getActivity().findViewById(R.id.public_recycler);
        firestoreRef = FirebaseFirestore.getInstance();
        adapter = new PublicPostAdapter(publicPosts,getFragmentManager());
        layoutManager = new LinearLayoutManager(getActivity());
        progressBar = getActivity().findViewById(R.id.public_pBar);
    }
    private void addBannersToRecycler() {
        for(int i=3;i<publicPosts.size();i=i+3) {
            AdView adView = new AdView(getActivity());
            publicPosts.add(i,adView);
        }
    }
}
