package pl.jpcodetask.xkcdcomics.usecase;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;

public interface SingleComicUseCase {
    Maybe<ComicWrapper> loadComic();
    Maybe<ComicWrapper> loadComic(int comicNumber);
    int getLatestComicNumber();
    Completable setFavorite(int comicNumber, boolean isFavorite);
}
