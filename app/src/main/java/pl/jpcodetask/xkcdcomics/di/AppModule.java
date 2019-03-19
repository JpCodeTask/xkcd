package pl.jpcodetask.xkcdcomics.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    @Named("application_context")
    Context provideApplicationContext(Application application){
        return application;
    }
}
