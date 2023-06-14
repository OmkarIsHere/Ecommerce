package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    final String urlUser = "https://ecommdot.000webhostapp.com/api/ecomm.php";
    ImageView pimg;
    TextView ptitle, pprice, pdesc, pcat;
    String pId, pImg, pTitle, pPrice, pCategory, pDescription;
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
        relativeLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        if(getIntent().hasExtra("pid")){
            pId = getIntent().getStringExtra("pid");
            pTitle = getIntent().getStringExtra("ptitle");
            pPrice = getIntent().getStringExtra("pprice");
            String rsPrice;
            rsPrice = "$ " + getIntent().getStringExtra("pprice");
            pImg = getIntent().getStringExtra("pimg");
            pCategory = getIntent().getStringExtra("pcat");
            if(pCategory.contains("'"))
            {
                pCategory = pCategory.replaceAll("'","");
            }
            if(pTitle.contains("'"))
            {
                pTitle = pTitle.replaceAll("'","");
            }
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
            pprice.setText(rsPrice);
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        addToCart.setOnClickListener(v -> {
            SharedPreferences alldata = getSharedPreferences("alldata", MODE_PRIVATE);
            String uId = alldata.getString("uId", "0");
            if(!uId.equals("0")){
                Dialog dialog = new Dialog(ProductDetailsActivity.this);
                dialog.setContentView(R.layout.layout_alert_dialog);
                dialog.setCancelable(true);
                dialog.show();

                Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
                Button btnContinue = (Button)dialog.findViewById(R.id.btnContinue);
                ImageButton btnMinus = (ImageButton)dialog.findViewById(R.id.btnSub);
                ImageButton btnAdd = (ImageButton)dialog.findViewById(R.id.btnAdd);
                TextView pQuantity = (TextView)dialog.findViewById(R.id.txtQuantity);


                pQuantity.setText(String.valueOf(i));


                btnAdd.setOnClickListener(v1 -> {
                   if(i<4){
                       i++;
                       pQuantity.setText(String.valueOf(i));
                   }
                    if(i==4)
                        Toast.makeText(ProductDetailsActivity.this, "Maximum 4 quantity can select", Toast.LENGTH_SHORT).show();
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
                    addDatatoCart(urlUser, uId, pId, pImg, pTitle, pPrice, quantity, pCategory);
                    dialog.dismiss();
                });
            }else{
                Intent i = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void addDatatoCart(String url,String uid, String pid, String pimg , String title, String price, String quantity, String category){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        relativeLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "response - "+ response);
                        String status;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getString("status");
                            Log.d(TAG, "status? - "+ status);
                            if (status.equals("success")){
                                Toast.makeText(getApplicationContext(),"Products successfully added to cart", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ProductDetailsActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "error: "+ e);
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
                params.put("pimg", pimg);
                params.put("title",title);
                params.put("price",price);
                params.put("quantity", quantity);
                params.put("category", category);
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
