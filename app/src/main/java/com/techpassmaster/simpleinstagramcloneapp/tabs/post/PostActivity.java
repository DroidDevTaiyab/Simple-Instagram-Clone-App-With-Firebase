package com.techpassmaster.simpleinstagramcloneapp.tabs.post;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techpassmaster.simpleinstagramcloneapp.R;
import com.techpassmaster.simpleinstagramcloneapp.activity.MainActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class PostActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private Uri imageUri;
    private String imageUrl;
    private ImageView added_uploadImg, close_img;
    private TextView post;
    private EditText description;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        CropImage.activity().start(PostActivity.this);

        added_uploadImg = findViewById(R.id.added_uploadImg);
        close_img = findViewById(R.id.close_img);
        post = findViewById(R.id.txt_post);
        description = findViewById(R.id.edttxt_description);

        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("Post");

        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PostActivity.this, MainActivity.class));
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
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

    private void uploadPost() {
        final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
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

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    String currentTime = DateFormat.getDateTimeInstance().
                            format(Calendar.getInstance().getTime());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(userid).child("Posts").child(currentTime);
                    String postId = databaseReference.push().getKey();

                    long postdate = Calendar.getInstance().getTimeInMillis();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("postId", postId);
                    hashMap.put("postImageUrl", imageUrl);
                    hashMap.put("description", description.getText().toString());
                    hashMap.put("currentUserId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("postTimestamp", postdate);

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } else {

            Toast.makeText(PostActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            added_uploadImg.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Something Wrong?", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }
}