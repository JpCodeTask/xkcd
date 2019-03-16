package pl.jpcodetask.xkcdcomics.ui.list;

import android.view.View;

import pl.jpcodetask.xkcdcomics.data.model.Comic;

public interface ComicItemListener{
    void onItemClicked(View view, Comic item);
    void onFavoriteClicked(View view, Comic item);
}