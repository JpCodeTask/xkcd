package pl.jpcodetask.xkcdcomics.usecase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.data.source.Repository;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;
import pl.jpcodetask.xkcdcomics.utils.SharedPreferenceProvider;

public class SingleComicUseCaseImpl implements SingleComicUseCase {

    private final Repository mDataSource;
    private final SharedPreferenceProvider mPreferenceProvider;

    @Inject
    public SingleComicUseCaseImpl(@Named("repository") Repository dataSource, SharedPreferenceProvider preferenceProvider) {
        mDataSource = dataSource;
        mPreferenceProvider = preferenceProvider;
    }

    @Override
    public Maybe<ComicWrapper> loadComic() {
        return  mDataSource.getLatestComic()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mPreferenceProvider.setLatestComicNumber(comic.getNum());
                    }
                });
    }

    @Override
    public Maybe<ComicWrapper> loadComic(int comicNumber) {
        return null;
    }
}
