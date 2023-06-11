package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    final String api = "https://ecommdot.000webhostapp.com/api/ecomm.php";
    ArrayList<Cart> cartArrayList = new ArrayList<>();
    ProgressBar progressBar;
    ImageButton btnBack;
    TextView errorMessage;
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
        errorMessage = findViewById(R.id.errorMessage);


        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnCheckout.setOnClickListener(v -> {
            CheckOutFragment checkOutFragment = new CheckOutFragment(cartArrayList);
            checkOutFragment.show(getSupportFragmentManager(), checkOutFragment.getTag());

        });

        SharedPreferences alldata = getSharedPreferences("alldata", MODE_PRIVATE);
        String uid = alldata.getString("uId", "0");
        Log.d(TAG, "alldata: "+uid);
        if(!uid.equals("0")){
            getCartItem(uid, api);
        }
        else{
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText(R.string.login_to_see_cart_item);
//            Toast.makeText(this,"Login to see cart items", Toast.LENGTH_LONG).show();
        }
    }

    private void getCartItem(String uid, String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String data;
                            String totalPrice, totalQuantity, totalProducts;
                            int tPrice, tQuantity, tProducts;
                            tPrice=tQuantity=tProducts=0;
                            Log.d(TAG, "Response: " + response);
                             JSONArray list = new JSONArray(response);
                            JSONObject listObject = list.getJSONObject(0);
                            data = listObject.getString("data");
                            if(data.equals("true")){
                                  for(int i=0; i<list.length(); i++){
                                      JSONObject listObjects = list.getJSONObject(i);
                                      String pId = listObjects.getString("pId");
                                      String pImg = listObjects.getString("pImg");
                                      String pTitle = listObjects.getString("pTitle");
                                      String pPrice = listObjects.getString("pPrice");
                                      String pCategory = listObjects.getString("pCategory");
                                      String pQuantity = listObjects.getString("pQuantity");

                                      tProducts++; //= tProducts + Integer.parseInt(pId);
                                      tPrice = tPrice + Integer.parseInt(pPrice);
                                      tQuantity = tQuantity + Integer.parseInt(pQuantity);

                                      Cart cart = new Cart(uid, pId, pImg , pTitle, pPrice, pCategory, pQuantity);
                                      cartArrayList.add(cart);
                                  }
                                  totalProducts= ""+ tProducts;
                                  totalPrice = ""+ tPrice;
                                  totalQuantity =""+ tQuantity;
                                  Log.d(TAG,totalProducts+" - "+totalPrice+" - "+totalQuantity);
                                  Log.d(TAG,"arrData: "+ cartArrayList.toString());

                                SharedPreferences orderSummery = getSharedPreferences("orderSummery", MODE_PRIVATE);
                                SharedPreferences.Editor prefEditor = orderSummery.edit();
                                prefEditor.putString("totalProducts",totalProducts);
                                prefEditor.putString("totalPrice",totalPrice);
                                prefEditor.putString("totalQuantity",totalQuantity);
                                prefEditor.apply();

                                  CartRecyclerAdapter cadapter = new CartRecyclerAdapter(getApplicationContext(),cartArrayList);
                                  recyclerCart.setAdapter(cadapter);
                            } else if (data.equals("false")) {
                                errorMessage.setVisibility(View.VISIBLE);
                                errorMessage.setText(R.string.cart_is_empty);
//                                Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                            relativeLayout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            btnBack.setVisibility(View.VISIBLE);
                            btnCheckout.setVisibility(View.VISIBLE);
                            Log.d(TAG, "btn visible");
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