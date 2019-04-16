package pl.jpcodetask.xkcdcomics.data.source.remote;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class RemoteDataSourceImpl implements DataSource {

    private final XkcdApi mApi;

    public RemoteDataSourceImpl(XkcdApi api) {
        mApi = api;
    }


    @Override
    public Maybe<Comic> getComic(int comicNumber) {
        return null;
        /*return mApi.comicItem(comicNumber)
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> {
                    return Maybe.just(ComicWrapper.from(throwable));
                });*/
    }

    @Override
    public Maybe<Comic> getLatestComic() {
        return mApi.latestComicItem();
        /*return mApi.latestComicItem()
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> {
                    return Maybe.just(ComicWrapper.from(throwable));
                });*/
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
