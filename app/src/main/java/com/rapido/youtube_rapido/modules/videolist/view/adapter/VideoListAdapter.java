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

    private static int NORMAL=0;
    private static int FOOTER=1;


    public VideoListAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    public void updateList(List<Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(Item item)
    {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void removeItem()
    {
        if(items.size()>0&&items.get(items.size()-1).getId()==null)
        this.items.remove(items.size()-1);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==NORMAL)
        {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_item, parent, false);
            return new VideoItemViewHolder(itemView);
        }
        else
        {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            return new FooterViewHolder(itemView);
        }

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
//            if(item.getSnippet().getThumbnails().getStandard().getUrl()!=null)
//            {
//                picasso.load(item.getSnippet().getThumbnails().getStandard().getUrl())
//                        .into(ivVideo);
//            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView loadingText;

        FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingText = itemView.findViewById(R.id.loading_text);
        }

        void populateView(Item item) {
            //tvTitle.setText(item.getSnippet().getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(items.get(position).getId()!=null)
            return NORMAL;
        else
            return FOOTER;
    }
}
