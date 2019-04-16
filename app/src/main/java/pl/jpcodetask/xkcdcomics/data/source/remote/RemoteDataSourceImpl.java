package pl.jpcodetask.xkcdcomics.data.source.remote;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class RemoteDataSourceImpl implements DataSource {

    private final XkcdApi mApi;

    public RemoteDataSourceImpl(XkcdApi api) {
        mApi = api;
    }


    @Override
    public Single<ComicWrapper> getComic(int comicNumber) {
        return mApi.comicItem(comicNumber)
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> Single.just(ComicWrapper.from(throwable)));
    }

    @Override
    public Single<ComicWrapper> getLatestComic() {
        return mApi.latestComicItem()
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> Single.just(ComicWrapper.from(throwable)));
    }

    @Override
    public Completable saveComic(@NonNull Comic comic) {
        throw new UnsupportedOperationException("Remote data source does not support save operation.");
    }

    @Override
    public Completable updateComic(@NonNull Comic comic) {
        throw new UnsupportedOperationException("Remote data source does not support update operation.");
    }
}
