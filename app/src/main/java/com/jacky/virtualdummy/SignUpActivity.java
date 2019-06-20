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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
//
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
//    private void loginUser() {
//
//
//        //convert object to json
//        Gson gson = new GsonBuilder().create();
//
//        Map<String, String> user = new HashMap<>();
//        user.put("username", username.getText().toString().trim());
//        user.put("email", email.getText().toString().trim());
//        user.put("password1", password.getText().toString().trim());
//        user.put("password2", mconfirmPassword.getText().toString().trim());
//
////        User user = new User(username.getText().toString().trim(), password.getText().toString().trim());
//
//
//        String json = gson.toJson(user);
//
//        OkHttpClient client = new OkHttpClient.Builder().build();
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.BASE_URL2).newBuilder();
//
//        String url = urlBuilder.build().toString();
//
//        MediaType MEDIA_TYPE = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(MEDIA_TYPE, json);
//
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                SignUpActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    if (response.code() == 200) {
//                        SignUpActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    String json = response.body().string();
//                                    JSONObject jsonObject = new JSONObject(json);
//                                    // Constants.Token = "Bearer "+jsonObject.getString("access");
//
//
//                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    SignUpActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//
//                                String json = response.body().string();
//                                JSONObject jsonObject = new JSONObject(json);
//
//                                Toast.makeText(SignUpActivity.this, jsonObject.getString("detail"), Toast.LENGTH_SHORT).show();
//
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == msignup) {
//            {
//                loginUser();
//
//            }
//
//
//        }
//    }

}
