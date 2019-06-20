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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.username)
    EditText memail;



    @BindView(R.id.password)
    EditText mpassword;
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
//
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


//    private void loginUser() {
//
//
//        //convert object to json
//        Gson gson = new GsonBuilder().create();
//
//        Map<String, String> user = new HashMap<>();
//        user.put("username", username.getText().toString().trim());
//        user.put("password", password.getText().toString().trim());
//
////        User user = new User(username.getText().toString().trim(), password.getText().toString().trim());
//
//
//        String json = gson.toJson(user);
//
//        OkHttpClient client = new OkHttpClient.Builder().build();
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.BASE_URL).newBuilder();
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
//                LoginActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
//                        LoginActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    String json = response.body().string();
//                                    JSONObject jsonObject = new JSONObject(json);
//                                    // Constants.Token = "Bearer "+jsonObject.getString("access");
//
//
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//
//                                String json = response.body().string();
//                                JSONObject jsonObject = new JSONObject(json);
//
//                                Toast.makeText(LoginActivity.this, jsonObject.getString("detail"), Toast.LENGTH_SHORT).show();
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
//        if (v == mlogin) {
//            {
//                loginUser();
//
//            }
//
//            if (v == msignup) {
//                Intent intent =  new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//        }
//
//
//        }
//    }
}

