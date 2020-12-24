package com.example.shop_application.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shop_application.cart.CartViewModel;
import com.example.shop_application.history.HistoryFragment;
import com.example.shop_application.R;
import com.example.shop_application.cart.CartFragment;
import com.example.shop_application.history.HistoryViewModel;
import com.example.shop_application.store.StoreFragment;
import com.example.shop_application.store.StoreViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// https://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
// https://imstudio.medium.com/android-8-cleartext-http-traffic-not-permitted-73c1c9e3b803
// https://stackoverflow.com/questions/54643379/proper-implementation-of-viewpager2-in-android

// TODO: Add pagination for each tab.
// TODO: Add images for each item.
public class MainActivity extends AppCompatActivity {
    //ListView listView;
    //InventoryViewModel viewModel;
    MainTabAdapter mainTabAdapter;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    private StoreViewModel storeViewModel;
    private CartViewModel cartViewModel;
    private HistoryViewModel historyViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteDatabase("user_database");
        setContentView(R.layout.tab_layout);
        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        StoreFragment storeFragment = new StoreFragment();
        CartFragment cartFragment = new CartFragment();
        HistoryFragment historyFragment = new HistoryFragment();
        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager(), getLifecycle());
        mainTabAdapter.addFragment(storeFragment, "Store");
        mainTabAdapter.addFragment(cartFragment, "Cart");
        mainTabAdapter.addFragment(historyFragment, "History");
        viewPager.setAdapter(mainTabAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(mainTabAdapter.getTitles().get(position))).attach();
    }
}
