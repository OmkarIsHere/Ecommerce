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


public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";
    Context context;
    ArrayList<Cart> arrCart;

    CartRecyclerAdapter(Context context, ArrayList<Cart> arrCart){
        this.context= context;
        this.arrCart = arrCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!Objects.equals(arrCart.get(position).pImg, null)) {
            Glide.with(context)
                    .load(arrCart.get(position).pImg)
                    .into(holder.pimg);
        }else{
            holder.pimg.setBackgroundResource(R.drawable.no_image);
        }
        String title = arrCart.get(position).pTitle;
            holder.ptitle.setText(title);
        String price ="Rs. "+ (arrCart.get(position).pPrice) + "/-";
        holder.pprice.setText(price);
        String q ="Quantity : "+ arrCart.get(position).pQuantity;
        holder.pquant.setText(q);
        holder.pcat.setText(arrCart.get(position).pCategory);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.d(TAG, "onClicked:");
//                Intent intent = new Intent(context, ProductDetailsActivity.class);
//                intent.putExtra("pid", arrCart.get(holder.getLayoutPosition()).pId);
//                intent.putExtra("pimg", arrCart.get(holder.getLayoutPosition()).pImg);
//                intent.putExtra("ptitle", title);
//                intent.putExtra("pprice", arrCart.get(holder.getLayoutPosition()).pPrice);
//                intent.putExtra("pcat", arrCart.get(holder.getLayoutPosition()).pCategory);
//                intent.putExtra("pdesc", arrCart.get(holder.getLayoutPosition()).pDescription);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                context.getApplicationContext().startActivity(intent);
//                Log.d(TAG, "intent passed");
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return arrCart.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pimg;
        TextView ptitle, pprice, pquant, pcat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pimg = itemView.findViewById(R.id.imgProduct);
            ptitle = itemView.findViewById(R.id.txtProductTitle);
            pprice = itemView.findViewById(R.id.txtProductPrice);
            pcat = itemView.findViewById(R.id.txtProductCategory);
            pquant = itemView.findViewById(R.id.txtProductQuantity);
        }
    }
}


