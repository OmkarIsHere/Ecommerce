package com.example.ecommerce;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CheckoutRecyclerAdapter extends RecyclerView.Adapter<CheckoutRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CheckoutAdapter";
    Context context;
    ArrayList<Cart> arrCart;

    CheckoutRecyclerAdapter(Context context, ArrayList<Cart> arrCart){
        this.context= context;
        this.arrCart = arrCart;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_order_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        Log.d(TAG, "arrList : "+ arrCart);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!Objects.equals(arrCart.get(position).pImg, "null")) {
            Glide.with(context)
                    .load(arrCart.get(position).pImg)
                    .into(holder.pimg);
        }else{
            holder.pimg.setBackgroundResource(R.drawable.no_image);
        }
        Log.d(TAG, "Image set");
        String title = arrCart.get(position).pTitle;
        holder.ptitle.setText(title);
        String price ="$ "+ (arrCart.get(position).pPrice);
        Log.d(TAG, "Price set");
        holder.pprice.setText(price);
        String q ="Quantity : "+ arrCart.get(position).pQuantity;
        holder.pquant.setText(q);
    }

    @Override
    public int getItemCount() {
        return arrCart.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pimg;
        TextView ptitle, pprice, pquant;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pimg = itemView.findViewById(R.id.imgProduct);
            ptitle = itemView.findViewById(R.id.txtProductTitle);
            pprice = itemView.findViewById(R.id.txtProductPrice);
            pquant = itemView.findViewById(R.id.txtProductQuantity);
        }
    }


}


