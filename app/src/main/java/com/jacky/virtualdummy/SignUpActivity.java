package com.jacky.virtualdummy;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.name)
    TextView mname;
    @BindView(R.id.email) TextView memail;
    @BindView(R.id.password) TextView mpassword;
    @BindView(R.id.confirmPassword) TextView mconfirmPassword;
    @BindView(R.id.signup)
    Button msignup;
    @BindView(R.id.login) TextView mlogin;
    @BindView(R.id.progressBar)
    ProgressBar mprogressBar;
    private FirebaseAuth firebaseAuth;
    private TextView mText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);
        msignup.setOnClickListener(this);
        mlogin.setOnClickListener(this);
        mprogressBar.setVisibility(View.INVISIBLE);

        mText = findViewById(R.id.text);
        Typeface pacific = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        mText.setTypeface(pacific);
    }
    public boolean validation(){
        boolean valid = false;

        if(mname.getText().toString().isEmpty()){
            mname.setError("Invalid name");
        }
        else if(memail.getText().toString().isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(memail.getText().toString()).matches())){
            memail.setError("Invalid email address");
        }
        else if(mpassword.getText().toString().isEmpty() || mpassword.getText().toString().length() < 8 ){   //  !(isValidPassword(mpassword.getText().toString().trim()))  ){
            mpassword.setError("Email should be at least 8 characters");
        }
        else if(!mconfirmPassword.getText().toString().equals(mpassword.getText().toString())){
            mconfirmPassword.setError("Password does not match");
        }
        else
        {
            valid = true;

        }
        return valid;
    }

//    public static boolean isValidPassword(final String password) { //password matcher
//
//        Pattern pattern;
//        Matcher matcher;
//        final String PASSWORD_PATTERN = "/^(?=.*\\d)(?=.*[A-Z])([@$%&#])[0-9a-zA-Z]{4,}$/";
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher = pattern.matcher(password);
//
//        return matcher.matches();
//
//    }

    private void registerUser(){
        mprogressBar.setVisibility(View.VISIBLE);
        String email = memail.getText().toString().trim();
        String password = mpassword.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{

                    Toast.makeText(SignUpActivity.this, "Could not register.. please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == msignup){
            if (validation()) {
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                registerUser();
            }

        }
        if (v == mlogin){
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

}
