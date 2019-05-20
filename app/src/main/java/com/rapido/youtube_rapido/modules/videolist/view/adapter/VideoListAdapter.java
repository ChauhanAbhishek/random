package com.rapido.youtube_rapido.modules.videolist.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapido.youtube_rapido.R;
import com.rapido.youtube_rapido.model.response.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items = new ArrayList<>();
    private Picasso picasso;

    public VideoListAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    public void updateList(List<Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_item, parent, false);
        return new VideoItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VideoItemViewHolder) {
            ((VideoItemViewHolder) holder).populateView(items.get(position));
        }
        if(holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).populateView(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class VideoItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivVideo;

        VideoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivVideo = itemView.findViewById(R.id.iv_video);
        }

        void populateView(Item item) {
            tvTitle.setText(item.getSnippet().getTitle());
            picasso.load(item.getSnippet().getThumbnails().getStandard().getUrl())
                    .into(ivVideo);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        void populateView(Item item) {
            tvTitle.setText(item.getSnippet().getTitle());
        }
    }
}
