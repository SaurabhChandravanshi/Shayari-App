package com.i3developer.shayari;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        allInitializations();

        if(mAuth.getCurrentUser() == null) {
            loginRequiredFrame.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            getPublicPostFromFirestore();
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

    private void getPublicPostFromFirestore() {
        firestoreRef.collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                for (DocumentSnapshot snapshot:queryDocumentSnapshots) {
                    publicPosts.add(snapshot.toObject(PublicPost.class));
                }
                addBannersToRecycler();
                loadRecyclerView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        });
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
        adapter = new PublicPostAdapter(publicPosts);
        layoutManager = new LinearLayoutManager(getActivity());
        progressBar = getActivity().findViewById(R.id.public_pBar);
    }
    private void addBannersToRecycler() {
        for(int i=3;i<publicPosts.size();i=i+3) {
            AdView adView = new AdView(Objects.requireNonNull(getActivity()));
            publicPosts.add(i,adView);
        }
    }
}
