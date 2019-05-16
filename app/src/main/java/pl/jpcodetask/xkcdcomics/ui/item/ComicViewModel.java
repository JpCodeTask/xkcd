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
    private final MutableLiveData<Boolean> mIsDataLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsError = new MutableLiveData<>();

    /** Data*/
    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mSnackBarMessage = new MutableLiveData<>();

    /** Data state*/
    private final MutableLiveData<Boolean> mIsLatest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsFirst = new MutableLiveData<>();

    //TODO wrap states to object

    public ComicViewModel(SingleComicUseCase singleComicUseCase) {
       mSingleComicUseCase = singleComicUseCase;
    }

    void loadComic(){
        mIsError.setValue(false);
        mIsDataLoading.setValue(true);
        mSingleComicUseCase.loadComic()
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mIsDataLoading.setValue(false);
                        mComicLiveData.setValue(comic);
                        mIsLatest.setValue(comicWrapper.isLatest());
                        mIsFirst.setValue(comicWrapper.isFirst());
                    }else{
                        mIsDataLoading.setValue(false);
                        mIsError.setValue(true);
                    }
                }).subscribe();
    }

    void loadComic(int comicNumber){
        mIsError.setValue(false);
        mIsDataLoading.setValue(true);
        mSingleComicUseCase.loadComic(comicNumber)
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();
                        mIsDataLoading.setValue(false);
                        mComicLiveData.setValue(comic);
                        mIsLatest.setValue(comicWrapper.isLatest());
                        mIsFirst.setValue(comicWrapper.isFirst());
                    }else{
                        mIsDataLoading.setValue(false);
                        mIsError.setValue(true);
                    }
                }).subscribe();
    }

    public void loadNext() {
        loadComic(mComicLiveData.getValue().getNum() + 1);
    }


    public void loadPrev() {
        loadComic(mComicLiveData.getValue().getNum() - 1);
    }

    public void goToComic(int comicNumber) {
        if (mIsUserAction){
            loadComic(comicNumber);
        }
        //first selection is made by system
        mIsUserAction = true;
    }

    public void reload() {
        loadComic();
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

    public LiveData<Boolean> getIsDataLoading() {
        return mIsDataLoading;
    }

    public LiveData<Boolean> getIsLatest() { return mIsLatest; }

    public LiveData<Boolean> getIsFirst() { return mIsFirst; }

    //public LiveData<Boolean> getIsDetailsVisible() { return mIsDetailsVisible; }

    public LiveData<Boolean> getIsError() { return mIsError; }

}
