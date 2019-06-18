package com.jacky.virtualdummy;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.email)
    EditText memail;
    @BindView(R.id.password) EditText mpassword;
    @BindView(R.id.login)
    Button mlogin;
    @BindView(R.id.signup)
    TextView msignup;

    private FirebaseAuth firebaseAuth;
    private TextView mText;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mlogin.setOnClickListener(this);
        msignup.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        mText = findViewById(R.id.text);
        Typeface pacific = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        mText.setTypeface(pacific);










    }

    public boolean validation() {
        boolean valid = false;

        if (memail.getText().toString().isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(memail.getText().toString()).matches())) {
            memail.setError("Invalid email address");
        } else if (mpassword.getText().toString().isEmpty() || mpassword.getText().toString().length() < 8) {   //  !(isValidPassword(mpassword.getText().toString().trim()))  ){
            mpassword.setError("Use at least one uppercase letter, lowercase letter, number, special characters and");
        } else {
            valid = true;

        }
        return valid;
    }

    @Override
    public void onClick(View v) {

        if (v == mlogin) {
            if (validation()) {
                loginUser();

            }
        }
        if (v == msignup) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }


    }

    private void loginUser() {

        String email = memail.getText().toString().trim();
        String password = mpassword.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String emails = memail.getText().toString();
                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("emails", emails);
                startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

