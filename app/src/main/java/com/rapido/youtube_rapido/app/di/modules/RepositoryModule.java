package com.rapido.youtube_rapido.app.di.modules;


import com.rapido.youtube_rapido.app.di.annotations.ApplicationScope;
import com.rapido.youtube_rapido.app.repositories.VideoRepository;
import com.rapido.youtube_rapido.app.service.ApiService;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
@Module
public class RepositoryModule {

    @Provides
    @ApplicationScope
    public VideoRepository repositoryModule(ApiService apiService)
    {
        return new VideoRepository(apiService);
    }
}
