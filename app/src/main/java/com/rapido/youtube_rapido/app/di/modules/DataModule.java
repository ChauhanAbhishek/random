package com.rapido.youtube_rapido.app.di.modules;

import android.content.Context;

import com.rapido.youtube_rapido.app.PrefUtils;
import com.rapido.youtube_rapido.app.di.annotations.ApplicationScope;

import dagger.Module;
import dagger.Provides;



@Module(includes = {ApplicationContextModule.class})
public class DataModule {

    @Provides
    @ApplicationScope
    PrefUtils prefUtils(Context context) {
        return new PrefUtils(context);
    }
}
