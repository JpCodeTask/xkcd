package pl.jpcodetask.xkcdcomics.usecase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.data.source.Repository;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;
import pl.jpcodetask.xkcdcomics.utils.SharedPreferenceProvider;

public class FavoritesUseCaseImpl implements FavoritesUseCase {

    private final Repository mDataSource;
    private final SharedPreferenceProvider mPreferenceProvider;

    @Inject
    public FavoritesUseCaseImpl(@Named("repository") Repository dataSource, SharedPreferenceProvider preferenceProvider) {
        mDataSource = dataSource;
        mPreferenceProvider = preferenceProvider;
    }


    @Override
    public Maybe<List<Comic>> loadList() {
        return null;
    }

    @Override
    public Maybe<ComicWrapper> loadComic(int comicNumber) {
        return mDataSource.getComic(comicNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread());
    }

    @Override
    public Completable setFavorite(int comicNumber, boolean isFavorite) {
        return mDataSource.setFavorite(comicNumber, isFavorite);
    }
}
