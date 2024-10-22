package pl.jpcodetask.xkcdcomics.data.source.local;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class LocalDataSourceImpl implements DataSource {


    private final ComicDao mComicDao;

    public LocalDataSourceImpl(ComicDao comicDao){
        mComicDao = comicDao;
    }

    @Override
    public Maybe<Comic> getComic(int comicNumber) {
        return mComicDao.item(comicNumber);
    }

    @Override
    public Maybe<Comic> getLatestComic() {
        return null;
    }

    @Override
    public Completable saveComic(@NonNull Comic comic) {
        return mComicDao.insert(comic);
    }

    @Override
    public Completable setFavorite(int comicNumber, boolean isFavorite) {
        return mComicDao.setFavorite(comicNumber, isFavorite);
    }

    @Override
    public Maybe<List<Comic>> getFavorites() {
        return mComicDao.favorite();
    }
}
