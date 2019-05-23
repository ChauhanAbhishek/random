package com.rapido.youtube_rapido.app.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rapido.youtube_rapido.app.Event;
import com.rapido.youtube_rapido.app.ViewModelNonUIChangesListener;
import com.rapido.youtube_rapido.app.service.ApiService;
import com.rapido.youtube_rapido.model.response.Item;
import com.rapido.youtube_rapido.model.response.VideoResponse;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class VideoRepository
{
    private ApiService mApiService;
    private MutableLiveData<List<Item>> mObservableVideoList;

    private ViewModelNonUIChangesListener viewModelNonUIChangesListener;

    private  MutableLiveData<Event<String>> toastMessage;


    private  MutableLiveData<Boolean> mIsDataLoadingError;



    public  VideoRepository(ApiService apiService)
    {
        mApiService=apiService;
        mObservableVideoList = new MutableLiveData<>();
        mIsDataLoadingError = new MutableLiveData<>();
        toastMessage = new MutableLiveData<>();
    }

    public LiveData<Boolean> getmIsDataLoadingError() {
        return mIsDataLoadingError;
    }


    public void setViewModelNonUIChangesListener(ViewModelNonUIChangesListener viewModelNonUIChangesListener)
    {
        this.viewModelNonUIChangesListener = viewModelNonUIChangesListener;
    }

    public void getVideos(String pageToken)
    {
        Single<Response<VideoResponse>> videoResponse;

        if(pageToken==null)
        {
            videoResponse = mApiService.getVideos("snippet",
                    "mostPopular",
                    "IN",
                    "AIzaSyB0uZhLck6cUIMJaBLKHqhmiI9OKDXdTGw",
                    10);
        }
        else
        {
            videoResponse = mApiService.getVideos("snippet",
                    "mostPopular",
                    "IN",
                    "AIzaSyB0uZhLck6cUIMJaBLKHqhmiI9OKDXdTGw",
                    10,pageToken);
        }

        videoResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<VideoResponse>>() {
                    @Override
                    public void onSuccess(Response<VideoResponse> response) {
                        if(response.isSuccessful()) {
                            //videoListAdapter.updateList(response.body().getItems());
                            mObservableVideoList.setValue(response.body().getItems());
                            viewModelNonUIChangesListener.setPurchaseToken(response.body().getNextPageToken());
                            Log.d("cnrr",response.toString() + response.body().getNextPageToken());
                        } else {
                            //todo handle errors
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //todo handle errors
                        viewModelNonUIChangesListener.errorOccurred();
                        toastMessage.setValue(new Event<>("Something has gone wrong"));
                    }
                });
    }

    public LiveData<Event<String>> getToastMessage() {
        return toastMessage;
    }


    public LiveData<List<Item>> getObservableVideoList()
    {
        return mObservableVideoList;
    }




}
