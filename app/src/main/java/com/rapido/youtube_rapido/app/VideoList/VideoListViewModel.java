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
    private  MutableLiveData<Event<Boolean>> mPlayerVisibility ;


    boolean isReqSent=false;

    MutableLiveData<Event<Boolean>> isReqSentEvent;




    public VideoListViewModel()
    {
        videoRepository = YoutubeApplication.getInstance().getApplicationComponent().videoRepository();
        mObservableVideoList =  Transformations.map(videoRepository.getObservableVideoList(), videos -> getVideos(videos));
        videoRepository.setViewModelNonUIChangesListener(this);
        isReqSentEvent = new MutableLiveData<>();
        toastMessage = Transformations.map(videoRepository.getToastMessage(), toastMessage -> toastmsg(toastMessage));
        mPlayerVisibility  = new MutableLiveData<>();
    }

    public LiveData<Event<Boolean>> getIsReqSent() {
        return isReqSentEvent;
    }


    public LiveData<Event<Boolean>> getPlayerVisibility() {
        return mPlayerVisibility;
    }




    public void getVideos()
    {

        if(!isReqSent)
        {
            videoRepository.getVideos(nextPageToken);
            isReqSentEvent.setValue(new Event<Boolean>(true));
            isReqSent=true;
        }



    }

    public LiveData<List<Item>> getmObservableVideoList()
    {
        return mObservableVideoList;
    }

    public List<Item> getVideos(List<Item> videoList)
    {
        isReqSent=false;
        isReqSentEvent.setValue(new Event<>(false));
        if(mObservableVideoList.getValue()==null)
        {
            return videoList;
        }
        else
        {
            List<Item> items = mObservableVideoList.getValue();
            items.addAll(videoList);
            return items;
        }
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

    public void setPlayerVisibility(Boolean isVisible)
    {
        mPlayerVisibility.setValue(new Event<Boolean>(isVisible));
    }

    @Override
    public void errorOccurred()
    {
        isReqSent=false;
        isReqSentEvent.setValue(new Event<>(false));
    }


}
