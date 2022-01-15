package com.example.currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile extends AppCompatActivity {

    TextView name, email, phone, currency;
    FirebaseAuth fdauth;
    FirebaseFirestore fdstore;
    String gname, gphone, gemail, gcurrency;
    String userID;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.pname);
        email = findViewById(R.id.pemail);
        phone = findViewById(R.id.pphone);
        currency = findViewById(R.id.pcurrency);

        fdauth = FirebaseAuth.getInstance();
        fdstore = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(profile.this,login.class);
            startActivity(i);
            finish();
        }
        else {
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
        }
    }
}