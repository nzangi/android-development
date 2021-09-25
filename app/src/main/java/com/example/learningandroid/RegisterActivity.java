package com.example.learningandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText userName,userPassword,userEmail, userAge;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilePicture;
    String email, name, age, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();
        firebaseAuth = FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
//                upload the data to the database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
//                                Toast.makeText(RegisterActivity.this,"Registration has sucessfull",Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                                    sendEmailVerification();
                                sendUserData();
                                Toast.makeText(RegisterActivity.this,"Successfully registered, upload Complete",Toast.LENGTH_SHORT).show();
//                                firebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));

                            }else{
                                Toast.makeText(RegisterActivity.this,"Registration has failed",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
    }
    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.username);
        userEmail = (EditText)findViewById(R.id.useremail);
        userPassword = (EditText)findViewById(R.id.userpassword);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvregistered);
        userAge  = (EditText)findViewById(R.id.etAge);
        userProfilePicture = (ImageView)findViewById(R.id.ivProfile);

    }
    private Boolean validate() {
        Boolean result = false;
        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        age = userAge.getText().toString();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty()){
            Toast.makeText(this, "Please Enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;

    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
         if(firebaseUser != null){
             firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegisterActivity.this,"Successfully registered, Verification mail has been sent",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Verification Mail Has not been sent",Toast.LENGTH_SHORT).show();
                    }
                 }
             });

        }
    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(age,email,name);
        myRef.setValue(userProfile);
    }
}