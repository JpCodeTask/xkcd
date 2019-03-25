package pl.jpcodetask.xkcdcomics.data.source.remote;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class RemoteDataSourceImpl implements DataSource {

    private final XkcdApi mApi;

    public RemoteDataSourceImpl(XkcdApi api) {
        mApi = api;
    }

    @Override
    public Observable<List<Comic>> getData() {
        return null;
    }

    @Override
    public Single<Comic> getComic(int comicNumber) {
        return mApi.comicItem(comicNumber);
    }
}
