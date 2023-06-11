package com.example.ecommerce;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;


public class CheckOutFragment extends BottomSheetDialogFragment {
    private static final String TAG = "CheckOutFragment";
    RecyclerView recyclerCart;
    public ArrayList<Cart> arrList;
    TextView txtTotalPrice, txtTotalQuantity, txtTotalProducts;
    String totalPrice, totalQuantity, totalProducts;
    Button btnContinue;
    CheckOutFragment(ArrayList<Cart>arrList){
       this.arrList = arrList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        txtTotalPrice= view.findViewById(R.id.txtTotalAmt);
        txtTotalQuantity= view.findViewById(R.id.txtTotalQuantity);
        txtTotalProducts= view.findViewById(R.id.txtTotalProducts);
        btnContinue =view.findViewById(R.id.btnContinue);
        recyclerCart =(RecyclerView)view.findViewById(R.id.recyclerCheckout);
        recyclerCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(!arrList.isEmpty()){
            CheckoutRecyclerAdapter cadapter = new CheckoutRecyclerAdapter(getActivity(),arrList);
            recyclerCart.setAdapter(cadapter);
            Log.d(TAG, "arrList : "+ arrList);
        }

        SharedPreferences orderdata = requireActivity().getSharedPreferences("orderSummery", MODE_PRIVATE);
        totalProducts= orderdata.getString("totalProducts","N/A");
        totalPrice= orderdata.getString("totalPrice","N/A");
        totalQuantity = orderdata.getString("totalQuantity","N/A");

        txtTotalPrice.setText(totalPrice);
        txtTotalQuantity.setText(totalQuantity);
        txtTotalProducts.setText(totalProducts);

        btnContinue.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "You have been successfully checkout", Toast.LENGTH_SHORT).show();
        });


        return view;
    }
}