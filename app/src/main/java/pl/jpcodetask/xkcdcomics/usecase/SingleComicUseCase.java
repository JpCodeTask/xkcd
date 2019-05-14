package pl.jpcodetask.xkcdcomics.usecase;

import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.ComicWrapper;

public interface SingleComicUseCase {
    Maybe<ComicWrapper> loadComic();
    Maybe<ComicWrapper> loadComic(int comicNumber);
    Single<Integer> getLatestComicNumber();
}
