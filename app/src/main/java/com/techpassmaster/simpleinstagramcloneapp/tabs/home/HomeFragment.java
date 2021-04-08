package com.techpassmaster.simpleinstagramcloneapp.tabs.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techpassmaster.simpleinstagramcloneapp.R;
import com.techpassmaster.simpleinstagramcloneapp.adapter.PostRecyclerAdaptor;
import com.techpassmaster.simpleinstagramcloneapp.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostRecyclerAdaptor postRecyclerAdaptor;
    private List<Post> postList;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase_post = FirebaseDatabase.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();

        postRecyclerAdaptor = new PostRecyclerAdaptor(getContext(), postList);
        recyclerView.setAdapter(postRecyclerAdaptor);

        getPost();
        return root;
    }

    private void getPost() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();
        DatabaseReference databaseReference_post = firebaseDatabase_post.getReference("Users").child(userid).child("Posts");

        databaseReference_post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();

                for (DataSnapshot itemSnapshort : dataSnapshot.getChildren()) {

                    Post post = itemSnapshort.getValue(Post.class);
                    post.setKey(itemSnapshort.getKey());
                    postList.add(post);
                }

                postRecyclerAdaptor.notifyDataSetChanged();
                Collections.reverse(postList);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}