package com.example.shop_application.item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the information about an item.
 */
public class ItemModel {
    private String id, name, category, description, quantity, unitPrice, imgSrc;
    private final Map<String, String> params = new HashMap<>();

    public ItemModel(String id, String name, String category, String description, String quantity, String unitPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        params.put("item_id", id);
        params.put("item_name", name);
        params.put("category", category);
        params.put("description", description);
        params.put("quantity", quantity);
        params.put("unit_price", unitPrice);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("item_id", id);
            jsonObject.put("item_name", name);
            jsonObject.put("category", category);
            jsonObject.put("description", description);
            jsonObject.put("quantity", quantity);
            jsonObject.put("unit_price", unitPrice);
            jsonObject.put("image", imgSrc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getPrice() { return unitPrice; }

    public void setPrice(String unitPrice) {this.unitPrice = unitPrice; }

    public void setImgSrc(String src) { imgSrc = src; }

    public String getImgSrc() { return imgSrc; }
}
