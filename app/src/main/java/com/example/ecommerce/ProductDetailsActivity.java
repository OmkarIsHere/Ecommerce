package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailsActivity";
    final String urlUser = "https://inundated-lenders.000webhostapp.com/api/login.php";
    ImageView pimg;
    TextView ptitle, pprice, pdesc, pcat;
    String pId, pImg, pTitle, pPrice, pCategory, pDescription;;
    Button addToCart;
    ImageButton btnBack;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ptitle = findViewById(R.id.txtProductTitle);
        pcat = findViewById(R.id.txtProductCategory);
        pprice = findViewById(R.id.txtProductPrice);
        pdesc = findViewById(R.id.txtProductDesc);
        pimg = findViewById(R.id.imgProduct);
        btnBack = findViewById(R.id.btnBack);
        addToCart = findViewById(R.id.btnAddCart);

        relativeLayout =(RelativeLayout)findViewById(R.id.relative);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        if(getIntent().hasExtra("pid")){
            pId = getIntent().getStringExtra("pid");
            pTitle = getIntent().getStringExtra("ptitle");
            pPrice = "Rs. " + getIntent().getStringExtra("pprice");
            pImg = getIntent().getStringExtra("pimg");
            pCategory = getIntent().getStringExtra("pcat");
            pDescription = getIntent().getStringExtra("pdesc");

            if(!Objects.equals(pImg , "")) {
                Glide.with(this)
                        .load(pImg)
                        .into(pimg);
            }else{
                pimg.setBackgroundResource(R.drawable.no_image);
            }
            ptitle.setText(pTitle);
            pcat.setText(pCategory);
            pdesc.setText(pDescription);
            pprice.setText(pPrice);
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        addToCart.setOnClickListener(v -> {
            SharedPreferences alldata = getSharedPreferences("alldata", MODE_PRIVATE);
            String uId = alldata.getString("uId", "id");
            if(!uId.equals("0")){
                Dialog dialog = new Dialog(ProductDetailsActivity.this);
                dialog.setContentView(R.layout.layout_alert_dialog);
                dialog.setCancelable(false);
                dialog.show();

                Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
                Button btnContinue = (Button)dialog.findViewById(R.id.btnContinue);
                ImageButton btnMinus = (ImageButton)dialog.findViewById(R.id.btnSub);
                ImageButton btnAdd = (ImageButton)dialog.findViewById(R.id.btnAdd);
                TextView pQuantity = (TextView)dialog.findViewById(R.id.txtQuantity);


                pQuantity.setText(String.valueOf(i));

                btnAdd.setOnClickListener(v1 -> {
                   if(i>=1){
                       i++;
                       pQuantity.setText(String.valueOf(i));
                   }
                });
                btnMinus.setOnClickListener(v1 -> {
                   if(i>1){
                       i--;
                   }
                   pQuantity.setText(String.valueOf(i));
                });



                btnCancel.setOnClickListener(v1 -> dialog.dismiss());
                btnContinue.setOnClickListener(v12 -> {
                    String quantity = String.valueOf(pQuantity.getText());
                    addDatatoCart(urlUser, uId, pId, pTitle, pPrice, quantity);
                    dialog.dismiss();
                });
            }else{
                startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
            }

        });
    }

    private void addDatatoCart(String url,String uid, String pid, String title, String price, String quantity){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String status = null;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getString("status");

                            if (status.equals("success")){
                                Intent i = new Intent(ProductDetailsActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
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
                params.put("addtocart", "addtocart");
                params.put("uid", uid);
                params.put("pid", pid);
                params.put("title",title);
                params.put("price",price);
                params.put("quantity", quantity);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        Log.d(TAG, "queued success: ");
        relativeLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

}