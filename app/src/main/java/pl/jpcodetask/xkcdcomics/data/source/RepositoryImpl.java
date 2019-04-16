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
public class RepositoryImpl implements Repository {

    private final DataSource mLocalDataSource;
    private final DataSource mRemoteDataSource;

    @Inject
    public RepositoryImpl(@Named("local_data_source") DataSource localDataSource, @Named("remote_data_source") DataSource remoteDataSource){
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }


    @Override
    public Maybe<ComicWrapper> getComic(int comicNumber) {
        return null;
    }

    @Override
    public Maybe<ComicWrapper> getLatestComic() {
        return mRemoteDataSource.getLatestComic()
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> {
                    return Maybe.just(ComicWrapper.from(throwable));
                });
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
