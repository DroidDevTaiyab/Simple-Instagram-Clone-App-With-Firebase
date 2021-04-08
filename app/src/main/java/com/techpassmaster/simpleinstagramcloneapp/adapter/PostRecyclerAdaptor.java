package com.techpassmaster.simpleinstagramcloneapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.techpassmaster.simpleinstagramcloneapp.R;
import com.techpassmaster.simpleinstagramcloneapp.activity.EditProfile_Activity;
import com.techpassmaster.simpleinstagramcloneapp.model.Post;
import com.techpassmaster.simpleinstagramcloneapp.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nb.scode.lib.ExpandableTextView;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class PostRecyclerAdaptor extends RecyclerView.Adapter<PostRecyclerAdaptor.PostMyHolder> {

    private final Context mContext;
    private final List<Post> postList;
    FirebaseUser firebaseUser;
    String key;

    public PostRecyclerAdaptor(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostMyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View myview = LayoutInflater.from(mContext)
                .inflate(R.layout.postview_item, viewGroup, false);

        return new PostRecyclerAdaptor.PostMyHolder(myview);

    }

    @Override
    public void onBindViewHolder(@NonNull final PostMyHolder postMyHolder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Posts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final Post post = postList.get(position);

                if (postMyHolder.getAdapterPosition() != -1) {

                    key = postList.get(postMyHolder.getAdapterPosition()).getKey();
                }
                Glide.with(mContext).load(post.getPostImageUrl()).into(postMyHolder.postImage);

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(post.getPostTimestamp());
                postMyHolder.timestamp.setText(DateUtils.getRelativeTimeSpanString(post.getPostTimestamp()));
                postMyHolder.timestamp.setText(format.format(calendar.getTime()));

                postMyHolder.postdescription.setText(post.getDescription());


                postMyHolder.postdescription.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
                    @Override
                    public void onStateChange(boolean isShrink) {
                        Post post = postList.get(position);
                        post.setShrink(isShrink);
                        postList.set(position, post);
                    }
                });

                postMyHolder.postDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            final DatabaseReference databaseReference_delete = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Posts");
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(post.getPostImageUrl());

                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    databaseReference_delete.child(key).removeValue();
                                    mContext.startActivity(new Intent(mContext, EditProfile_Activity.class));
                                    Toast.makeText(mContext, "Post Deleted", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                postMyHolder.postdescription.resetState(postList.get(position).isShrink());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference_user = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        databaseReference_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getProfileImageUrl()).into(postMyHolder.user_profile);
                postMyHolder.postusername.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class PostMyHolder extends RecyclerView.ViewHolder {

        CircleImageView user_profile;
        ImageView postImage, postDelete;
        TextView postusername, timestamp;
        ExpandableTextView postdescription;

        public PostMyHolder(View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.userprofile);
            postusername = itemView.findViewById(R.id.txt_postusername);
            postImage = itemView.findViewById(R.id.postImage);
            postDelete = itemView.findViewById(R.id.post_delete);
            postdescription = itemView.findViewById(R.id.txt_postdescription);
            timestamp = itemView.findViewById(R.id.txt_timestamp);
        }
    }

}