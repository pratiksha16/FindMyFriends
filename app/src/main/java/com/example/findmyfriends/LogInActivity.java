package com.example.findmyfriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    EditText email, password;
    Button logIn;
    FirebaseAuth mfirebaseAuth;
    TextView tvLog;
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mfirebaseAuth= FirebaseAuth.getInstance();
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        logIn= findViewById(R.id.LogIn);
        tvLog=findViewById(R.id.textView);
        mfirebaseAuth= FirebaseAuth.getInstance();

        mAuthStateListner= new FirebaseAuth.AuthStateListener() {



            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser= mfirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    Toast.makeText(LogInActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent( LogInActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LogInActivity.this, "Please log In", Toast.LENGTH_SHORT).show();
                }

            }
        };
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId= email.getText().toString();
                String pw= password.getText().toString();
                if(emailId.isEmpty()){
                    email.setError("Please,enter the email id.");
                    email.requestFocus();
                }
                else if(pw.isEmpty()){
                    password.setError("Please, input the password");
                    password.requestFocus();
                }
                else if(emailId.isEmpty() && pw.isEmpty()){
                    Toast.makeText(LogInActivity.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();

                }
                else if(!emailId.isEmpty() && !pw.isEmpty()){
                    mfirebaseAuth.signInWithEmailAndPassword(emailId, pw).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if(!task.isSuccessful()) {
                               Toast.makeText(LogInActivity.this, "Log In Error!", Toast.LENGTH_SHORT).show();
                           }
                           else{
                               FirebaseUser user= mfirebaseAuth.getCurrentUser();
                               Intent intent= new Intent(LogInActivity.this,HomeActivity.class);
                               startActivity(intent);
                               finish();
                           }
                        }
                    });




                }
                else{
                    Toast.makeText(LogInActivity.this, "Error Occured", Toast.LENGTH_SHORT);
                }

            }
        });

        tvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(LogInActivity.this, MainActivity.class);
                startActivity(in);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mfirebaseAuth.addAuthStateListener(mAuthStateListner);
    }
}