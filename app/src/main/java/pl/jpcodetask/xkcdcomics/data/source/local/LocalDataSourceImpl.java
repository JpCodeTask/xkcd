package pl.jpcodetask.xkcdcomics.data.source.local;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class LocalDataSourceImpl implements DataSource {


    private final ComicDao mComicDao;

    public LocalDataSourceImpl(ComicDao comicDao){
        mComicDao = comicDao;
    }

    @Override
    public Single<ComicWrapper> getComic(int comicNumber) {
        return null;//mComicDao.item(comicNumber);
    }

    @Override
    public Single<ComicWrapper> getLatestComic() {
        return null;
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
