package com.rapido.youtube_rapido.modules.videolist.view.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingMethod;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rapido.youtube_rapido.R;
import com.rapido.youtube_rapido.Utils;
import com.rapido.youtube_rapido.app.VideoList.VideoListViewModel;
import com.rapido.youtube_rapido.app.VideoPlayer.VideoPlayerActivity;
import com.rapido.youtube_rapido.databinding.ItemVideoItemBinding;
import com.rapido.youtube_rapido.model.response.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items = new ArrayList<>();
    private Context context;
    private VideoListViewModel videoModel;

    private static int NORMAL=0;
    private static int FOOTER=1;


    public VideoListAdapter(VideoListViewModel videoModel, Context context) {
        this.videoModel=videoModel;
        this.context = context;
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
            ItemVideoItemBinding parentCommentItemBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_video_item,
                            parent, false);
            return new VideoItemViewHolder(parentCommentItemBinding);
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
            ((VideoItemViewHolder) holder).getItemVideoItemBinding().setItem(items.get(position));
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

        ItemVideoItemBinding itemVideoItemBinding;

        VideoItemViewHolder(@NonNull ItemVideoItemBinding itemVideoItemBinding) {
            super(itemVideoItemBinding.getRoot());
            this.itemVideoItemBinding=itemVideoItemBinding;
        }

        public ItemVideoItemBinding getItemVideoItemBinding() {
            return itemVideoItemBinding;
        }



        void populateView(Item item) {
           // tvTitle.setText(item.getSnippet().getTitle());
//            if(item.getId()!=null&&item.getSnippet().getThumbnails().getStandard()!=null)
//            {
//                picasso.load(item.getSnippet().getThumbnails().getStandard().getUrl())
//                        .into(itemVideoItemBinding.ivVideo);
         //   }

            itemVideoItemBinding.videoItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("cnrc","clicked");
                    videoModel.setPlayerVisibility(false);

                    Intent i = new Intent(context, VideoPlayerActivity.class);
                    i.putExtra("item_object", item);
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation((Activity) context, itemVideoItemBinding.ivVideo, "robot");
                    ((Activity) context).startActivityForResult(i, Utils.PLAYER_START_REQ,options.toBundle());
                }

            });
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
