package com.techpassmaster.simpleinstagramcloneapp.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techpassmaster.simpleinstagramcloneapp.R;
import com.techpassmaster.simpleinstagramcloneapp.model.User;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class EditProfile_Activity extends AppCompatActivity {

    private StorageReference storageReference;
    private Uri imageUri;
    private String imageUrl;

    private CircleImageView change_profileImage;
    private ImageView saveProfile_img, close_editProfile;
    private TextView chanage_profiletxt, edit_username, edit_fullname;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        change_profileImage = findViewById(R.id.change_profileImage);
        close_editProfile = findViewById(R.id.close_editProfile);
        saveProfile_img = findViewById(R.id.saveProfile_img);
        chanage_profiletxt = findViewById(R.id.change_profiletxt);
        edit_username = findViewById(R.id.edit_username);
        edit_fullname = findViewById(R.id.edit_fullname);


        close_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("UserProfile");

        DatabaseReference databaseReference_user = FirebaseDatabase.getInstance()
                .getReference("Users").child(firebaseUser.getUid());

        databaseReference_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if (user != null) {
                    Glide.with(EditProfile_Activity.this).load(user.getProfileImageUrl()).into(change_profileImage);
                    edit_username.setText(user.getUserName());
                    edit_fullname.setText(user.getFullName());
                } else {
                    Toast.makeText(EditProfile_Activity.this, "Fill all blank", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chanage_profiletxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(EditProfile_Activity.this);

            }
        });
        saveProfile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update_userProfile(edit_fullname.getText().toString(), edit_username.getText().toString());
                startActivity(new Intent(EditProfile_Activity.this, MainActivity.class));
                Toast.makeText(EditProfile_Activity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
                uploadImage();

            }

            private void update_userProfile(String fullname, String username) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("fullname", fullname);
//                hashMap.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/flytant-assignment.appspot.com/o/user.png?alt=media&token=bb9071b2-81e8-45a0-8945-fe991911f99d");

                databaseReference.updateChildren(hashMap);
            }
        });
    }

    private String getfileExtention(Uri uri) {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfile_Activity.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        if (imageUri != null) {
            StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getfileExtention(imageUri));

            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;

                    Uri urlimge = uriTask.getResult();
                    imageUrl = urlimge.toString();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                            .getReference().child("Users").child(firebaseUser.getUid());

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("profileImageUrl", imageUrl);

                    databaseReference.updateChildren(hashMap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } else {
//            Toast.makeText(EditProfile_Activity.this, "Please Select Image", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            change_profileImage.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Something Wrong?", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(EditProfile_Activity.this, MainActivity.class));
            finish();
        }
    }
}
