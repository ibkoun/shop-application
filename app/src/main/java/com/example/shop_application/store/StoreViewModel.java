package com.example.shop_application.store;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shop_application.async.DatabaseOperation;
import com.example.shop_application.async.DatabaseReader;
import com.example.shop_application.async.DatabaseWriter;
import com.example.shop_application.item.ItemModel;
import com.example.shop_application.async.TaskRunner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// https://medium.com/@taman.neupane/basic-example-of-livedata-and-viewmodel-14d5af922d0
// https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
/**
 * Contains the data read from the 'store' table in the database.
 */
public class StoreViewModel extends AndroidViewModel implements DatabaseOperation {
    private final TaskRunner taskRunner = new TaskRunner();
    private final MutableLiveData<Map<Integer, ItemModel>> items = new MutableLiveData<>();

    public StoreViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Map<Integer, ItemModel>> getItems(String link) {
        if (items.getValue() == null) {
            loadData(link);
        }
        return items;
    }

    @Override
    public void loadData(String link) {
        taskRunner.executeAsync(new DatabaseReader(link), (json) -> {
            JSONArray jsonArray = new JSONArray(json);
            Map<Integer, ItemModel> store = new HashMap<>();
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
                item.setImgSrc(obj.getString("image"));
                store.put(id, item);
            }
            items.setValue(store);
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
            JSONArray jsonList = new JSONArray(json);
            Map<Integer, ItemModel> store = items.getValue();
            if (store != null) {
                int id;
                ItemModel item;
                for (int i = 0; i < jsonList.length(); i++) {
                    JSONObject obj = jsonList.getJSONObject(i);
                    id = Integer.parseInt(obj.getString("item_id"));
                    if (store.containsKey(id)) {
                        item = store.get(id);
                        if (item != null) item.setQuantity(obj.getString("quantity"));
                    }
                }
                items.setValue(store);
            }
        });
    }

    @Override
    public void deleteData(String link, JSONArray data) {

    }
}
