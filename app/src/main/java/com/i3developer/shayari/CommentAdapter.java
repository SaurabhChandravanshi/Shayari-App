package com.i3developer.shayari;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> dataList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public CommentAdapter(List<Object> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder)holder;
        Comment data = (Comment) dataList.get(position);
        myViewHolder.commentBox.setText(data.getCommentText());
        if(mAuth.getUid().equals(data.getUid())) {
            myViewHolder.cardView.setCardBackgroundColor(myViewHolder.itemView.getResources().getColor(R.color.colorLightGray));
            myViewHolder.nameTtv.setText("You");
        } else {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            db.getReference("Users").child(data.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    myViewHolder.nameTtv.setText(user.getName());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        Button commentBox;
        private CardView cardView;
        private TextView nameTtv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            commentBox = itemView.findViewById(R.id.comment_list_content);
            cardView = itemView.findViewById(R.id.comment_list_card);
            nameTtv = itemView.findViewById(R.id.comment_list_name);
        }
    }
}
