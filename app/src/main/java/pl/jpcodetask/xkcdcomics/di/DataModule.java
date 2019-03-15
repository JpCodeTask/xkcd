package pl.jpcodetask.xkcdcomics.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.data.source.local.LocalDataSourceImpl;

@Module
public class DataModule {

    @Singleton
    @Provides
    @Named("local_data_source")
    DataSource provideLocalDataSource(){
        return new LocalDataSourceImpl();
    }
}
