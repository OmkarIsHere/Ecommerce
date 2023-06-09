package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    ArrayList<String> arrSpinner = new ArrayList<>();
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

        recyclerProducts = (RecyclerView)findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        arrSpinner.add("New to Old");
        arrSpinner.add("Old to New");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrSpinner);
        spinnerSort.setAdapter(spinnerAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selected = spinnerSort.getSelectedItem().toString();
               if(selected.equals("New to Old")){
                   setProductData(urlSortProductsDesc);
               }else{
                   setProductData(urlSortProductsAsc);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
            if (id == R.id.txtAll) {
                setProductData(urlAllProducts);
                electronics.setBackgroundResource(R.drawable.bg_category_unselect);
                jewelery.setBackgroundResource(R.drawable.bg_category_unselect);
                menClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                womensClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                allProducts.setBackgroundResource(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.all, Toast.LENGTH_SHORT).show();
            }
            else if(id == R.id.txtElectronics) {
                setProductData(urlElectronicsProducts);

                allProducts.setBackgroundResource(R.drawable.bg_category_unselect);
                jewelery.setBackgroundResource(R.drawable.bg_category_unselect);
                menClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                womensClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                electronics.setBackgroundResource(R.drawable.bg_category_selected);
            }
            else if(id == R.id.txtJewelery) {
                setProductData(urlJeweleryProducts);
                allProducts.setBackgroundResource(R.drawable.bg_category_unselect);
                electronics.setBackgroundResource(R.drawable.bg_category_unselect);
                menClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                womensClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                jewelery.setBackgroundResource(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.jewelery, Toast.LENGTH_SHORT).show();
            }
            else if(id ==  R.id.txtMensClothing) {
                setProductData(urlmenClothingProducts);
                allProducts.setBackgroundResource(R.drawable.bg_category_unselect);
                jewelery.setBackgroundResource(R.drawable.bg_category_unselect);
                electronics.setBackgroundResource(R.drawable.bg_category_unselect);
                womensClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                menClothing.setBackgroundResource(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.men_s_clothing, Toast.LENGTH_SHORT).show();
            }
            else if(id == R.id.txtWomensClothing) {
                setProductData(urlwomensClothingProducts);
                allProducts.setBackgroundResource(R.drawable.bg_category_unselect);
                jewelery.setBackgroundResource(R.drawable.bg_category_unselect);
                menClothing.setBackgroundResource(R.drawable.bg_category_unselect);
                electronics.setBackgroundResource(R.drawable.bg_category_unselect);
                womensClothing.setBackgroundResource(R.drawable.bg_category_selected);
//                Toast.makeText(this, R.string.women_s_clothing, Toast.LENGTH_SHORT).show();
            }
        }

    private void setProductData(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!productsArrayList.isEmpty()){
                            productsArrayList.clear();
                        }
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
