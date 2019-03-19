package pl.jpcodetask.xkcdcomics.data.source.local;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

@Dao
public interface ComicDao {
    @Insert
    Completable insert(@NonNull Comic comic);

    @Update
    Completable update(@NonNull Comic comic);

    @Query("SELECT * FROM comic")
    Observable<List<Comic>> all();

    @Query("SELECT * FROM comic WHERE favorite = 1")
    Observable<List<Comic>> favorite();

    @Query("SELECT * FROM comic WHERE num = :comicNumber")
    Single<Comic> item(int comicNumber);

    @Query("DELETE FROM comic")
    Completable removeAll();

}
