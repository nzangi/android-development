package com.example.learningandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {
    private EditText passwordEmail;
    private Button resetButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passwordEmail = (EditText)findViewById(R.id.resetPassword);
        resetButton = (Button)findViewById(R.id.btnPasswordReset);
        firebaseAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = passwordEmail.getText().toString().trim();
                if (userEmail.equals("")){
                    Toast.makeText(PasswordActivity.this,"Please Enter your registered Email ID",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            {
                                if(task.isSuccessful()){
                                    Toast.makeText(PasswordActivity.this,"Password reset  Email sent ", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                                }
                                else{
                                    Toast.makeText(PasswordActivity.this,"Error in sending Password Email reset ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}