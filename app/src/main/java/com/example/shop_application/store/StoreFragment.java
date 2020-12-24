package com.example.shop_application.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shop_application.cart.CartViewModel;
import com.example.shop_application.enums.Link;
import com.example.shop_application.enums.MathSymbol;
import com.example.shop_application.item.ItemModel;
import com.example.shop_application.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra
// https://medium.com/@vinodpattanshetti49/fragment-lifecycle-while-doing-add-and-replace-6a3f084364af
// https://stackoverflow.com/questions/2367936/listview-onitemclicklistener-not-responding?rq=1
// https://guides.codepath.com/android/using-the-recyclerview

public class StoreFragment extends Fragment implements StoreAdapter.StoreAdapterCallback {
    private StoreAdapter storeAdapter;
    private StoreViewModel storeViewModel;
    private CartViewModel cartViewModel;
    private RecyclerView recyclerView;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    public void onAddToCart(ItemModel item) {
        if (Integer.parseInt(item.getQuantity()) > 0) {
            // TODO: Get the user's id.
            JSONArray jsonArray1 = new JSONArray();
            JSONObject jsonObject1 = item.getJsonObject();
            JSONArray jsonArray2 = new JSONArray();
            JSONObject jsonObject2 = item.getJsonObject();
            try {
                jsonObject1.put("user_id", "1");
                jsonObject2.put("user_id", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray1.put(jsonObject1);
            jsonArray2.put(jsonObject2);
            storeViewModel.saveData(Link.UPDATE_STORE.getURL(), jsonArray1, MathSymbol.SUBTRACTION.getOperator());
            cartViewModel.saveData(Link.UPDATE_CART.getURL(), jsonArray2, MathSymbol.ADDITION.getOperator());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_layout, container, false);
        recyclerView = view.findViewById(R.id.store_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storeViewModel = new ViewModelProvider(requireActivity()).get(StoreViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        storeAdapter = new StoreAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(storeAdapter);
        storeViewModel.getItems(Link.STORE.getURL()).observe(getViewLifecycleOwner(), data -> {
            List<ItemModel> items = new ArrayList<>(data.values());
            storeAdapter.setItems(items);
            storeAdapter.notifyDataSetChanged();
        });
    }
}
