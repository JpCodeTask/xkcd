package pl.jpcodetask.xkcdcomics.ui.item;

public interface ComicNavigator {
    void onNext();
    void onPrev();
    void onGoTo(int comicNumber);
    void onComicDetails();
    void onRandom();
    void onReload();
}
