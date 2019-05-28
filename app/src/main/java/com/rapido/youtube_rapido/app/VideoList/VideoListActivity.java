package com.rapido.youtube_rapido.app.VideoList;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.rapido.youtube_rapido.R;
import com.rapido.youtube_rapido.Utils;
import com.rapido.youtube_rapido.app.Event;
import com.rapido.youtube_rapido.app.VideoPlayer.VideoPlayerActivity;
import com.rapido.youtube_rapido.app.YoutubeApplication;
import com.rapido.youtube_rapido.app.service.ApiService;
import com.rapido.youtube_rapido.databinding.ActivityMainBinding;
import com.rapido.youtube_rapido.model.response.Item;
import com.rapido.youtube_rapido.model.response.VideoResponse;
import com.rapido.youtube_rapido.modules.videolist.view.adapter.VideoListAdapter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class VideoListActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView rvVideos;
    private VideoListViewModel videoListViewModel;
    VideoListAdapter videoListAdapter;
    LiveData<List<Item>> liveData;
    LiveData<Event<String>> toastMessage;
    LiveData<Boolean> isReqSent;

    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView;
    ActivityMainBinding activityMainBinding;

    ImageView playerScreenMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        apiService = YoutubeApplication.getInstance().getApplicationComponent().apiService();


        rvVideos = activityMainBinding.rvVideos;
        youTubePlayerView = activityMainBinding.youtubePlayerView;

         playerScreenMode =new ImageView(this);
        playerScreenMode.setImageResource(R.drawable.ic_close_white_36dp);
        youTubePlayerView.getPlayerUiController().addView(playerScreenMode);
        rvVideos.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));

        videoListViewModel  = ViewModelProviders.of(this).get(VideoListViewModel.class);



        videoListAdapter = new VideoListAdapter(videoListViewModel,this);

        rvVideos.setAdapter(videoListAdapter);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Popular Videos");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }

//        apiService.getVideos("snippet",
//                "mostPopular",
//                "IN",
//                "AIzaSyB0uZhLck6cUIMJaBLKHqhmiI9OKDXdTGw",
//                10)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableSingleObserver<Response<VideoResponse>>() {
//                    @Override
//                    public void onSuccess(Response<VideoResponse> response) {
//                        if(response.isSuccessful()) {
//                            videoListAdapter.updateList(response.body().getItems());
//                        } else {
//                            //todo handle errors
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //todo handle errors
//                    }
//                });



        liveData = videoListViewModel.getmObservableVideoList();
        toastMessage = videoListViewModel.getToastMessage();
        isReqSent = videoListViewModel.getIsReqSent();
        liveData.observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> videoList) {
                Log.d("cnrrr","changed" + videoList);
                if(videoList!=null)
                {
                    videoListAdapter.updateList(videoList);
                }
            }
        });

        toastMessage.observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(@Nullable Event<String> event) {
                Log.d("cnrrr","changed " + "event");
                if(event!=null)
                {
                    Toast.makeText(VideoListActivity.this,event.getContentIfNotHandled(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        isReqSent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isReqSent) {
                Log.d("cnrrr","reqsent " + isReqSent);
                if(isReqSent)
                {
                    Item item = new Item();
                    videoListAdapter.addItem(item);
                }
                else
                {
                   videoListAdapter.removeItem();
                }

            }
        });

        setUpScrollEndListener();

        videoListViewModel.getVideos();

        videoListViewModel.getPlayerVisibility().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> taskIdEvent) {
                Boolean item = taskIdEvent.getContentIfNotHandled();
                Log.d("cnrc","clicked2 " + item);

              if(youTubePlayerView!=null)
              {
                  youTubePlayerView.setVisibility(View.GONE);
              }

            }
        });

        initializePlayer();



    }

    private YouTubePlayer youTubePlayer;

    public void initializePlayer()
    {
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                VideoListActivity.this.youTubePlayer=youTubePlayer;
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                if(state==PlayerConstants.PlayerState.ENDED)
                {
                    youTubePlayerView.setVisibility(View.INVISIBLE);
                }
            }
        });

        playerScreenMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                youTubePlayer.pause();
                youTubePlayerView.setVisibility(View.INVISIBLE);

            }
        });

    }

    public void playVideo(String videoId,float seekAt)
    {
        youTubePlayer.loadVideo(videoId, seekAt);
    }


    public void setUpScrollEndListener()
    {
        rvVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    //Toast.makeText(YourActivity.this, "Last", Toast.LENGTH_LONG).show();
                    videoListViewModel.getVideos();
                }
            }
        });
    }

    String videoId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                videoId=data.getStringExtra("video_id");
                int seekTo = data.getIntExtra("seek_to",-1);

                if(seekTo!=-1)
                {
                    youTubePlayerView.setVisibility(View.VISIBLE);
                    playVideo(videoId,seekTo);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


}
