package com.example.learningandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private TextView Infor;
    private Button Login;
    private TextView userRegistration;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.name);
        Password = (EditText)findViewById(R.id.password);
        Infor = (TextView)findViewById(R.id.Infor);
        Login = (Button)findViewById(R.id.btnLogin);
        userRegistration = (TextView)findViewById(R.id.register);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);

        Infor.setText("Attempts Remaining: 5");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
//            Log.d("TAG", "onCreate: Logged in user is :"+user.getEmail());
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (MainActivity.this,RegisterActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (MainActivity.this,PasswordActivity.class));
            }
        });

    }
    private void validate(String Name,String Password) {
        progressDialog.setMessage("Login in Progress");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(Name,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
//                    Toast.makeText(MainActivity.this, "Login Successful",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent (MainActivity.this,SecondActivity.class));
                    checkEmailVerification();
                }
                else{
                    Toast.makeText(MainActivity.this, "Login Failed",Toast.LENGTH_SHORT).show();
                    counter --;
                    Infor.setText("Attempts Remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter == 0){
                        Login.setEnabled(false);
                    }
                }

            }
        });

    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag){
            finish();
            Toast.makeText(MainActivity.this, "Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        else{
            Toast.makeText(this,"Verify your Email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}