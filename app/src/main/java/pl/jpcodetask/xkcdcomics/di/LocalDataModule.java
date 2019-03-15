package pl.jpcodetask.xkcdcomics.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.jpcodetask.xkcdcomics.data.DataSource;
import pl.jpcodetask.xkcdcomics.data.local.LocalDataSourceImpl;

@Module
public class LocalDataModule {

    @Singleton
    @Provides
    @Named("local_data_source")
    DataSource provideLocalDataSource(){
        return new LocalDataSourceImpl();
    }
}
