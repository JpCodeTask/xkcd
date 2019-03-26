package pl.jpcodetask.xkcdcomics.data.source.local;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class LocalDataSourceImpl implements DataSource {


    private final ComicDao mComicDao;

    public LocalDataSourceImpl(ComicDao comicDao){
        mComicDao = comicDao;

        mComicDao.removeAll()
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        for(int i = 0; i < 10; i++){
                            Comic comic = new Comic();
                            comic.setNum(i + 2000);
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
                    public void onError(Throwable e) {

                    }
                });


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
