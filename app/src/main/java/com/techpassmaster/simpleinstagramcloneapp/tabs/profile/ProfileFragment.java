package com.techpassmaster.simpleinstagramcloneapp.tabs.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.techpassmaster.simpleinstagramcloneapp.activity.EditProfile_Activity;
import com.techpassmaster.simpleinstagramcloneapp.adapter.ProfilePostRecyclerAdaptor;
import com.techpassmaster.simpleinstagramcloneapp.auth.LoginActivity;
import com.techpassmaster.simpleinstagramcloneapp.model.Post;
import com.techpassmaster.simpleinstagramcloneapp.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class ProfileFragment extends Fragment {

    private ImageView profileImage, logout;
    private TextView profile_username, profile_fullname;
    private Button profilEedit_btn;
    private List<Post> postsProfileList;
    private ProfilePostRecyclerAdaptor postPrifileAdaptor;
    private RecyclerView recycler_profile;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    private final FirebaseDatabase firebaseDatabase_post = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        profileImage = root.findViewById(R.id.profile_image);
        logout = root.findViewById(R.id.logout_img);
        profile_username = root.findViewById(R.id.profile_username);
        profile_fullname = root.findViewById(R.id.profile_fullname);
        profilEedit_btn = root.findViewById(R.id.profileedit_btn);

        recycler_profile = root.findViewById(R.id.recycler_profile);
        recycler_profile.setHasFixedSize(true);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        recycler_profile.setLayoutManager(linearLayoutManager);
        postsProfileList = new ArrayList<>();
        postPrifileAdaptor = new ProfilePostRecyclerAdaptor(getContext(), postsProfileList);
        recycler_profile.setAdapter(postPrifileAdaptor);

        profilEedit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), EditProfile_Activity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Toast.makeText(getContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        read_UserProfile();
        read_UserProfilePost();
        return root;
    }

    private void read_UserProfile() {
        String userid = firebaseUser.getUid();

        DatabaseReference databaseReference_user = FirebaseDatabase.getInstance().
                getReference("Users").child(userid);

        databaseReference_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if (isAdded()) {
                    if (user != null) {
                        Glide.with(getContext()).load(user.getProfileImageUrl()).into(profileImage);
                        profile_username.setText(user.getUserName());
                        profile_fullname.setText(user.getFullName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void read_UserProfilePost() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();
        DatabaseReference databaseReference_post = firebaseDatabase_post.getReference("Users").child(userid).child("Posts");

        databaseReference_post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postsProfileList.clear();

                for (DataSnapshot itemSnapshort : dataSnapshot.getChildren()) {

                    Post post = itemSnapshort.getValue(Post.class);
                    postsProfileList.add(post);
                }

                postPrifileAdaptor.notifyDataSetChanged();
                Collections.reverse(postsProfileList);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}