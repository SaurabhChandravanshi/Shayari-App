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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MenuFragment extends Fragment {
    private CardView logoutCard;
    private FirebaseAuth mAuth;
    private FrameLayout loginFrame,mainFrame;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allInitializations(view);
        updateUI(); // This method check if user Signed in or not and update UI accordingly

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextThemeWrapper themeWrapper = new ContextThemeWrapper(getActivity(),R.style.CustomAlertTheme);
                AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);
                builder.setMessage("Do you really wants to Logout?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(getActivity(),MainActivity.class));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("CANCEL",null);
                builder.create().show();
            }
        });
    }

    // Update UI Frame according to User login status
    private void updateUI() {
        if(mAuth.getCurrentUser() != null) {
            // User signed in
            mainFrame.setVisibility(View.VISIBLE);
            loginFrame.setVisibility(View.GONE);
        }
        else {
            // User not signed in
            mainFrame.setVisibility(View.GONE);
            loginFrame.setVisibility(View.VISIBLE);
        }
    }

    private void allInitializations(View view) {
        logoutCard = view.findViewById(R.id.menu_logout);
        mAuth = FirebaseAuth.getInstance();
        loginFrame = view.findViewById(R.id.menu_login_require_frame);
        mainFrame = view.findViewById(R.id.menu_main_frame);
    }
}
