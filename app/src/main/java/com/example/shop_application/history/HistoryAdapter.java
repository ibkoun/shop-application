package com.example.shop_application.history;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_application.cart.CartAdapter;
import com.example.shop_application.item.ItemModel;
import com.example.shop_application.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final List<OrderModel> orders = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, totalPrice;
        RecyclerView items;
        CartAdapter cartAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.order_date);
            totalPrice = itemView.findViewById(R.id.order_total_price);
            items = itemView.findViewById(R.id.order_items);
            cartAdapter = new CartAdapter();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            items.setLayoutManager(linearLayoutManager);
            items.setAdapter(cartAdapter);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the order at the specified position.
        OrderModel order = orders.get(position);
        List<ItemModel> items = order.getItems();

        // Load the information of the order into the view holder.
        StringBuilder details = new StringBuilder();
        Iterator<ItemModel> itemsIterator = items.iterator();
        Resources resources = holder.itemView.getResources();
        ItemModel item;
        while (itemsIterator.hasNext()) {
            item = itemsIterator.next();
            String name = item.getName().replaceAll("_", " ");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            details.append(String.format(resources.getString(R.string.item_quantity), name, item.getQuantity()));
            if (itemsIterator.hasNext()) details.append("\n");
        }
        holder.date.setText(String.format(resources.getString(R.string.purchase_date), order.getDate()));
        holder.totalPrice.setText(String.format(resources.getString(R.string.total_price), order.getTotalPrice()));
        holder.cartAdapter.setItems(order.getItems());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setHistory(List<OrderModel> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
        notifyDataSetChanged();
    }
}
