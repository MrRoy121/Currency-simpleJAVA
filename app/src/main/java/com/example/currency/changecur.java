package com.example.currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class changecur extends AppCompatActivity {

    String[] cury = {"  ", "Taka", "Euro", "Dollar", "Rupee", "Yen", "Pound" };
    String currency ="cfy";
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    DocumentReference documentReference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecur);

        Spinner spnr = findViewById(R.id.spx);
        Button update = findViewById(R.id.update);
        TextView curr = findViewById(R.id.curr);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        ArrayAdapter<String> ad1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cury);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr.setAdapter(ad1);

        spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                if(position == 1){
                    currency = "Taka";
                } else if(position == 2){
                    currency = "Euro";
                }else if(position == 3){
                    currency = "Dollar";
                }else if(position == 4){
                    currency = "Rupee";
                }else if(position == 5){
                    currency = "Yen";
                }else if(position == 6){
                    currency = "Pound";
                }

            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(changecur.this,login.class);
            startActivity(i);
            finish();
        }
        else {
            userID = fauth.getCurrentUser().getUid();
            documentReference = fstore.collection("users").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        String tcurrency = task.getResult().getString("Currency");
                        curr.setText(tcurrency);

                    }
                }
            });

        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userID = fauth.getCurrentUser().getUid();
                documentReference = fstore.collection("users").document(userID);
                documentReference.update("Currency", currency);
                Toast.makeText(changecur.this, "added to database successfully", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                }
        });

    }
}