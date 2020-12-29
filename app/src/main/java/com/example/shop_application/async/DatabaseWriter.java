package com.example.shop_application.async;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
// https://www.baeldung.com/httpurlconnection-post
/**
 * Asynchronous writing to a database.
 */
public class DatabaseWriter {
    public static Callable<String> write(String link, JSONArray jsonArray) {
        return () -> {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                byte[] input = jsonArray.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
                outputStream.flush();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    stringBuilder.append(json).append("\n");
                }
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Callable<String> write(String link, JSONObject jsonObject) {
        return () -> {
            try {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
                outputStream.flush();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    stringBuilder.append(json).append("\n");
                }
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                return null;
            }
        };
    }
}
