package pl.jpcodetask.xkcdcomics.data.source.local;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

@Dao
public interface ComicDao {
    @Insert
    Completable insert(@NonNull Comic comic);

    @Query("SELECT * FROM comic")
    Maybe<List<Comic>> all();

    @Query("SELECT * FROM comic WHERE favorite = 1")
    Maybe<List<Comic>> favorite();

    @Query("SELECT * FROM comic WHERE num = :comicNumber")
    Maybe<Comic> item(int comicNumber);

    @Query("SELECT * FROM comic ORDER BY num DESC LIMIT 1")
    Maybe<Comic> latestItem();

    @Query("DELETE FROM comic")
    Completable removeAll();

    @Query("UPDATE comic SET favorite = :isFavorite WHERE num = :comicNumber")
    Completable setFavorite(int comicNumber, boolean isFavorite);

}
