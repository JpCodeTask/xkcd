package pl.jpcodetask.xkcdcomics.data.source;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

public interface DataSource {
    Observable<List<Comic>> getData();
    Single<Comic> getComic(int comicNumber);
    Single<Comic> getLatestComic();
}
