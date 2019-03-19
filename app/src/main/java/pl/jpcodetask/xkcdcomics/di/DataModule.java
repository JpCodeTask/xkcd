package pl.jpcodetask.xkcdcomics.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.data.source.Repository;
import pl.jpcodetask.xkcdcomics.data.source.local.LocalDataSourceImpl;
import pl.jpcodetask.xkcdcomics.data.source.remote.RemoteDataSourceImpl;

@Module
class DataModule {

   /* @Singleton
    @Provides
    ComicDatabase provideDatabase(@Named("application_context") Context context){
        return Room.databaseBuilder(context, ComicDatabase.class, "database").build();
    }

    @Singleton
    @Provides
    ComicDao provideComicDao(ComicDatabase comicDatabase){
        return comicDatabase.getComicDao();
    }*/

    @Singleton
    @Provides
    @Named("remote_data_source")
    DataSource provideRemoteDataSource(){
        return new RemoteDataSourceImpl();
    }

    @Singleton
    @Provides
    @Named("local_data_source")
    DataSource provideLocalDataSource(){
        return new LocalDataSourceImpl();
    }

    @Singleton
    @Provides
    @Named("repository")
    DataSource provideRepository(@Named("local_data_source") DataSource localDataSource, @Named("remote_data_source") DataSource remoteDataSource){
        return new Repository(localDataSource, remoteDataSource);
    }


}
