package com.example.shop_application.async;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
// https://stackoverflow.com/questions/29763358/best-way-to-create-configuration-fileconfig-php-php

/**
 * Asynchronous reading of a database.
 */
public class DatabaseReader implements Callable<String> {
    private final String link;

    public DatabaseReader(String link) {
        this.link = link;
    }

    @Override
    public String call() {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                stringBuilder.append(json).append("\n");
            }
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            return null;
        }
    }
}
