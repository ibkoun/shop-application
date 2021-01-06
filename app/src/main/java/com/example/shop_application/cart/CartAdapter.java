package com.example.shop_application.cart;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_application.async.ImageLoader;
import com.example.shop_application.async.TaskRunner;
import com.example.shop_application.item.ItemModel;
import com.example.shop_application.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Defines the view of each row in the 'cart' tab.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final List<ItemModel> items  = new ArrayList<>();
    private final TaskRunner taskRunner = new TaskRunner();
    private View previousSelection;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView item, unitPrice, subtotal;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.cart_item);
            unitPrice = itemView.findViewById(R.id.cart_item_unit_price);
            subtotal = itemView.findViewById(R.id.cart_item_subtotal);
            image = itemView.findViewById(R.id.cart_item_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the item at the specified position.
        ItemModel item = items.get(position);
        String id = item.getId();
        String name = item.getName().replaceAll("_", " ");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String category = item.getCategory().replaceAll("_", " ");
        category = category.substring(0, 1).toUpperCase() + category.substring(1);
        String quantity = item.getQuantity();
        String unitPrice = String.format(Locale.CANADA, "%.2f", Double.parseDouble(item.getPrice()));
        String totalPrice = String.format(Locale.CANADA, "%.2f", Integer.parseInt(item.getQuantity()) * Double.parseDouble(item.getPrice()));

        // Load the information of the item into the view holder.
        Resources resources = holder.itemView.getResources();
        holder.item.setText(String.format(resources.getString(R.string.item_quantity), name, quantity));
        holder.unitPrice.setText(String.format(resources.getString(R.string.unit_price), unitPrice));
        holder.subtotal.setText(String.format(resources.getString(R.string.cart_subtotal), totalPrice));
        taskRunner.executeAsync(new ImageLoader(item.getImgSrc()), (bitmap) -> holder.image.setImageBitmap(bitmap));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // https://stackoverflow.com/questions/27377830/what-is-the-equivalent-listview-setselection-in-case-of-recycler-view
                // TODO: Complete the click event.
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ItemModel> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        if (previousSelection != null) {
            previousSelection.setSelected(false);
            previousSelection.setVisibility(View.GONE);
        }
    }

    public JSONArray getJsonArray() {
        JSONArray jsonArray = new JSONArray();
        for (ItemModel item : items) {
            jsonArray.put(item.getJsonObject());
        }
        return jsonArray;
    }
}
