package pl.jpcodetask.xkcdcomics.ui.item;

import android.util.Log;

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

    /** View state*/
    private final MutableLiveData<ComicState> mState = new MutableLiveData<>();

    /** Data*/
    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mSnackBarMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> mRequestComicNumber = new MutableLiveData<>();

    public ComicViewModel(SingleComicUseCase singleComicUseCase) {
       mSingleComicUseCase = singleComicUseCase;
    }

    void loadComic(){
        setStateOnLoading();
        mSingleComicUseCase.loadComic()
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mComicLiveData.setValue(comic);
                        mRequestComicNumber.setValue(comic.getNum());
                        setStateOnSuccess(comicWrapper.isLatest(), comicWrapper.isFirst());
                    }else{
                        setViewStateOnError(comicWrapper.isLatest(), comicWrapper.isFirst());
                    }
                }).subscribe();
    }

    private void setStateOnLoading(){
        mState.setValue(
                new ComicState.Builder()
                        .setDataLoading(true)
                        .setErrorOccurred(false)
                        .setNextAvailable(false)
                        .setPrevAvailable(false)
                        .build()
        );
    }

    private void setStateOnSuccess(boolean isLatest, boolean isFirst){
        mState.setValue(
                new ComicState.Builder()
                        .setDataLoading(false)
                        .setErrorOccurred(false)
                        .setNextAvailable(!isLatest)
                        .setPrevAvailable(!isFirst)
                        .build()
        );
    }

    private void setViewStateOnError(boolean isLatest, boolean isFirst){
        mState.setValue(
                new ComicState.Builder()
                        .setDataLoading(false)
                        .setErrorOccurred(true)
                        .setNextAvailable(!isLatest)
                        .setPrevAvailable(!isFirst)
                        .build()
        );
    }

    void loadComic(int comicNumber){
        Log.d("Load", "!");
        setStateOnLoading();
        mSingleComicUseCase.loadComic(comicNumber)
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mComicLiveData.setValue(comic);
                        setStateOnSuccess(comicWrapper.isLatest(), comicWrapper.isFirst());
                    }else{
                        setViewStateOnError(comicWrapper.isLatest(), comicWrapper.isFirst());
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
        Log.d("Load", "goto");
        mRequestComicNumber.setValue(comicNumber);
        loadComic(comicNumber);
    }

    public void loadRandom(){
        int randomComicNumber = (int) Math.abs( Math.random() * mSingleComicUseCase.getLatestComicNumber() + 1);
        mRequestComicNumber.setValue(randomComicNumber);
        loadComic(randomComicNumber);
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
        int latestComicNumber = mSingleComicUseCase.getLatestComicNumber();


        ArrayList<Integer> range =  new ArrayList<>();
        for (int i = latestComicNumber; i > 0; i--){
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

    public LiveData<ComicState> getState() { return mState; }

    public LiveData<Integer> getRequestComicNumber() { return mRequestComicNumber; }
}
