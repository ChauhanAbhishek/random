package com.rapido.youtube_rapido.app.VideoList;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.rapido.youtube_rapido.app.Event;
import com.rapido.youtube_rapido.app.ViewModelNonUIChangesListener;
import com.rapido.youtube_rapido.app.YoutubeApplication;
import com.rapido.youtube_rapido.app.repositories.VideoRepository;
import com.rapido.youtube_rapido.model.response.Item;

import java.util.List;


public class VideoListViewModel extends ViewModel implements ViewModelNonUIChangesListener {

    private VideoRepository videoRepository;
    private LiveData<List<Item>> mObservableVideoList;
    private  String nextPageToken;
    private  LiveData<Event<String>> toastMessage;

    MutableLiveData<Boolean> isReqSent;

    public VideoListViewModel()
    {
        videoRepository = YoutubeApplication.getInstance().getApplicationComponent().videoRepository();
        mObservableVideoList =  Transformations.map(videoRepository.getObservableVideoList(), videos -> getVideos(videos));
        videoRepository.setViewModelNonUIChangesListener(this);
        isReqSent = new MutableLiveData<>();
        toastMessage = Transformations.map(videoRepository.getToastMessage(), toastMessage -> toastmsg(toastMessage));
    }

    public LiveData<Boolean> getIsReqSent() {
        return isReqSent;
    }


    public void getVideos()
    {
        //Log.d("cnrr",nextPageToken.getValue());

        if(isReqSent.getValue()==null)
        {
            isReqSent.setValue(false);
        }

        if(!isReqSent.getValue())
        {
            videoRepository.getVideos(nextPageToken);
            isReqSent.setValue(true);
        }
    }

    public LiveData<List<Item>> getmObservableVideoList()
    {
        return mObservableVideoList;
    }

    public List<Item> getVideos(List<Item> videoList)
    {
        isReqSent.setValue(false);
        return videoList;
    }

    public Event<String> toastmsg(Event<String> toastMessage)
    {
        return toastMessage;
    }



    @Override
    public void setPurchaseToken(String nextPageToken)
    {
       this.nextPageToken=nextPageToken;
    }

    public LiveData<Event<String>> getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(MutableLiveData<Event<String>> toastMessage) {
        this.toastMessage = toastMessage;
    }

    @Override
    public void errorOccurred()
    {
        isReqSent.setValue(false);
    }


}
