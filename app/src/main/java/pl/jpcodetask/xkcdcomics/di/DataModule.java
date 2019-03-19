package pl.jpcodetask.xkcdcomics.di;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.data.source.Repository;
import pl.jpcodetask.xkcdcomics.data.source.local.ComicDao;
import pl.jpcodetask.xkcdcomics.data.source.local.ComicDatabase;
import pl.jpcodetask.xkcdcomics.data.source.local.LocalDataSourceImpl;
import pl.jpcodetask.xkcdcomics.data.source.remote.RemoteDataSourceImpl;

@Module
public class DataModule {

    @Singleton
    @Provides
    ComicDatabase provideDatabase(@Named("application_context") Context context){
        return Room.databaseBuilder(context, ComicDatabase.class, "database").build();
    }

    @Singleton
    @Provides
    ComicDao provideComicDao(ComicDatabase comicDatabase){
        return comicDatabase.getComicDao();
    }

    @Singleton
    @Provides
    @Named("remote_data_source")
    DataSource provideRemoteDataSource(){
        return new RemoteDataSourceImpl();
    }

    @Singleton
    @Provides
    @Named("local_data_source")
    DataSource provideLocalDataSource(ComicDao comicDao){
        return new LocalDataSourceImpl(comicDao);
    }

    @Singleton
    @Provides
    @Named("repository")
    DataSource provideRepository(@Named("local_data_source") DataSource localDataSource, @Named("remote_data_source") DataSource remoteDataSource){
        return new Repository(localDataSource, remoteDataSource);
    }

}
