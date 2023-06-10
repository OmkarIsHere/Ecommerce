package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "VerificationActivity";
    private static final String url = "https://inundated-lenders.000webhostapp.com/api/login.php";
    ImageButton btnBack;
    TextView txtEmail;
    TextInputEditText edtOtp;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    String email;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verification);
        btnBack= findViewById(R.id.btnBack);
        btnSubmit = findViewById(R.id.btnSubmit);
        edtOtp = (TextInputEditText)findViewById(R.id.edtOtpCode);
        txtEmail = findViewById(R.id.txtEmail);
        relativeLayout =(RelativeLayout)findViewById(R.id.relativeLayout);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        relativeLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        if(getIntent().hasExtra("email")){
            email = getIntent().getStringExtra("email");
            String getEmail = "Email id - " + email;
            txtEmail.setText(getEmail);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = Objects.requireNonNull(edtOtp.getText()).toString();
                if(otp.length()==6)
                    verifyOtp(email,otp);
                else
                    Toast.makeText(getApplicationContext(), "Enter 6 digit Code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp(String email, String code){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        relativeLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onResponse: "+ response);
                        String status = null;
                        String  codee = null;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            status = jsonObject.getString("status");
                            codee = jsonObject.getString("code");
                            if(codee.equals("right")){
                                    Intent i = new Intent(VerificationActivity.this,LoginActivity.class);
                                    startActivity(i);
                            }
                            else if(codee.equals("wrong")){
                                    Toast.makeText(getApplicationContext(),"Entered otp is Wrong", Toast.LENGTH_LONG).show();
                            }
//                            else if(status.equals("false")){
//                                Toast.makeText(getApplicationContext(),"Something went wrong, Please try again", Toast.LENGTH_LONG).show();
//                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Something went wrong, Please try again later", Toast.LENGTH_LONG).show();
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
                params.put("checkotp", "checkotp");
                params.put("email", email);
                params.put("code", code);
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
        Log.d(TAG, "otp queued success: ");
    }
}