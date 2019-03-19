package pl.jpcodetask.xkcdcomics.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

@Database(entities = {Comic.class}, version = 1, exportSchema = false)
public abstract class ComicDatabase extends RoomDatabase {
    public abstract ComicDao getComicDao();
}
