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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class PublicFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;
    private FrameLayout loginRequiredFrame;
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

    private void allInitializations() {
        mAuth = FirebaseAuth.getInstance();
        fab = getActivity().findViewById(R.id.public_fab);
        loginRequiredFrame = getActivity().findViewById(R.id.public_login_require_frame);
    }
}
