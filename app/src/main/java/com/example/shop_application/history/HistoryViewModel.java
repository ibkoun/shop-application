package com.example.shop_application.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shop_application.async.DatabaseOperation;
import com.example.shop_application.async.DatabaseReader;
import com.example.shop_application.async.DatabaseWriter;
import com.example.shop_application.async.TaskRunner;
import com.example.shop_application.enums.Link;
import com.example.shop_application.item.ItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the data read from the 'orders' and 'order_items' tables in the database.
 */
public class HistoryViewModel extends AndroidViewModel implements DatabaseOperation {
    private final TaskRunner taskRunner = new TaskRunner();
    private final MutableLiveData<Map<Integer, OrderModel>> orders = new MutableLiveData<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        loadData(Link.HISTORY.getURL());
    }

    public LiveData<Map<Integer, OrderModel>> getItems(String link) {
        if (orders.getValue()  == null) {
            loadData(link);
        }
        return orders;
    }

    @Override
    public void loadData(String link) {
        taskRunner.executeAsync(new DatabaseReader(link), (json) -> {
            JSONArray jsonArray = new JSONArray(json);
            Map<Integer, OrderModel> history = new HashMap<>();
            int orderId;
            OrderModel order;
            ItemModel item;
            JSONObject jsonItem;
            List<ItemModel> items;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                orderId = Integer.parseInt(obj.getString("order_id"));
                JSONArray jsonItems = obj.getJSONArray("order_items");
                items = new ArrayList<>();
                for (int j = 0; j < jsonItems.length(); j++) {
                    jsonItem = jsonItems.getJSONObject(j);
                    item = new ItemModel(jsonItem.getString("item_id"),
                            jsonItem.getString("item_name"),
                            jsonItem.getString("category"),
                            jsonItem.getString("description"),
                            jsonItem.getString("quantity"),
                            jsonItem.getString("unit_price"));
                    items.add(item);
                }
                order = new OrderModel(obj.getString("purchase_date"),
                        items);
                order.setId(obj.getString("order_id"));
                history.put(orderId, order);
            }
            orders.setValue(history);
        });
    }

    // TODO: Change this method's signature.
    @Override
    public void saveData(String link, JSONArray data, String operator) {
        taskRunner.executeAsync(DatabaseWriter.write(link, data), (json) -> {
            JSONArray jsonArray = new JSONArray(json);
            Map<Integer, OrderModel> history = orders.getValue();
            if (history != null) {
                int orderId;
                OrderModel order;
                ItemModel item;
                JSONObject jsonItem;
                List<ItemModel> items;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    orderId = Integer.parseInt(obj.getString("order_id"));
                    JSONArray jsonItems = obj.getJSONArray("order_items");
                    items = new ArrayList<>();
                    for (int j = 0; j < jsonItems.length(); j++) {
                        jsonItem = jsonItems.getJSONObject(j);
                        item = new ItemModel(jsonItem.getString("item_id"),
                                jsonItem.getString("item_name"),
                                jsonItem.getString("category"),
                                jsonItem.getString("description"),
                                jsonItem.getString("quantity"),
                                jsonItem.getString("unit_price"));
                        items.add(item);
                    }
                    order = new OrderModel(obj.getString("purchase_date"),
                            items);
                    order.setId(obj.getString("order_id"));
                    history.put(orderId, order);
                }
                orders.setValue(history);
            }
        });
    }

    @Override
    public void deleteData(String link, JSONArray jsonArray) {
        taskRunner.executeAsync(DatabaseWriter.write(link, jsonArray), (json) -> {

        });
    }
}
