package com.marketingapps.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marketingapps.R;
import com.marketingapps.databinding.FragmentCampaignBinding;
import com.marketingapps.ui.detailcampaign.CampaignDetail;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentCampaignBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCampaignBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        ListCampaignAdapter lcAdapter = new ListCampaignAdapter();
        lcAdapter.setOnClickListener(new ListCampaignAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), CampaignDetail.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(lcAdapter);
        List<String> strList = new ArrayList<>();
        strList.add("a");
        strList.add("b");
        strList.add("a");
        strList.add("b");
        strList.add("a");
        strList.add("b");
        strList.add("a");
        strList.add("b");
        Log.d("debug lca", "woi");
        lcAdapter.setData(strList);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}