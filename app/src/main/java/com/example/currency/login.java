package com.example.currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText eemail, epass;
    FirebaseAuth feauth;
    ProgressBar pbar1;
    String email, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, register.class);
                startActivity(i);
            }
        });

        feauth = FirebaseAuth.getInstance();

        eemail = findViewById(R.id.email);
        epass = findViewById(R.id.password);
        pbar1 = findViewById(R.id.pbar1);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = eemail.getText().toString().trim();
                pass = epass.getText().toString().trim();
                if(email.equals("")  && pass.equals("")){
                    Toast.makeText(login.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }else{
                    pbar1.setVisibility(View.VISIBLE);
                    feauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(login.this,more.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(login.this, "Error !!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pbar1.setVisibility(View.GONE);
                            }
                        }
                    });


                }
            }
        });

    }
}