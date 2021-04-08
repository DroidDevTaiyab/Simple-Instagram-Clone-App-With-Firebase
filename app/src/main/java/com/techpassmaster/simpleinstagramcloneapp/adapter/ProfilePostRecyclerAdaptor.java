package com.techpassmaster.simpleinstagramcloneapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techpassmaster.simpleinstagramcloneapp.R;
import com.techpassmaster.simpleinstagramcloneapp.model.Post;

import java.util.List;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class ProfilePostRecyclerAdaptor extends RecyclerView.Adapter<ProfilePostRecyclerAdaptor.ProfilePostMyHolder> {

    private final Context mContext;
    private final List<Post> profilepostList;
    FirebaseUser firebaseUser;

    public ProfilePostRecyclerAdaptor(Context mContext, List<Post> profilepostList) {
        this.mContext = mContext;
        this.profilepostList = profilepostList;
    }

    @NonNull
    @Override
    public ProfilePostMyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View myview = LayoutInflater.from(mContext)
                .inflate(R.layout.profile_post_item, viewGroup, false);


        return new ProfilePostRecyclerAdaptor.ProfilePostMyHolder(myview);

    }

    @Override
    public void onBindViewHolder(@NonNull final ProfilePostMyHolder postMyHolder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Post post = profilepostList.get(position);
                Glide.with(mContext).load(post.getPostImageUrl()).into(postMyHolder.profile_postImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return profilepostList.size();
    }

    public static class ProfilePostMyHolder extends RecyclerView.ViewHolder {

        ImageView profile_postImage;

        public ProfilePostMyHolder(View itemView) {
            super(itemView);

            profile_postImage = itemView.findViewById(R.id.profile_postImage);
        }
    }

}