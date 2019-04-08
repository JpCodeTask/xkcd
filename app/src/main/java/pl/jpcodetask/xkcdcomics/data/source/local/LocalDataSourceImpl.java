package pl.jpcodetask.xkcdcomics.data.source.local;

import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class LocalDataSourceImpl implements DataSource {


    private final ComicDao mComicDao;

    public LocalDataSourceImpl(ComicDao comicDao){
        mComicDao = comicDao;
    }

    @Override
    public Single<Comic> getComic(int comicNumber) {
        return mComicDao.item(comicNumber);
    }

    @Override
    public Single<Comic> getLatestComic() {
        return null;
    }
}
