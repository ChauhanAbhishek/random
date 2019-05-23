package com.rapido.youtube_rapido.app.VideoList;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rapido.youtube_rapido.R;
import com.rapido.youtube_rapido.app.Event;
import com.rapido.youtube_rapido.app.YoutubeApplication;
import com.rapido.youtube_rapido.app.service.ApiService;
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

        videoListViewModel  = ViewModelProviders.of(this).get(VideoListViewModel.class);


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


}
