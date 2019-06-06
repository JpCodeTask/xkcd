package pl.jpcodetask.xkcdcomics.data.source;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

@Singleton
public class RepositoryImpl implements Repository {

    private static final String TAG = RepositoryImpl.class.getSimpleName();

    private final DataSource mLocalDataSource;
    private final DataSource mRemoteDataSource;

    @Inject
    public RepositoryImpl(@Named("local_data_source") DataSource localDataSource, @Named("remote_data_source") DataSource remoteDataSource){
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }


    @Override
    public Maybe<ComicWrapper> getComic(int comicNumber) {
        return Maybe.concatArray(
                getComicFromDb(comicNumber),
                getComicFromApi(comicNumber))
                .doOnError(throwable -> {
                    Log.e(TAG, "Error getComic", throwable);
                })
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> {
                    return Maybe.just(ComicWrapper.from(throwable)).toFlowable();
                }).firstElement();
    }

    private Maybe<Comic> getComicFromDb(int comicNumber) {
        return mLocalDataSource.getComic(comicNumber).doOnSuccess(comic -> {
            Log.i(TAG, "getComicFromDb :" + comic.getTitle());
        });
    }

    private Maybe<Comic> getComicFromApi(int comicNumber) {
        return mRemoteDataSource.getComic(comicNumber)
                .doOnError(throwable -> {
                    Log.e(TAG, "Error getComicFromApi",throwable);
                })
                .doOnSuccess(this::storeInDb);
    }

    private void storeInDb(Comic comic){
        mLocalDataSource.saveComic(comic)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
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
        return mLocalDataSource.saveComic(comic);
    }

    @Override
    public Completable setFavorite(int comicNumber, boolean isFavorite) {
        return mLocalDataSource.setFavorite(comicNumber, isFavorite);
    }

    @Override
    public Maybe<List<Comic>> getFavorites() {
        return mLocalDataSource.getFavorites();
    }
}
