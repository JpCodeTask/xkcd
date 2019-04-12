package pl.jpcodetask.xkcdcomics.data.source;

import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;

public interface DataSource {
    Single<ComicWrapper> getComic(int comicNumber);
    Single<ComicWrapper> getLatestComic();
}
