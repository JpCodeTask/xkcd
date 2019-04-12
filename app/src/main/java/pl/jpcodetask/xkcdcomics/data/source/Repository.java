package pl.jpcodetask.xkcdcomics.data.source;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;

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
    public Single<ComicWrapper> getComic(int comicNumber) {
        return mRemoteDataSource.getComic(comicNumber);
    }

    @Override
    public Single<ComicWrapper> getLatestComic() {
        return mRemoteDataSource.getLatestComic();
    }
}
