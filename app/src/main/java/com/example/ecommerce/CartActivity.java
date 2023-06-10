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
    final String url = "https://inundated-lenders.000webhostapp.com/api/login.php";
    ArrayList<Cart> cartArrayList = new ArrayList<>();
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    RecyclerView recyclerCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerCart = (RecyclerView)findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.relativeLayout);

        SharedPreferences alldata = getSharedPreferences("alldata", MODE_PRIVATE);
        String uid = alldata.getString("uId", "0");
        Log.d(TAG, "alldata: "+uid);
        if(!uid.equals("0")){
            getCartItem(uid);
        }
    }

    private void getCartItem(String uid){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        relativeLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray list = new JSONArray(response);
                            for(int i=0; i<list.length(); i++){
                                JSONObject listObjects = list.getJSONObject(i);
                                String pId = listObjects.getString("uId");
//                                String pImg = listObjects.getString("image");
                                String pTitle = listObjects.getString("pTitle");
                                String pPrice = listObjects.getString("pPrice");
                                String pCategory = listObjects.getString("pCategory");
                                String pQuantity = listObjects.getString("pQuantity");


                                Cart cart = new Cart(pId, null , pTitle, pPrice, pCategory, pQuantity);
                                cartArrayList.add(cart);
                            }
                            Log.d(TAG,"arrData: "+ cartArrayList.toString());

                            CartRecyclerAdapter cadapter = new CartRecyclerAdapter(getApplicationContext(),cartArrayList);
                            recyclerCart.setAdapter(cadapter);
                            Log.d(TAG, "adapter set ");
                        } catch (JSONException e) {
                            relativeLayout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
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
                params.put("getcart", "getcart");
                params.put("uId", uid);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(CartActivity.this).add(stringRequest);
        Log.d(TAG, "queued success: ");
    }
}