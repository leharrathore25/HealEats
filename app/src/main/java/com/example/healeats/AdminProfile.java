package com.example.healeats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AdminProfile extends AppCompatActivity {
    private String username;
    private FirebaseFirestore db;
    private ImageView pfp;
    private Button upload_img, camera, apply_changes;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        pfp = findViewById(R.id.imageView2);
        upload_img = findViewById(R.id.button4);
        apply_changes = findViewById(R.id.button5);
        camera = findViewById(R.id.button6);
        db = FirebaseFirestore.getInstance();
        User user = UserDataSingleton.getInstance().getUser();

        if (user != null) {
            // Get the username
            username = user.getUsername();
        }

    }

    public void openFileChooser(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void openCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                // Gallery option
                imageUri = data.getData();
                pfp.setImageURI(imageUri);
            } else if (requestCode == CAMERA_REQUEST && data != null && data.getExtras() != null) {
                // Camera option
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                pfp.setImageBitmap(photo);
                // Convert the bitmap to Uri
                imageUri = getImageUri(photo);
            }
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child(username + "/profile_picture.jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveImageUrlToFirestore(imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(AdminProfile.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUrlToFirestore(String imageUrl) {
        Log.d("UserProfile.this","im in!");
        if (username != null) {
            db.collection("users")
                    .document(username)
                    .update("pfp", imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminProfile.this, "Profile picture added successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminProfile.this, "Failed to add profile picture", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.d("UserProfile.this","NUll");
            Toast.makeText(AdminProfile.this, "Null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onApplyChangesClick(View view) {
        uploadImageToFirebase();
    }
}