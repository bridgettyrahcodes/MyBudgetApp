package com.example.budgetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText mName;
    private EditText mEmail;
    private EditText mPass;
    private Button btnReg;
    TextView signin;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        mDialog = new ProgressDialog(this);
        mName=findViewById(R.id.name_reg);
        mEmail = findViewById(R.id.email_reg);
        mPass = findViewById(R.id.password_reg);
        btnReg = findViewById(R.id.btn_reg);
        signin=findViewById(R.id.signin);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, MainActivity.class));
                finish();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name=mName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String pass = mPass.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            mName.setError("Name Required");
            return;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email Required ");
            return;
        }
          else if (TextUtils.isEmpty(pass)) {
        mPass.setError("Password Required ");
        return;
        } else {


            mDialog.setMessage("Registering, please wait...");
            mDialog.show();

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Handle successful registration
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String id = firebaseUser.getUid();
                                    UserHelperClass user = new UserHelperClass(id, email, pass);
                                    databaseReference.child(id).setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mDialog.dismiss();
                                                        Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        mDialog.dismiss();
                                                        Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(Register.this, "Error, try again", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                            } else {
                                // If registration fails, display a message to the user.
                                mDialog.dismiss();
                                Toast.makeText(Register.this, "User id null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            // Handle failure
                            Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

        }


    }
}