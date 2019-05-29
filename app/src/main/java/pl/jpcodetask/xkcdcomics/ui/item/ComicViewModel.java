package pl.jpcodetask.xkcdcomics.ui.item;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.ui.common.ComicState;
import pl.jpcodetask.xkcdcomics.usecase.ExploreUseCase;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class ComicViewModel extends ViewModel {

    private final ExploreUseCase mExploreUseCase;

    /** State*/
    private final MutableLiveData<ComicState> mState = new MutableLiveData<>();

    /** Data*/
    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> mRequestComicNumber = new MutableLiveData<>();

    public ComicViewModel(ExploreUseCase exploreUseCase) {
       mExploreUseCase = exploreUseCase;
    }

    void loadComic(){
        setStateOnLoading();
        mExploreUseCase.loadComic()
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mComicLiveData.setValue(comic);
                        mRequestComicNumber.setValue(comic.getNum());
                        setStateOnSuccess(comicWrapper.isLatest(), comicWrapper.isFirst(), comic.isFavorite());
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

    private void setStateOnSuccess(boolean isLatest, boolean isFirst, boolean isFavorite){
        mState.setValue(
                new ComicState.Builder()
                        .setDataLoading(false)
                        .setErrorOccurred(false)
                        .setNextAvailable(!isLatest)
                        .setPrevAvailable(!isFirst)
                        .setFavorite(isFavorite)
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
        mExploreUseCase.loadComic(comicNumber)
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mComicLiveData.setValue(comic);
                        setStateOnSuccess(comicWrapper.isLatest(), comicWrapper.isFirst(), comic.isFavorite());
                    }else{
                        setViewStateOnError(comicWrapper.isLatest(), comicWrapper.isFirst());
                    }
                })
                .doOnError(throwable -> {
                    Log.e("Error", "Error", throwable);
                })
                .subscribe();
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
        int randomComicNumber = (int) Math.abs( Math.random() * mExploreUseCase.getLatestComicNumber() + 1);
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

        if(mComicLiveData.getValue() == null){
            return;
        }

        mExploreUseCase.setFavorite(mComicLiveData.getValue().getNum(), isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .doOnComplete(() -> {
                    if (isFavorite){
                        mMessage.setValue(new Event<>("Add to favorites"));
                        mState.setValue(new ComicState.Builder(mState.getValue()).setFavorite(true).build());
                    }else{
                        mMessage.setValue(new Event<>("Remove from favorites"));
                        mState.setValue(new ComicState.Builder(mState.getValue()).setFavorite(false).build());

                    }
                })
                .subscribe();
    }

    List<Integer> getComicRange(){
        int latestComicNumber = mExploreUseCase.getLatestComicNumber();


        ArrayList<Integer> range =  new ArrayList<>();
        for (int i = latestComicNumber; i > 0; i--){
            range.add(0, i);
        }

        return range;
    }


    public LiveData<Comic> getComic(){
        return mComicLiveData;
    }

    public LiveData<Event<String>> getMessage(){
        return mMessage;
    }

    public LiveData<ComicState> getState() { return mState; }

    public LiveData<Integer> getRequestComicNumber() { return mRequestComicNumber; }
}
