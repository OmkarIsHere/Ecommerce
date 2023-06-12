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


public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";
    final String api = "https://ecommdot.000webhostapp.com/api/ecomm.php";
    long lastClick;
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
        Log.d(TAG, "onCreateViewHolder: ");
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
        if(title.length() > 33){
            String firstHalf = title.substring(0,35);
            String fullString = firstHalf + "...";
            holder.ptitle.setText(fullString);
        }else{
            holder.ptitle.setText(title);
        }
        String price ="$ "+ (arrCart.get(position).pPrice);
        Log.d(TAG, "Price set");
        holder.pprice.setText(price);
        String q ="Quantity : "+ arrCart.get(position).pQuantity;
        holder.pquant.setText(q);
        Log.d(TAG, "Quantity set");
        holder.pcat.setText(arrCart.get(position).pCategory);
        String id = arrCart.get(position).pId;
        String uid = arrCart.get(position).uId;
        holder.pid.setText(id);
        Log.d(TAG, "Category and id set");
        holder.btnDelete.setContentDescription(arrCart.get(position).uId);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialog = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.layout_delete_alert, null);
//                Dialog dialog = new Dialog();
//                dialog.setContentView(R.layout.layout_delete_alert);
//                dialog.setCancelable(false);
//                dialog.show();
                builder.setView(dialog);
                builder.setCancelable(true);
                builder.show();

                Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
                Button btnDelete = (Button)dialog.findViewById(R.id.btnDelete);
                ProgressBar progressBar1 = (ProgressBar)dialog.findViewById(R.id.progressBar);

//                btnCancel.setOnClickListener(v1 -> dialog.dismiss());

                btnDelete.setOnClickListener(v12 -> {
                    if (SystemClock.elapsedRealtime() - lastClick < 180000){  //For Click after 2 min
                        return;
                    }
                    lastClick = SystemClock.elapsedRealtime();
                    deleteCartItem(uid, id, api);
                    builder.setCancelable(false);
                    btnDelete.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    progressBar1.setVisibility(View.VISIBLE);

                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrCart.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pimg;
        ImageButton btnDelete;
        TextView pid, ptitle, pprice, pquant, pcat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pimg = itemView.findViewById(R.id.imgProduct);
            pid =(TextView)itemView.findViewById(R.id.txtpId);
            ptitle = itemView.findViewById(R.id.txtProductTitle);
            pprice = itemView.findViewById(R.id.txtProductPrice);
            pcat = itemView.findViewById(R.id.txtProductCat);
            pquant = itemView.findViewById(R.id.txtProductQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void deleteCartItem(String uId, String pId, String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "Response: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("success")){
                                Intent i = new Intent(context.getApplicationContext(),CartActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);// | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                context.startActivity(i);
                            }
                            else if(status .equals("fail")){
                                Toast.makeText(context.getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Log.d(TAG, "json exception "+ e);
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context.getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "LogE " + error.getMessage());
                    }

                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("deleteItem", "deleteItem");
                params.put("uId", uId);
                params.put("pId", pId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context.getApplicationContext()).add(stringRequest);
        Log.d(TAG, "deleteCart queued success");
    }
}


