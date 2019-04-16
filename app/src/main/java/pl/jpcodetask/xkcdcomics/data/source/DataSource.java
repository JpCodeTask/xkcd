package pl.jpcodetask.xkcdcomics.data.source;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;

public interface DataSource {
    Single<ComicWrapper> getComic(int comicNumber);
    Single<ComicWrapper> getLatestComic();
    Completable saveComic(@NonNull Comic comic);
    Completable updateComic(@NonNull Comic comic);
}
