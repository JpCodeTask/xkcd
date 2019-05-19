package pl.jpcodetask.xkcdcomics.data.source.remote;

import android.util.Log;

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
        return mApi.comicItem(comicNumber)
                .doOnError(throwable -> {
                    Log.wtf("Remote", throwable);
                });
    }

    @Override
    public Maybe<Comic> getLatestComic() {
        return mApi.latestComicItem();
    }

    @Override
    public Completable saveComic(@NonNull Comic comic) {
        throw new UnsupportedOperationException("Remote data source does not support save operation.");
    }

    @Override
    public Completable setFavorite(int comicNumber, boolean isFavorite) {
        throw new UnsupportedOperationException("Remote data source does not support update operation.");
    }
}
