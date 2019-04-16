package pl.jpcodetask.xkcdcomics.data.source;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

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
        /*return mRemoteDataSource.getComic(comicNumber)
                .map(ComicWrapper::from)
                .onErrorResumeNext(new Function<Throwable, MaybeSource<? extends ComicWrapper>>() {
                    @Override
                    public MaybeSource<? extends ComicWrapper> apply(Throwable throwable) throws Exception {
                        return Maybe.just(ComicWrapper.from(throwable));
                    }
                }
    );*/
        return Maybe.concatArray(
                getComicFromDb(comicNumber),
                getComicFromApi(comicNumber))
                .doOnError(throwable -> {
                    Log.wtf("Repo", throwable);
                })
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> {
                    return Maybe.just(ComicWrapper.from(throwable)).toFlowable();
                }).firstElement();
    }

    private Maybe<Comic> getComicFromDb(int comicNumber) {
        return mLocalDataSource.getComic(comicNumber).doOnSuccess(comic -> {
            Log.e("Repo", "From db " + comic.getTitle());
        });
    }

    private Maybe<Comic> getComicFromApi(int comicNumber) {
        return mRemoteDataSource.getComic(comicNumber)
                .doOnError(throwable -> {
                    Log.wtf("Repo", throwable);
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
        return null;
    }

    @Override
    public Completable updateComic(@NonNull Comic comic) {
        return null;
    }
}
