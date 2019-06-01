package pl.jpcodetask.xkcdcomics.utils;

import android.content.Intent;

import pl.jpcodetask.xkcdcomics.data.model.Comic;

public class ComicUtils {

    private ComicUtils(){
        //empty
    }

    public static Intent getComicShareIntent(Comic comic){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, comic.getImgUrl());
        shareIntent.setType("text/plain");

        return shareIntent;
    }
}
