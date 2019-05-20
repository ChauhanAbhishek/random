package com.rapido.youtube_rapido.modules.videolist;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rapido.youtube_rapido.R;
import com.rapido.youtube_rapido.app.YoutubeApplication;
import com.rapido.youtube_rapido.app.service.ApiService;
import com.rapido.youtube_rapido.model.response.VideoResponse;
import com.rapido.youtube_rapido.modules.videolist.view.adapter.VideoListAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class VideoListActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView rvVideos;

    VideoListAdapter videoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = YoutubeApplication.getInstance().getApplicationComponent().apiService();

        rvVideos = findViewById(R.id.rv_videos);
        rvVideos.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));

        videoListAdapter = new VideoListAdapter(YoutubeApplication.getInstance().getApplicationComponent().picasso());

        rvVideos.setAdapter(videoListAdapter);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Popular Videos");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }

        apiService.getVideos("snippet",
                "mostPopular",
                "IN",
                "AIzaSyB0uZhLck6cUIMJaBLKHqhmiI9OKDXdTGw",
                10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<VideoResponse>>() {
                    @Override
                    public void onSuccess(Response<VideoResponse> response) {
                        if(response.isSuccessful()) {
                            videoListAdapter.updateList(response.body().getItems());
                        } else {
                            //todo handle errors
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //todo handle errors
                    }
                });
    }
}
