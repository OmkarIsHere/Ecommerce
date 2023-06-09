package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String url = "https://inundated-lenders.000webhostapp.com/api/login.php";
    private TextView signup;
    TextInputEditText  edtEmail, edtPassword;
    Button btnLogin;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup =findViewById(R.id.txtSignUp);
        edtEmail= (TextInputEditText)findViewById(R.id.edtEmail);
        edtPassword= (TextInputEditText)findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        relativeLayout =(RelativeLayout)findViewById(R.id.relativeLayout);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);


        signup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));

        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String tempEmail = Objects.requireNonNull(edtEmail.getText()).toString().trim();
                    String tempPass = Objects.requireNonNull(edtPassword.getText()).toString().trim();

                    if(!tempEmail.equals("") || !tempPass.equals("")){
                        checkLogin(tempEmail, tempPass);
                    }else{
                        Toast.makeText(getApplicationContext(),"Both the fields are mandatory", Toast.LENGTH_LONG).show();
                    }
            }
        });

    }
    private void checkLogin(String email, String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String status = null;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getString("status");

                            if (status.equals("true")){
                                SharedPreferences loggedIn = getSharedPreferences("email", MODE_PRIVATE);
                                SharedPreferences.Editor prefEditor = loggedIn.edit();
                                prefEditor.putString("email",email);
                                prefEditor.apply();

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }
                            else{
                                relativeLayout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Wrong Email or Password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "LogE " + error.getMessage());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("checklogin", "checklogin");
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        relativeLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "queued success: ");
    }
}