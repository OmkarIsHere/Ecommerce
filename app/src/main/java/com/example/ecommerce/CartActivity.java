package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    final String api = "https://inundated-lenders.000webhostapp.com/api/login.php";
    ArrayList<Cart> cartArrayList = new ArrayList<>();
    ProgressBar progressBar;
    ImageButton btnBack;
    Button btnCheckout;
    RelativeLayout relativeLayout;
    RecyclerView recyclerCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerCart = (RecyclerView)findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);
        btnCheckout = findViewById(R.id.btnCheckout);
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.relativeLayout);


        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        SharedPreferences alldata = getSharedPreferences("alldata", MODE_PRIVATE);
        String uid = alldata.getString("uId", "0");
        Log.d(TAG, "alldata: "+uid);
        if(!uid.equals("0")){
            getCartItem(uid, api);
        }
        else{
            Toast.makeText(this,"Login to see cart items", Toast.LENGTH_LONG).show();
        }
    }

    private void getCartItem(String uid, String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                        Log.d(TAG, "Response: " + response);
                                  JSONArray list = new JSONArray(response);
                                  for(int i=0; i<list.length(); i++){
                                      JSONObject listObjects = list.getJSONObject(i);
                                      String pId = listObjects.getString("pId");
                                      String pTitle = listObjects.getString("pTitle");
                                      String pPrice = listObjects.getString("pPrice");
                                      String pCategory = listObjects.getString("pCategory");
                                      String pQuantity = listObjects.getString("pQuantity");
                                      String pImg = "null";//listObjects.getString("image");

                                      Cart cart = new Cart(uid, pId, pImg , pTitle, pPrice, pCategory, pQuantity);
                                      cartArrayList.add(cart);
                                  }
                                  Log.d(TAG,"arrData: "+ cartArrayList.toString());
                                  CartRecyclerAdapter cadapter = new CartRecyclerAdapter(getApplicationContext(),cartArrayList);
                                  recyclerCart.setAdapter(cadapter);
                                  relativeLayout.setVisibility(View.GONE);
                                  progressBar.setVisibility(View.GONE);
                                  btnBack.setVisibility(View.VISIBLE);
                                  btnCheckout.setVisibility(View.VISIBLE);
                                  Log.d(TAG, "adapter set ");

                        } catch (JSONException e) {
                            relativeLayout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            btnBack.setVisibility(View.VISIBLE);
                            btnCheckout.setVisibility(View.VISIBLE);
                            Log.d(TAG, "json exception "+ e);
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "LogE " + error.getMessage());
                    }

                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("getCart", "getCart");
                params.put("uId", uid);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(CartActivity.this).add(stringRequest);
        Log.d(TAG, "cart queued success: ");
    }
}