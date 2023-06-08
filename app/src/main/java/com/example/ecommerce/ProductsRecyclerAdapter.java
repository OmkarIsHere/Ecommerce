package com.example.ecommerce;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;


public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {
    Context context;
    String location;
    ArrayList<Products> arrProducts;

    ProductsRecyclerAdapter(Context context, ArrayList<Products> arrProducts){
        this.context= context;
        this.arrProducts = arrProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!Objects.equals(arrProducts.get(position).pImg, "")) {
            Glide.with(context)
                    .load(arrProducts.get(position).pImg)
                    .into(holder.pimg);
        }else{
            holder.pimg.setBackgroundResource(R.drawable.no_image);
        }
        String title = arrProducts.get(position).pTitle;
        if(title.length() > 33){
            String firstHalf = title.substring(0,32);
            String fullString = firstHalf + "...";
            holder.ptitle.setText(fullString);
        }else{
            holder.ptitle.setText(title);
        }
        String price ="Rs. "+ (arrProducts.get(position).pPrice) + "/-";
        holder.pprice.setText(price);
        holder.pid.setText(arrProducts.get(position).pId);
        holder.pdesc.setText(arrProducts.get(position).pDescription);
        holder.pcat.setText(arrProducts.get(position).pCategory);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences prefLocation = view.getContext().getSharedPreferences("location", MODE_PRIVATE);
//                location = prefLocation.getString("city", "Select City");
//                if(!Objects.equals(location, "Select City")){
//                    Intent intent = new Intent(context, MovieDetailsActivity.class);
//                    intent.putExtra("movie_name", arrSecond.get(holder.getLayoutPosition()).mName);
//                    context.startActivity(intent);
//                }else{
//                    Toast.makeText(view.getContext(), "Please select a city", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pimg;
        TextView ptitle, pprice, pid, pdesc, pcat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pimg = itemView.findViewById(R.id.imgProduct);
            ptitle = itemView.findViewById(R.id.txtProductTitle);
            pprice = itemView.findViewById(R.id.txtProductPrice);
            pid = itemView.findViewById(R.id.txtproductId);
            pcat = itemView.findViewById(R.id.txtProductCategory);
            pdesc = itemView.findViewById(R.id.txtProductDesc);
        }
    }
}

