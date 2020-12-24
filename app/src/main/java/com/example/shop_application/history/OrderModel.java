package com.example.shop_application.history;

import com.example.shop_application.item.ItemModel;

import java.util.List;
import java.util.Locale;

/**
 * Contains the information of an order placed by a user.
 */
public class OrderModel {
    private String id, date, totalPrice;
    private List<ItemModel> items;

    public OrderModel(String date, List<ItemModel> items) {
        this.date = date;
        this.items = items;
        double value = 0;
        for (ItemModel item : this.items) {
            value += Double.parseDouble(item.getPrice());
        }
        totalPrice = String.format(Locale.CANADA, "%.2f", value);
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public List<ItemModel> getItems() { return items; }

    public String getDate() { return date; }

    public void setDate(String date) {this.date = date; }

    public String getTotalPrice() { return totalPrice; }
}
