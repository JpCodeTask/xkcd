package pl.jpcodetask.xkcdcomics.usecase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.data.source.Repository;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;
import pl.jpcodetask.xkcdcomics.utils.SharedPreferenceProvider;

public class SingleComicUseCaseImpl implements SingleComicUseCase {

    private static final int NO_BOOKMARK = -1;
    private static final int DEFAULT_LATEST_COMIC_NUMBER = 1;

    private final Repository mDataSource;
    private final SharedPreferenceProvider mPreferenceProvider;

    @Inject
    public SingleComicUseCaseImpl(@Named("repository") Repository dataSource, SharedPreferenceProvider preferenceProvider) {
        mDataSource = dataSource;
        mPreferenceProvider = preferenceProvider;
    }

    @Override
    public Maybe<ComicWrapper> loadComic() {
        if (mPreferenceProvider.isFirstLaunch() || mPreferenceProvider.getBookmarkComicNumber(NO_BOOKMARK) == NO_BOOKMARK){
            return  mDataSource.getLatestComic()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.mainThread())
                    .doOnSuccess(comicWrapper -> {
                        if(comicWrapper.isSuccess()){
                            Comic comic = comicWrapper.getComic();
                            comicWrapper.setLatest(true);
                            mPreferenceProvider.setLatestComicNumber(comic.getNum());
                            mPreferenceProvider.setFirstLaunch();
                            mPreferenceProvider.setBookmarkComicNumber(comic.getNum());
                        }
                    });
        }else{
            return loadComic(mPreferenceProvider.getBookmarkComicNumber(NO_BOOKMARK));
        }

    }

    @Override
    public Maybe<ComicWrapper> loadComic(int comicNumber) {
        return  mDataSource.getComic(comicNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        if (comic.getNum() == 1){
                            comicWrapper.setFirst(true);
                        }else if(comic.getNum() == mPreferenceProvider.getLatestComicNumber(DEFAULT_LATEST_COMIC_NUMBER)){
                            comicWrapper.setLatest(true);
                        }
                        mPreferenceProvider.setBookmarkComicNumber(comic.getNum());
                    }
                });
    }

    @Override
    public Single<Integer> getLatestComicNumber() {
        return Single.just(mPreferenceProvider.getLatestComicNumber(DEFAULT_LATEST_COMIC_NUMBER));
    }
}
