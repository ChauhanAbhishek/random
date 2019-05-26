package com.rapido.youtube_rapido.app.VideoPlayer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.rapido.youtube_rapido.R;
import com.rapido.youtube_rapido.databinding.ActivityVideoPlayerBinding;
import com.rapido.youtube_rapido.model.response.Item;

public class VideoPlayerActivity extends YouTubeBaseActivity implements com.google.android.youtube.player.YouTubePlayer.OnInitializedListener,com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener,com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener{

    com.google.android.youtube.player.YouTubePlayerView playerView;

    String API_KEY = "AIzaSyB0uZhLck6cUIMJaBLKHqhmiI9OKDXdTGw";
    String testId = "G8PaeXJwddc" ;
    String videoId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ActivityVideoPlayerBinding activityVideoPlayerBinding = DataBindingUtil.setContentView(this,R.layout.activity_video_player);

        playerView = activityVideoPlayerBinding.playerUi.player;

        playerView.initialize(API_KEY,this);



        Intent i = getIntent();

        //change to PARCELABLE
        Item item = (Item)i.getSerializableExtra("item_object");
        videoId= item.getId();


        activityVideoPlayerBinding.playerUi.title.setText(item.getSnippet().getTitle());
        activityVideoPlayerBinding.playerUi.owner.setText(item.getSnippet().getChannelTitle());



        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.setPlayerStateChangeListener(this);
        youTubePlayer.setPlaybackEventListener(this);

        if(!b)
        {
            youTubePlayer.loadVideo(videoId);
        }
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Log.d("cnrr",errorReason.toString());
    }

    @Override
    public void onAdStarted() {

    }


    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onSeekTo(int i) {

    }
}
