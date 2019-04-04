package pl.jpcodetask.xkcdcomics.data.source;

import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

public interface DataSource {
    Single<Comic> getComic(int comicNumber);
    Single<Comic> getLatestComic();
}
