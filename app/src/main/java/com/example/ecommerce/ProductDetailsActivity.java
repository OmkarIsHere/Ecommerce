package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailsActivity";
    ImageView pimg;
    TextView ptitle, pprice, pdesc, pcat;
    String pId, pImg, pTitle, pPrice, pCategory, pDescription;;
    Button addToCart;
    ImageButton btnBack;
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
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

}