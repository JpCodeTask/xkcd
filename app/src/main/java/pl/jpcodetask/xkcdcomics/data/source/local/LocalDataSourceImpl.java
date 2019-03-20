package pl.jpcodetask.xkcdcomics.data.source.local;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class LocalDataSourceImpl implements DataSource {


    private final ComicDao mComicDao;

    public LocalDataSourceImpl(ComicDao comicDao){
        mComicDao = comicDao;
        for(int i = 0; i < 10; i++){
            Comic comic = new Comic();
            comic.setNum(i);
            comic.setTitle("Lorem ipsum 220122" + i);
            comic.setDay(i);
            comic.setMonth(i + 2);
            comic.setYear(10 * i + 1000);

            mComicDao.insert(comic)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        }
    }


    @Override
    public Observable<List<Comic>> getData() {
        return mComicDao.all();
    }

    @Override
    public Single<Comic> getComic(int comicNumber) {
        return mComicDao.item(comicNumber);
    }
}
