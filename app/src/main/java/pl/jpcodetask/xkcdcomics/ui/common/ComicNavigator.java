package pl.jpcodetask.xkcdcomics.ui.common;

public interface ComicNavigator extends ComicViewer{
    void onNext();
    void onPrev();
    void onGoTo(int comicNumber);
    void onRandom();
}
