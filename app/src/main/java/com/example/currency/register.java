package com.example.currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText ename, eemail, ephone, epass;
    Spinner spinner;
    String name, phone, email, pass, currency = "xyz", userID;
    FirebaseAuth fauth;
    ProgressBar pbar;
    String[] cury = {"  ", "Taka", "Euro", "Dollar", "Rupee", "Yen", "Pound" };
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(register.this, login.class);
                startActivity(i);
            }
        });


        eemail = findViewById(R.id.email);
        ename = findViewById(R.id.name);
        ephone = findViewById(R.id.phone);
        epass = findViewById(R.id.password);
        spinner = findViewById(R.id.sp1);
        pbar = findViewById(R.id.pbar);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        Button register = findViewById(R.id.register);

        ArrayAdapter<String> ad1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cury);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = ename.getText().toString().trim();
                email = eemail.getText().toString().trim();
                phone = ephone.getText().toString().trim();
                pass = epass.getText().toString().trim();

                if(name.equals("") && email.equals("") && phone.equals("") && pass.equals("")){
                    Toast.makeText(register.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }else{
                    pbar.setVisibility(View.VISIBLE);
                    fauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                userID = fauth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("Name", name);
                                user.put("Email", email);
                                user.put("Phone", phone);
                                user.put("Currency", currency);
                                user.put("Password", pass);
                                documentReference.set(user);

                                Intent i = new Intent(register.this,login.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(register.this, "Error !!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pbar.setVisibility(View.GONE);
                            }
                        }
                    });


                }
            }
        });

    }
}