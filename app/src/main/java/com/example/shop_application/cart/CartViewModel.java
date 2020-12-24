package com.example.shop_application.cart;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shop_application.async.DatabaseOperation;
import com.example.shop_application.async.DatabaseReader;
import com.example.shop_application.async.DatabaseWriter;
import com.example.shop_application.enums.Link;
import com.example.shop_application.item.ItemModel;
import com.example.shop_application.async.TaskRunner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the data read from the 'shopping_cart' in the database.
 */
public class CartViewModel extends AndroidViewModel implements DatabaseOperation {
    private final TaskRunner taskRunner = new TaskRunner();
    private final MutableLiveData<Map<Integer, ItemModel>> items = new MutableLiveData<>();

    public CartViewModel(Application application) {
        super(application);
        loadData(Link.CART.getURL());
    }

    public LiveData<Map<Integer, ItemModel>> getItems(String link) {
        if (items.getValue()  == null) {
            loadData(link);
        }
        return items;
    }

    @Override
    public void loadData(String link) {
        taskRunner.executeAsync(new DatabaseReader(link), (json) -> {
            JSONArray jsonArray = new JSONArray(json);
            Map<Integer, ItemModel> cart = new HashMap<>();
            int id;
            ItemModel item;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                id = Integer.parseInt(obj.getString("item_id"));
                item = new ItemModel(obj.getString("item_id"),
                        obj.getString("item_name"),
                        obj.getString("category"),
                        obj.getString("description"),
                        obj.getString("quantity"),
                        obj.getString("unit_price"));
                cart.put(id, item);
            }
            items.setValue(cart);
        });
    }

    @Override
    public void saveData(String link, JSONArray data, String operator) {
        try {
            JSONObject jsonObject;
            for (int i = 0; i < data.length(); i++) {
                jsonObject = (JSONObject)data.get(i);
                jsonObject.put("operator", operator);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        taskRunner.executeAsync(DatabaseWriter.write(link, data), (json) -> {
            JSONArray jsonArray = new JSONArray(json);
            Map<Integer, ItemModel> cart = this.items.getValue();
            if (cart != null) {
                int id;
                ItemModel item;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    id = Integer.parseInt(obj.getString("item_id"));
                    if (cart.containsKey(id)) {
                        item = cart.get(id);
                        if (item != null) item.setQuantity(obj.getString("quantity"));
                    }
                    else {
                        item = new ItemModel(obj.getString("item_id"),
                                obj.getString("item_name"),
                                obj.getString("category"),
                                obj.getString("description"),
                                obj.getString("quantity"),
                                obj.getString("unit_price"));
                        cart.put(id, item);
                    }
                }
                this.items.setValue(cart);
            }
        });
    }

    @Override
    public void deleteData(String link, JSONArray jsonArray) {
        taskRunner.executeAsync(DatabaseWriter.write(link, jsonArray), (json) -> {
            JSONArray jsonList = new JSONArray(json);
            Map<Integer, ItemModel> cart = items.getValue();
            if (cart != null) {
                int id;
                for (int i = 0; i < jsonList.length(); i++) {
                    JSONObject obj = jsonList.getJSONObject(i);
                    id = Integer.parseInt(obj.getString("item_id"));
                    cart.remove(id);
                }
                items.setValue(cart);
            }
        });
    }

    public JSONArray getJsonArray() {
        Map<Integer, ItemModel> cart = items.getValue();
        if (cart != null) {
            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject;
                for (ItemModel item : cart.values()) {
                    jsonObject = item.getJsonObject();
                    // TODO: Get the user's id.
                    jsonObject.put("user_id", "1");
                    jsonArray.put(jsonObject);
                }
                return jsonArray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONArray();
    }

    public double getTotalPrice() {
        double result = 0;
        Map<Integer, ItemModel> cart = items.getValue();
        if (cart != null) {
            for (ItemModel item : cart.values()) {
                result += Double.parseDouble(item.getPrice()) * Double.parseDouble(item.getQuantity());
            }
        }
        return result;
    }
}
