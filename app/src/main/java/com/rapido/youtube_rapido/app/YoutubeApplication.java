package com.rapido.youtube_rapido.app;

import android.app.Application;
import android.os.Build;

import com.rapido.youtube_rapido.app.di.component.ApplicationComponent;
import com.rapido.youtube_rapido.app.di.component.DaggerApplicationComponent;
import com.rapido.youtube_rapido.app.di.modules.ApplicationContextModule;



public class YoutubeApplication extends Application {

    private static YoutubeApplication instance;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationContextModule(new ApplicationContextModule(this))
                .build();
    }

    public static YoutubeApplication getInstance() {
        return instance;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
