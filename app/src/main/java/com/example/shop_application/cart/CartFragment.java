package com.example.shop_application.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_application.R;
import com.example.shop_application.enums.Link;
import com.example.shop_application.enums.MathSymbol;
import com.example.shop_application.history.HistoryViewModel;
import com.example.shop_application.store.StoreViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

// TODO: Add options to remove or purchase a single item from the cart.
public class CartFragment extends Fragment {
    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private HistoryViewModel historyViewModel;
    private StoreViewModel storeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_layout, container, false);
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CartViewModel cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        storeViewModel = new ViewModelProvider(requireActivity()).get(StoreViewModel.class);
        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        cartAdapter = new CartAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartAdapter);
        cartViewModel.getItems(Link.CART.getURL()).observe(getViewLifecycleOwner(), items -> {
            cartAdapter.setItems(new ArrayList<>(items.values()));
            TextView totalPriceView = view.findViewById(R.id.cart_total_price);
            TextView emptyCartView = view.findViewById(R.id.empty_cart_msg);
            if (items.values().isEmpty()) {
                totalPriceView.setVisibility(View.GONE);
                emptyCartView.setVisibility(View.VISIBLE);
            }
            else {
                totalPriceView.setVisibility(View.VISIBLE);
                emptyCartView.setVisibility(View.GONE);
            }
            String totalPrice = String.format(Locale.CANADA, "%.2f", cartViewModel.getTotalPrice());
            totalPriceView.setText(String.format(view.getResources().getString(R.string.total_price), totalPrice));
        });


        // Button to buy all items in the shopping cart.
        view.findViewById(R.id.purchase_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Get the user's id.
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", "1");
                    jsonObject.put("total_price", cartViewModel.getTotalPrice());
                    jsonObject.put("order_items", cartViewModel.getJsonArray());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                historyViewModel.saveData(Link.UPDATE_HISTORY.getURL(), jsonArray, "");
                cartViewModel.deleteData(Link.DELETE_CART.getURL(), cartViewModel.getJsonArray());

            }
        });

        // Button to clear all items int eh shopping cart.
        view.findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Get the user's id.
                storeViewModel.saveData(Link.UPDATE_STORE.getURL(), cartViewModel.getJsonArray(), MathSymbol.ADDITION.getOperator());
                cartViewModel.deleteData(Link.DELETE_CART.getURL(), cartViewModel.getJsonArray());
            }
        });
    }
}
