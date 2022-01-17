package com.example.currency;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class profile extends AppCompatActivity {

    TextView name, email, phone, currency;
    FirebaseAuth fdauth;
    FirebaseFirestore fdstore;
    String gname, gphone, gemail, gcurrency;
    String userID;
    DocumentReference documentReference;
    Uri filepath;
    ImageView img;
    Button browse,upload;
    FirebaseStorage fst;
    StorageReference storage;
    Bitmap bitmap;
    Boolean exists = false, show = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.pname);
        email = findViewById(R.id.pemail);
        phone = findViewById(R.id.pphone);
        currency = findViewById(R.id.pcurrency);
        fst = FirebaseStorage.getInstance();
        storage = fst.getReference();


        fdauth = FirebaseAuth.getInstance();
        fdstore = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(profile.this, login.class);
            startActivity(i);
            finish();
        } else {
            userID = fdauth.getCurrentUser().getUid();
            documentReference = fdstore.collection("users").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {

                        gname = task.getResult().getString("Name");
                        gphone = task.getResult().getString("Phone");
                        gemail = task.getResult().getString("Email");
                        gcurrency = task.getResult().getString("Currency");

                        name.setText(gname);
                        phone.setText(gphone);
                        email.setText(gemail);
                        currency.setText(gcurrency);

                    } else
                        Toast.makeText(profile.this, "Profile Doesn't Exists", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(profile.this, "Server Error!!", Toast.LENGTH_SHORT).show();
                }
            });


            img = (ImageView) findViewById(R.id.img);
            upload = (Button) findViewById(R.id.upload);
            browse = (Button) findViewById(R.id.browse);

            browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!exists){
                        chosepic();
                    }else
                        Toast.makeText(getApplicationContext(), "You have Image Uploaded",Toast.LENGTH_SHORT).show();
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!show){
                        uploadtofirebase();
                    }else
                        Toast.makeText(getApplicationContext(), "Select An Image First",Toast.LENGTH_SHORT).show();

                }
            });

            try {
                StorageReference ds = storage.child("image/" + userID);
                File l = File.createTempFile(userID,"*");
                ds.getFile(l).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        bitmap = BitmapFactory.decodeFile(l.getAbsolutePath());
                        img.setImageBitmap(bitmap);
                        exists = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed!!  " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadtofirebase() {

        StorageReference d = storage.child("image/" + userID);
        d.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "File Uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed!!  " + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void chosepic(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(i);
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        filepath = data.getData();
                        img.setImageURI(filepath);
                        show = true;
                    }
                }
            });
}