package com.example.shop_application.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_application.R;
import com.example.shop_application.enums.Link;

import java.util.ArrayList;

// TODO: Add options to archive or delete a history.
public class HistoryFragment extends Fragment {
    private HistoryAdapter historyAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        recyclerView = view.findViewById(R.id.history_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HistoryViewModel historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        historyAdapter = new HistoryAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(historyAdapter);
        historyViewModel.getItems(Link.HISTORY.getURL()).observe(getViewLifecycleOwner(), orders -> {
            historyAdapter.setHistory(new ArrayList<>(orders.values()));
            TextView emptyHistoryView= view.findViewById(R.id.empty_history_msg);
            if (orders.values().isEmpty()) {
                emptyHistoryView.setVisibility(View.VISIBLE);
            }
            else {
                emptyHistoryView.setVisibility(View.GONE);
            }
        });
    }
}
