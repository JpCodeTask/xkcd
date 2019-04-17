package pl.jpcodetask.xkcdcomics.usecase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;
import pl.jpcodetask.xkcdcomics.utils.SharedPreferenceProvider;

public class ExploreUseCaseImpl implements ExploreUseCase {

    private final DataSource mDataSource;
    private final SharedPreferenceProvider mPreferenceProvider;

    @Inject
    public ExploreUseCaseImpl(@Named("repository") DataSource dataSource, SharedPreferenceProvider preferenceProvider) {
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
                }).toMaybe();
    }

    @Override
    public Maybe<ComicWrapper> loadComic(int comicNumber) {
        return null;
    }
}
