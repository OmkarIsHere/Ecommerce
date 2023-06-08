package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    EditText searchView;
    Spinner spinnerSort;
    ImageButton cart;
    TextView userImg, allProducts, electronics,jewelery, menClothing, womensClothing;
    RecyclerView recyclerProducts;
    ProgressBar progressBar;
    final String urlAllProducts = "https://fakestoreapi.com/products";
    final String urlElectronicsProducts = "https://fakestoreapi.com/products/category/electronics";
    final String urlJeweleryProducts = "https://fakestoreapi.com/products/category/jewelery";
    final String urlmenClothingProducts = "https://fakestoreapi.com/products/category/men's%20clothing";
    final String urlwomensClothingProducts = "https://fakestoreapi.com/products/category/women's%20clothing";
    final String urlSortProductsAsc = "https://fakestoreapi.com/products?sort=asc";
    final String urlSortProductsDesc = "https://fakestoreapi.com/products?sort=desc";

    ArrayList<Products> productsArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userImg=findViewById(R.id.userImg);
        allProducts=findViewById(R.id.txtAll);
        electronics=findViewById(R.id.txtElectronics);
        jewelery=findViewById(R.id.txtJewelery);
        menClothing=findViewById(R.id.txtMensClothing);
        womensClothing=findViewById(R.id.txtWomensClothing);
        searchView = findViewById(R.id.searchView);
        spinnerSort = findViewById(R.id.spinnerSort);
        cart = findViewById(R.id.imgBtnCart);
        progressBar = findViewById(R.id.progressBar);

        allProducts.setOnClickListener(this);
        electronics.setOnClickListener(this);
        jewelery.setOnClickListener(this);
        menClothing.setOnClickListener(this);
        womensClothing.setOnClickListener(this);

        allProducts.setSelected(true);

        recyclerProducts = (RecyclerView)findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));


    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
            if (id == R.id.txtAll) {
                electronics.setSelected(false);
                jewelery.setSelected(false);
                menClothing.setSelected(false);
                womensClothing.setSelected(false);
                allProducts.setSelected(true);
                setProductData(urlAllProducts);
//                electronics.setBackgroundColor(R.drawable.bg_category_unselect);
//                jewelery.setBackgroundColor(R.drawable.bg_category_unselect);
//                menClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                womensClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                allProducts.setBackgroundColor(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.all, Toast.LENGTH_SHORT).show();
            }
            else if(id == R.id.txtElectronics) {
                allProducts.setSelected(false);
                jewelery.setSelected(false);
                menClothing.setSelected(false);
                womensClothing.setSelected(false);
                electronics.setSelected(true);
                setProductData(urlElectronicsProducts);

//                allProducts.setBackgroundColor(R.drawable.bg_category_unselect);
//                jewelery.setBackgroundColor(R.drawable.bg_category_unselect);
//                menClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                womensClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                electronics.setBackgroundColor(R.drawable.bg_category_selected);
            }
            else if(id == R.id.txtJewelery) {
                allProducts.setSelected(false);
                electronics.setSelected(false);
                menClothing.setSelected(false);
                womensClothing.setSelected(false);
                jewelery.setSelected(true);
                setProductData(urlJeweleryProducts);
//                allProducts.setBackgroundColor(R.drawable.bg_category_unselect);
//                electronics.setBackgroundColor(R.drawable.bg_category_unselect);
//                menClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                womensClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                jewelery.setBackgroundColor(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.jewelery, Toast.LENGTH_SHORT).show();
            }
            else if(id ==  R.id.txtMensClothing) {
                allProducts.setSelected(false);
                electronics.setSelected(false);
                jewelery.setSelected(false);
                womensClothing.setSelected(false);
                menClothing.setSelected(true);
                setProductData(urlmenClothingProducts);
//                allProducts.setBackgroundColor(R.drawable.bg_category_unselect);
//                jewelery.setBackgroundColor(R.drawable.bg_category_unselect);
//                electronics.setBackgroundColor(R.drawable.bg_category_unselect);
//                womensClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                menClothing.setBackgroundColor(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.men_s_clothing, Toast.LENGTH_SHORT).show();
            }
            else if(id == R.id.txtWomensClothing) {
                allProducts.setSelected(false);
                electronics.setSelected(false);
                jewelery.setSelected(false);
                menClothing.setSelected(false);
                womensClothing.setSelected(true);
                setProductData(urlwomensClothingProducts);
//                allProducts.setBackgroundColor(R.drawable.bg_category_unselect);
//                jewelery.setBackgroundColor(R.drawable.bg_category_unselect);
//                menClothing.setBackgroundColor(R.drawable.bg_category_unselect);
//                electronics.setBackgroundColor(R.drawable.bg_category_unselect);
//                womensClothing.setBackgroundColor(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.women_s_clothing, Toast.LENGTH_SHORT).show();
            }
        }

    private void setProductData(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray list = new JSONArray(response);
                            for(int i=0; i<list.length(); i++){
                                JSONObject listObjects = list.getJSONObject(i);
                                String pId = listObjects.getString("id");
                                String pImg = listObjects.getString("image");
                                String pTitle = listObjects.getString("title");
                                String pPrice = listObjects.getString("price");
                                String pCategory = listObjects.getString("category");
                                String pDescription = listObjects.getString("description");

                                Products products = new Products(pId, pImg, pTitle, pPrice, pCategory, pDescription);
                                productsArrayList.add(products);
                            }
                            Log.d(TAG,"arrData: "+ productsArrayList.toString());

                            ProductsRecyclerAdapter padapter = new ProductsRecyclerAdapter(getApplicationContext(),productsArrayList);
                            recyclerProducts.setAdapter(padapter);
                            Log.d(TAG, "adapter set ");
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
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

                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
        Log.d(TAG, "queued success: ");

    }

}


