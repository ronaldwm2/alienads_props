package com.marketingapps.ui.news;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.marketingapps.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> list = new ArrayList<>();

    public NewsAdapter() {

    }

    private OnItemClickListener listener;

    public void setData(List<String> data) {
        this.list = data;
        Log.d("debug lca", String.valueOf(this.list.size()));
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.listener = onClickListener;
    }

    public class CampaignViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final TextView tv_description;
        private final LinearLayout layout;
        public CampaignViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.categorylayout);
            tv_title = view.findViewById(R.id.tv_title);
            tv_description = view.findViewById(R.id.tv_description);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_news, viewGroup, false);

        CampaignViewHolder cvh = new CampaignViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "clicked");
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
