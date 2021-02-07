package com.i3developer.shayari;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommentFragment extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Map<String,List<String>> commentMap;
    private List<Object> commentList = new ArrayList<>();
    private EditText commentEdt;
    private ImageView sendIcon;
    private FirebaseFirestore firestore;
    private String postId;
    private FirebaseAuth mAuth;
    private Button closeBtn;
    private String ownerUID;

    public CommentFragment(Map<String,List<String>> commentMap,String postId,String ownerUID) {
        this.commentMap = commentMap;
        this.postId = postId;
        this.ownerUID = ownerUID;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allInitializations(view);
        populateCommentList();
        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(commentEdt.getText())) {
                    List<String> comments = new ArrayList<>();
                    Set set = commentMap.entrySet();
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry)iterator.next();
                        if(entry.getKey().equals(mAuth.getUid())) {
                            comments = (List<String>) entry.getValue();
                        }
                    }
                    comments.add(commentEdt.getText().toString());
                    commentMap.put(mAuth.getUid(),comments);
                    firestore.collection("posts").document(postId).update("commentMap."+mAuth.getUid(),comments);
                    commentList.add(new Comment(mAuth.getUid(),commentEdt.getText().toString()));
                    adapter.notifyDataSetChanged();
                    commentEdt.setText("");
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void populateCommentList() {
        Set set = commentMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            List<String> comments = (List<String>) entry.getValue();
            for (String comment:comments) {
                Comment data = new Comment(entry.getKey().toString(),comment);
                commentList.add(data);
            }
        }
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void allInitializations(View view) {
        recyclerView = view.findViewById(R.id.comment_recycler);
        adapter = new CommentAdapter(commentList);
        layoutManager = new LinearLayoutManager(getContext());
        commentEdt = view.findViewById(R.id.comment_editText);
        sendIcon = view.findViewById(R.id.comment_send);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        closeBtn = view.findViewById(R.id.comment_close);
    }
}
