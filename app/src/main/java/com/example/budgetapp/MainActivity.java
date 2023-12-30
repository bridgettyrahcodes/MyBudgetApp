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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView signup_reg;
    Button btn_login;

    private TextView mForgotPassword;
    private EditText mEmail;
    private EditText mPass;
    private Button  btnLogin;

    private TextView mSignup;
    private ProgressDialog mDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup_reg=findViewById(R.id.signup_reg);
        btn_login=findViewById(R.id.btn_login);
        mForgotPassword=findViewById(R.id.forgot_password);
        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);

        signup_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
                finish();
            }
        });

        //reset
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetActivity2.class));

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDetails();
            }
        });

    }

    private void  loginDetails(){
        mEmail=findViewById(R.id.email_login);
        mPass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        mForgotPassword=findViewById(R.id.forgot_password);
        mSignup=findViewById(R.id.signup_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email Required");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Password Required");
                    return;
                }else {

                    mDialog.setMessage("Processing");
                    mDialog.show();

                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                finish();
                            }else{
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        //registration
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        //reset
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetActivity2.class));

            }
        });

    }

}