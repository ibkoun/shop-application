package com.example.shop_application.async;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// https://stackoverflow.com/questions/58767733/android-asynctask-api-deprecating-in-android-11-what-are-the-alternatives
/**
 * Execute tasks asynchronously.
 */
public class TaskRunner {
    private final Executor executor = new ThreadPoolExecutor(5, 128, 1,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    //private final Executor executor = Executors.newCachedThreadPool();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<T> {
        void onComplete(T result) throws JSONException;
    }

    public <T> void executeAsync(Callable<T> callable, Callback<T> callback) {
        executor.execute(() -> {
            final T result;
            try {
                result = callable.call();
                handler.post(() -> {
                    try {
                        callback.onComplete(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
