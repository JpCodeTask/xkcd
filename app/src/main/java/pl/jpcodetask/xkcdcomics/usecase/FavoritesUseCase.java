package pl.jpcodetask.xkcdcomics.usecase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;

public interface FavoritesUseCase extends BaseUseCase, Likeable {
    Maybe<List<Comic>> loadList();
    Maybe<ComicWrapper> loadComic(int comicNumber);
    Completable setFavorite(int comicNumber, boolean isFavorite);
    int getSortField(int defaultValue);
    void setSortField(int sortfield);
}
