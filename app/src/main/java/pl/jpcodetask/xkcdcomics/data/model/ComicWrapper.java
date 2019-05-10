package pl.jpcodetask.xkcdcomics.data.model;

public class ComicWrapper {

    private Comic mComic;
    private boolean mIsLatest;
    private boolean mIsFirst;
    private Throwable mThrowable;

    private ComicWrapper(Comic comic){
         mComic =  comic;
    }

    private ComicWrapper(Throwable throwable){
        mThrowable =  throwable;
    }

    public static ComicWrapper from(Comic comic){
        return new ComicWrapper(comic);
    }

    public static ComicWrapper from(Throwable throwable){
        return new ComicWrapper(throwable);
    }

    public Comic getComic() {
        return mComic;
    }

    public Throwable getError(){
        return mThrowable;
    }

    public boolean isSuccess(){
        return mThrowable == null;
    }

    public boolean isFirst() {
        return mIsFirst;
    }

    public boolean isLatest() {
        return mIsLatest;
    }

    public void setFirst(boolean first) {
        mIsFirst = first;
    }

    public void setLatest(boolean latest) {
        mIsLatest = latest;
    }
}
