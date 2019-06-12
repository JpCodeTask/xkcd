package pl.jpcodetask.xkcdcomics.usecase;

import io.reactivex.Completable;

public interface Likeable {
    Completable setFavorite(int comicNumber, boolean isFavorite);
}
