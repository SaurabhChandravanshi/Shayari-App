package com.i3developer.shayari;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReferralListFragment extends BottomSheetDialogFragment {
    private TextView amountTtv;
    private TextView referralListItem;
    private Button paymentRequest;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.referral_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getReferralListData();
    }

    private void getReferralListData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("referralList").document(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getData() == null) {
                            List<String> list = new ArrayList<>();
                            list.add(0,"No referral found Please share refer link to your friend and tell them to install the app via link");
                            ReferralListData data = new ReferralListData("No referrals found",list,0,2);
                            updateUI(data);
                        } else {
                            ReferralListData data = documentSnapshot.toObject(ReferralListData.class);
                            updateUI(data);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to get data Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(ReferralListData data) {
        amountTtv.setText("Your earned ₹"+data.getEarnedMoney()+" so far");

        if(data.getEarnedMoney() >= 50) {
            paymentRequest.setEnabled(true);
            paymentRequest.setText("Add payment method");
        } else {
            paymentRequest.setEnabled(false);
            double percent = data.getEarnedMoney()*100/50;
            paymentRequest.setText(percent+"% threshold reached");
        }
        for(String line:data.getReferrals()) {
            String htmlLI = "<li>"+line+"</li><br/>";
            referralListItem.append(HtmlCompat.fromHtml(htmlLI,HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
    }

    private void init(View view) {
        amountTtv = view.findViewById(R.id.referral_list_amount);
        referralListItem = view.findViewById(R.id.referral_list_item);
        paymentRequest = view.findViewById(R.id.referral_list_payment_request);
    }
}