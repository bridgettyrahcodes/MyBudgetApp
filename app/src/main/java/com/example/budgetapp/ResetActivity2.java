package com.example.budgetapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity2 extends AppCompatActivity {
    EditText email;
    Button reset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset2);

        email=findViewById(R.id.email);
        reset=findViewById(R.id.reset);
        mAuth=FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString().trim();

                if (Email.isEmpty()){
                    email.setError("Email is empty...");
                }else {
                    mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetActivity2.this, "Check your email to change your password", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ResetActivity2.this, "Error try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ResetActivity2.this, "Error failed to send a link", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}