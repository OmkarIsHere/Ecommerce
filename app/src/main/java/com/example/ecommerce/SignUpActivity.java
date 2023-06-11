package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
   ImageButton btnBack;
   Button btnSignup;
   RelativeLayout relativeLayout;
   ProgressBar progressBar;
   TextInputEditText edtName,edtPhoneNo, edtEmail, edtPassword;

   TextView txtErrorNo, txtErrorEmail, txtErrorPass, txtLogin;
    private static final String url = "https://ecommdot.000webhostapp.com/api/ecomm.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnBack = findViewById(R.id.btnBack);
        btnSignup = findViewById(R.id.btnSignUp);
        edtName = (TextInputEditText)findViewById(R.id.edtName);
        edtPhoneNo= (TextInputEditText)findViewById(R.id.edtPhone);
        edtEmail= (TextInputEditText)findViewById(R.id.edtEmail);
        edtPassword= (TextInputEditText)findViewById(R.id.edtPassword);

        txtErrorNo= findViewById(R.id.txtErrorNo);
        txtErrorEmail= findViewById(R.id.txtErrorEmail);
        txtErrorPass= findViewById(R.id.txtErrorPass);

        txtLogin = findViewById(R.id.txtSignUp);
        relativeLayout =(RelativeLayout)findViewById(R.id.relativeLayout);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        relativeLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtErrorEmail.setVisibility(View.GONE);
                txtErrorNo.setVisibility(View.GONE);
                txtErrorPass.setVisibility(View.GONE);
               if (!phoneValidation(Objects.requireNonNull(edtPhoneNo.getText()).toString().trim()))
                    txtErrorNo.setVisibility(View.VISIBLE);
               else if (!emailValidation(Objects.requireNonNull(edtEmail.getText()).toString().trim()))
                    txtErrorEmail.setVisibility(View.VISIBLE);
               else if(!passwordValidation(Objects.requireNonNull(edtPassword.getText()).toString().trim()))
                    txtErrorPass.setVisibility(View.VISIBLE);
               else{
                   String Name = Objects.requireNonNull(edtName.getText()).toString().trim();
                   String PhoneNo = Objects.requireNonNull(edtPhoneNo.getText()).toString().trim();
                   String Email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
                   String Password = Objects.requireNonNull(edtPassword.getText()).toString().trim();
                   sendData(Name, PhoneNo, Email, Password);
               }
            }
        });
    }

    public static boolean phoneValidation(String No) {
        if(No.length() == 10 ){
            Pattern p = Pattern.compile(
                    "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
            Matcher m = p.matcher(No);
            return m.matches();
        }
        return false;
    }
    public static boolean emailValidation(String email) {
        if(!email.equals("") ){
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern pat = Pattern.compile(emailRegex);
            return pat.matcher(email).matches();
        }
        return false;
    }
    public static boolean passwordValidation(String pass) {
        if(!pass.equals("")){
//            String passRegex ="^(?=.*[a-z])"+"(?=.*[A-Z])"+"(?=.*\d)"+"(?=.*[@$!%*?&])"+"[A-Za-z\d@$!%*?&]{8,}$";
            String passRegex = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%?!*&])"
                    + "(?=\\S+$).{8,20}$";

            Pattern pat = Pattern.compile(passRegex);
            return pat.matcher(pass).matches();
        }
        return false;
    }

    private void sendData(String name, String phone, String email, String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            String status = null;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getString("status");

                            if (status.equals("success")){
                                Intent i = new Intent(SignUpActivity.this, VerificationActivity.class);
                                i.putExtra("email", email);
                                startActivity(i);
                                finish();
                            }
                            else if (status.equals("exists")){
                                relativeLayout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Email-Id already exits", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                relativeLayout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Something went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "theatre LogE " + error.getMessage());

                    }

                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("signup", "signup");
                params.put("name", name);
                params.put("phone", phone);
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