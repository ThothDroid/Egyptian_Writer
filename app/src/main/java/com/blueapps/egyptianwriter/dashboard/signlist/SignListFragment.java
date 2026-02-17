package com.blueapps.egyptianwriter.dashboard.signlist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blueapps.egyptianwriter.databinding.FragmentSignListBinding;
import com.blueapps.egyptianwriter.layoutadapter.GridAdapter;
import com.blueapps.signprovider.SignProvider;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.Unit;

public class SignListFragment extends Fragment {

    private FragmentSignListBinding binding;

    private RecyclerView recyclerView;
    private ArrayList<SignData> recyclerDataArrayList = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private GridLayoutManager layoutManager;

    private SignProvider signProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // binding
        binding = FragmentSignListBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Set names for Views
        recyclerView = binding.signGrid;

        // init SignProvider
        signProvider = new SignProvider(getContext());

        // added data from arraylist to adapter class.
        layoutManager = new GridLayoutManager(getContext(), 4);
        new GridAdapter(getContext(), recyclerView, 100, layoutManager);
        adapter = new RecyclerViewAdapter(recyclerDataArrayList, getActivity(), signProvider, layoutManager, 50);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init RecyclerView

        // add data to array list
        try {
            ArrayList<String> rawSigns = signProvider.getAllSigns();

            int counter = 0;
            for (String sign: rawSigns){
                SignData signData = new SignData(counter, sign);
                recyclerDataArrayList.add(signData);
                counter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Error Handling
        }

        // at last set adapter to recycler view.
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.shutdown();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.start(1);
    }
}