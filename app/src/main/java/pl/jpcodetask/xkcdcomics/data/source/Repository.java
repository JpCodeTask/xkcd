package pl.jpcodetask.xkcdcomics.data.source;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
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
    public Maybe<ComicWrapper> getComic(int comicNumber) {
        return mRemoteDataSource.getComic(comicNumber);
    }

    @Override
    public Maybe<ComicWrapper> getLatestComic() {
        return mRemoteDataSource.getLatestComic();
    }

    @Override
    public Completable saveComic(@NonNull Comic comic) {
        return null;
    }

    @Override
    public Completable updateComic(@NonNull Comic comic) {
        return null;
    }
}
