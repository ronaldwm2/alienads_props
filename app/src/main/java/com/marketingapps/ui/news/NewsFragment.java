package com.marketingapps.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marketingapps.R;
import com.marketingapps.databinding.FragmentNewsBinding;
import com.marketingapps.ui.detailcampaign.CampaignDetail;
import com.marketingapps.ui.home.ListCampaignAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        NewsAdapter newsAdapter = new NewsAdapter();
        newsAdapter.setOnClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), CampaignDetail.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(newsAdapter);
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
        newsAdapter.setData(strList);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}