package pl.jpcodetask.xkcdcomics.ui;

public interface ComicNavigator {
    void onNext();
    void onPrev();
    void onGoTo(int comicNumber);
    void onComicDetails();
}
