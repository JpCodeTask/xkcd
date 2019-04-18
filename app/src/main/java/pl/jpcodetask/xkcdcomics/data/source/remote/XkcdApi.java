package pl.jpcodetask.xkcdcomics.data.source.remote;

import io.reactivex.Maybe;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdApi {
    @GET("{comicNumber}/info.0.json")
    Maybe<Comic> comicItem(@Path("comicNumber") int comicNumber);

    @GET("/info.0.json")
    Maybe<Comic> latestComicItem();
}
