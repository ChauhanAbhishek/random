package com.rapido.youtube_rapido.app.di.component;

import com.rapido.youtube_rapido.app.PrefUtils;
import com.rapido.youtube_rapido.app.di.annotations.ApplicationScope;
import com.rapido.youtube_rapido.app.di.modules.DataModule;
import com.rapido.youtube_rapido.app.di.modules.NetworkModule;
import com.rapido.youtube_rapido.app.di.modules.RepositoryModule;
import com.rapido.youtube_rapido.app.repositories.VideoRepository;
import com.rapido.youtube_rapido.app.service.ApiService;
import com.squareup.picasso.Picasso;

import dagger.Component;



@ApplicationScope
@Component(modules = {NetworkModule.class, DataModule.class, RepositoryModule.class})
public interface ApplicationComponent {
    ApiService apiService();
    PrefUtils prefUtils();
    Picasso picasso();
    VideoRepository videoRepository();
}
