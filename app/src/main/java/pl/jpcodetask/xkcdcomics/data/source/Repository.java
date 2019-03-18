package pl.jpcodetask.xkcdcomics.data.source;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import pl.jpcodetask.xkcdcomics.data.model.Comic;

@Singleton
public class Repository implements DataSource {

    private final DataSource mLocalDataSource;
    private final DataSource mRemoteDataSource;

    @Inject
    public Repository(@Named("local_data_source") DataSource localDataSource, @Named("remote_data_source") DataSource remoteDataSource){
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public List<Comic> getData() {
        return mLocalDataSource.getData();
    }

    @Override
    public Comic getComic(int comicNumber) {
        return mLocalDataSource.getComic(comicNumber);
    }
}
