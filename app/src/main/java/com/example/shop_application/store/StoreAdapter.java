package com.example.shop_application.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_application.async.ImageLoader;
import com.example.shop_application.async.TaskRunner;
import com.example.shop_application.item.ItemModel;
import com.example.shop_application.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

// https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
// https://stackoverflow.com/questions/9250215/default-selector-background-in-clickable-views
// https://stackoverflow.com/questions/9250215/default-selector-background-in-clickable-views
// https://stackoverflow.com/questions/15444375/how-to-create-interface-between-fragment-and-adapter

/**
 * Defines the view of each row in the 'store' tab.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private final List<ItemModel> items = new ArrayList<>();
    private final StoreAdapterCallback storeAdapterCallback;
    private final TaskRunner taskRunner = new TaskRunner();

    public interface StoreAdapterCallback {
        void onAddToCart(ItemModel item);
    }

    public StoreAdapter(StoreAdapterCallback storeAdapterCallback) {
        this.storeAdapterCallback = storeAdapterCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, description, quantity, unitPrice;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.store_item_name);
            category = itemView.findViewById(R.id.store_item_category);
            description = itemView.findViewById(R.id.store_item_desc);
            quantity = itemView.findViewById(R.id.store_item_quantity);
            unitPrice = itemView.findViewById(R.id.store_item_price);
            image = itemView.findViewById(R.id.store_item_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the item at the specified position.
        ItemModel item = items.get(position);
        String name = item.getName().replaceAll("_", " ");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String category = item.getCategory().replaceAll("_", " ");
        category = category.substring(0, 1).toUpperCase() + category.substring(1);
        String description = item.getDescription();
        String quantity = item.getQuantity();
        String unitPrice = item.getPrice();

        // Load the information of the item into the view holder.
        Resources resources = holder.itemView.getResources();
        holder.name.setText(name);
        holder.category.setText(String.format(resources.getString(R.string.category), category));
        holder.description.setText(String.format(resources.getString(R.string.description), description));
        holder.quantity.setText(String.format(resources.getString(R.string.quantity), quantity));
        holder.unitPrice.setText(String.format(resources.getString(R.string.unit_price), unitPrice));
        taskRunner.executeAsync(new ImageLoader(item.getImgSrc()), (bitmap) -> holder.image.setImageBitmap(bitmap));

        // Open a dialog window when an item has been clicked.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                if (Integer.parseInt(item.getQuantity()) > 0) {
                    String title = item.getName().replaceAll("_", " ");
                    title = title.substring(0, 1).toUpperCase() + title.substring(1);
                    AtomicReference<Double> totalPriceValue = new AtomicReference<>(Double.parseDouble(item.getPrice()));;
                    AtomicReference<String> totalPriceText = new AtomicReference<>(resources.getString(R.string.total_price, String.format(Locale.CANADA, "%.2f", totalPriceValue.get())));

                    View alertView = LayoutInflater.from(v.getContext()).inflate(R.layout.purchase_layout, null);
                    TextView totalPrice = alertView.findViewById(R.id.total_price);
                    NumberPicker amount = alertView.findViewById(R.id.amount);
                    totalPrice.setText(totalPriceText.get());
                    amount.setMinValue(1);
                    amount.setMaxValue(Integer.parseInt(item.getQuantity()));
                    amount.setOnValueChangedListener((picker, oldVal, newVal) -> {
                        totalPriceValue.set(newVal * Double.parseDouble(item.getPrice()));
                        totalPriceText.set(resources.getString(R.string.total_price, String.format(Locale.CANADA, "%.2f", totalPriceValue.get())));
                        totalPrice.setText(totalPriceText.get());
                    });

                    builder.setTitle(title);
                    builder.setMessage("Set the amount to purchase");
                    builder.setView(alertView);

                    // Add the item to the cart.
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ItemModel cartItem = new ItemModel(item.getId(),
                                    item.getName(),
                                    item.getCategory(),
                                    item.getDescription(),
                                    String.valueOf(amount.getValue()),
                                    item.getPrice());
                            cartItem.setImgSrc(item.getImgSrc());
                            storeAdapterCallback.onAddToCart(cartItem);
                            Toast.makeText(holder.itemView.getContext(), "Item added to your cart.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Cancel the purchase of the item.
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

                    });
                }
                else {
                    // TODO: Change the view when an item is sold out.
                    builder.setMessage("Out of stock!");
                    builder.setPositiveButton("Confirm", (dialog, which) -> {

                    });
                }
                builder.create();
                builder.show();
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
    }
}
