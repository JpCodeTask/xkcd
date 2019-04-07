package pl.jpcodetask.xkcdcomics.ui;

public interface ComicNavigator {
    void onNext(int currentComicNumber);
    void onPrev(int currentComicNumber);
    void onGoTo(int currentComicNumber);
    void showDetails();
    void hideDetails();
}
