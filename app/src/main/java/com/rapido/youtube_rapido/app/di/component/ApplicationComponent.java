package com.rapido.youtube_rapido.app.di.component;

import com.rapido.youtube_rapido.app.PrefUtils;
import com.rapido.youtube_rapido.app.di.annotations.ApplicationScope;
import com.rapido.youtube_rapido.app.di.modules.DataModule;
import com.rapido.youtube_rapido.app.di.modules.NetworkModule;
import com.rapido.youtube_rapido.app.service.ApiService;
import com.squareup.picasso.Picasso;

import dagger.Component;

/**
 * Created by adityachauhan on 23/06/18.
 *
 */

@ApplicationScope
@Component(modules = {NetworkModule.class, DataModule.class})
public interface ApplicationComponent {
    ApiService apiService();
    PrefUtils prefUtils();
    Picasso picasso();
}
