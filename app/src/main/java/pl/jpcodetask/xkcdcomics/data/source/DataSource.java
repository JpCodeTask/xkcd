package pl.jpcodetask.xkcdcomics.data.source;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

public interface DataSource {
    Maybe<Comic> getComic(int comicNumber);
    Maybe<Comic> getLatestComic();
    Completable saveComic(@NonNull Comic comic);
    Completable updateComic(@NonNull Comic comic);
}
