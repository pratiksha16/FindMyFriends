package com.example.findmyfriends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button signUp;
    FirebaseAuth mfirebaseAuth;
    TextView register;

    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirebaseAuth= FirebaseAuth.getInstance();
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        signUp= findViewById(R.id.signUp);
        register=findViewById(R.id.register);
        signUp.setOnClickListener(new View.OnClickListener(){

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
                    Toast.makeText(MainActivity.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();

                }
                else if(!emailId.isEmpty() && !pw.isEmpty()){
                    mfirebaseAuth.createUserWithEmailAndPassword(emailId,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"SignUp UnsuccessFul.Please Try Again",Toast.LENGTH_SHORT);
                            }
                            else{
                                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);

            }
        });



       signInButton= findViewById(R.id.sign_in_button);
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("sign-in", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void  updateUI(GoogleSignInAccount account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MapsActivity.class));
        }else {
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }
}
