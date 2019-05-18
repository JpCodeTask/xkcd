package pl.jpcodetask.xkcdcomics.ui.item;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.usecase.SingleComicUseCase;

public class ComicViewModel extends ViewModel {

    private final SingleComicUseCase mSingleComicUseCase;
    private boolean mIsUserAction = false;
    private int mLatestComicNumber;

    /** View state*/
    private final MutableLiveData<ComicState> mViewState = new MutableLiveData<>();

    /** Data*/
    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mSnackBarMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> mRequestComicNumber = new MutableLiveData<>();

    public ComicViewModel(SingleComicUseCase singleComicUseCase) {
       mSingleComicUseCase = singleComicUseCase;
    }

    void loadComic(){
        initViewState();
        mSingleComicUseCase.loadComic()
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mComicLiveData.setValue(comic);
                        setViewStateOnSuccess(comicWrapper.isLatest(), comicWrapper.isFirst());
                    }else{
                        setViewStateOnError();
                    }
                }).subscribe();
    }

    private void initViewState(){
        mViewState.setValue(
                new ComicState.Builder()
                        .setDataLoading(true)
                        .setErrorOccurred(false)
                        .setNextAvailable(true)
                        .setPrevAvailable(true)
                        .build()
        );
    }

    private void setViewStateOnSuccess(boolean isLatest, boolean isFirst){
        mViewState.setValue(
                new ComicState.Builder()
                        .setDataLoading(false)
                        .setErrorOccurred(false)
                        .setNextAvailable(!isLatest)
                        .setPrevAvailable(!isFirst)
                        .build()
        );
    }

    private void setViewStateOnError(){
        mViewState.setValue(
                new ComicState.Builder()
                        .setDataLoading(false)
                        .setErrorOccurred(true)
                        .setNextAvailable(true)
                        .setPrevAvailable(true)
                        .build()
        );
    }

    void loadComic(int comicNumber){
        initViewState();
        mSingleComicUseCase.loadComic(comicNumber)
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mComicLiveData.setValue(comic);
                        setViewStateOnSuccess(comicWrapper.isLatest(), comicWrapper.isFirst());
                    }else{
                        setViewStateOnError();
                    }
                }).subscribe();
    }

    public void loadNext() {
        mRequestComicNumber.setValue(mComicLiveData.getValue().getNum() + 1);
        loadComic(mRequestComicNumber.getValue());
    }


    public void loadPrev() {
        mRequestComicNumber.setValue(mComicLiveData.getValue().getNum() - 1);
        loadComic(mRequestComicNumber.getValue());
    }

    public void goToComic(int comicNumber) {
        //TODO viewmodel should not know about it
        if (mIsUserAction){
            mRequestComicNumber.setValue(comicNumber);
            loadComic(comicNumber);
        }
        //first selection is made by system
        mIsUserAction = true;
    }

    public void reload() {
        if (mRequestComicNumber.getValue() != null){
            loadComic(mRequestComicNumber.getValue());
        }else{
            loadComic();
        }
    }

    void setComicFavorite(boolean isFavorite){
        //TODO add missing implementation
        if(isFavorite){
            mSnackBarMessage.setValue(new Event<>("Add to favorites"));
        }else{
            mSnackBarMessage.setValue(new Event<>("Remove from favorites"));
        }

    }

    List<Integer> getComicRange(){

        mSingleComicUseCase.getLatestComicNumber().
                doOnSuccess(comicNumber -> {
                    mLatestComicNumber = comicNumber;
                }).subscribe();

        ArrayList<Integer> range =  new ArrayList<>();
        for (int i = mLatestComicNumber; i > 0; i--){
            range.add(0, i);
        }

        return range;
    }


    public LiveData<Comic> getComic(){
        return mComicLiveData;
    }

    public LiveData<Event<String>> getSnackBarMessage(){
        return mSnackBarMessage;
    }

    public LiveData<ComicState> getViewState() { return mViewState; }

    public LiveData<Integer> getRequestComicNumber() { return mRequestComicNumber; }
}
