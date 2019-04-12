package pl.jpcodetask.xkcdcomics.data.source.remote;

import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class RemoteDataSourceImpl implements DataSource {

    private final XkcdApi mApi;

    public RemoteDataSourceImpl(XkcdApi api) {
        mApi = api;
    }


    @Override
    public Single<ComicWrapper> getComic(int comicNumber) {
        return null;//mApi.comicItem(comicNumber);
    }

    @Override
    public Single<ComicWrapper> getLatestComic() {
        return mApi.latestComicItem()
                .map(ComicWrapper::from)
                .onErrorResumeNext(throwable -> Single.just(ComicWrapper.from(throwable)));
    }
}
