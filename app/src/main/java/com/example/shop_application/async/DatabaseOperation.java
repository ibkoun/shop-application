package com.example.shop_application.async;

import org.json.JSONArray;

public interface DatabaseOperation {
    void loadData(String link); // Read from a database.
    void saveData(String link, JSONArray data, String operator); // Write into a database.
    void deleteData(String link, JSONArray data); // Delete one or more records from a database.
}
