package com.rapido.youtube_rapido.app.di.modules;

import android.content.Context;

import com.rapido.youtube_rapido.app.di.annotations.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by adityachauhan on 23/06/18.
 *
 */

@Module
public class ApplicationContextModule {

    private final Context context;

    public ApplicationContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    Context context() {
        return context;
    }
}
