package pl.jpcodetask.xkcdcomics.data.source;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;

public interface Repository {
    Maybe<ComicWrapper> getComic(int comicNumber);
    Maybe<ComicWrapper> getLatestComic();
    Completable saveComic(@NonNull Comic comic);
    Completable updateComic(@NonNull Comic comic);
}
