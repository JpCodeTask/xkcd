package pl.jpcodetask.xkcdcomics.data.source;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

public interface DataSource {
    Maybe<Comic> getComic(int comicNumber);
    Maybe<Comic> getLatestComic();
    Completable saveComic(@NonNull Comic comic);
    Completable setFavorite(int comicNumber, boolean isFavorite);
    Maybe<List<Comic>> getFavorites();
}
