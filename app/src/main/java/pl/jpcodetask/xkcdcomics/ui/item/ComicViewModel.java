package pl.jpcodetask.xkcdcomics.ui.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.Repository;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;
import pl.jpcodetask.xkcdcomics.utils.SharedPreferenceProvider;

public class ComicViewModel extends ViewModel implements ComicNavigator{

    private final Repository mRepository;
    private final SharedPreferenceProvider mPreferenceProvider;


    /** View state*/
    private final MutableLiveData<Boolean> mIsDetailsVisible = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsDataLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsError = new MutableLiveData<>();

    /** Data*/
    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mSnackBarMessage = new MutableLiveData<>();

    /** Data state*/
    private final MutableLiveData<Boolean> mIsLatest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsFirst = new MutableLiveData<>();



    public ComicViewModel(Repository repository, SharedPreferenceProvider sharedPreferenceProvider) {
        mRepository = repository;
        mPreferenceProvider = sharedPreferenceProvider;
    }

    void loadComic(){
        mIsError.setValue(false);
        mIsDetailsVisible.setValue(false);
        mIsDataLoading.setValue(true);
        mRepository.getComic(2017)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .doOnSuccess(comicWrapper -> {
                    if(comicWrapper.isSuccess()){
                        Comic comic = comicWrapper.getComic();

                        mIsDataLoading.setValue(false);
                        mComicLiveData.setValue(comic);
                        mIsLatest.setValue(true);
                        mPreferenceProvider.setLatestComicNumber(comic.getNum());
                    }else{
                        mIsDataLoading.setValue(false);
                        mIsError.setValue(true);
                    }
                }).subscribe();
    }

    void setComicFavorite(boolean isFavorite){
        //TODO add missing implementation
        if(isFavorite){
            mSnackBarMessage.setValue(new Event<>("Add to favorites"));
        }else{
            mSnackBarMessage.setValue(new Event<>("Remove from favorites"));
        }

    }


    @Override
    public void onNext() {
        //TODO implement
    }

    @Override
    public void onPrev() {
        //TODO implement
    }

    @Override
    public void onGoTo(int comicNumber) {
        //TODO implement
    }

    @Override
    public void onComicDetails() {
        if(mIsDetailsVisible.getValue() != null){
            mIsDetailsVisible.setValue(!mIsDetailsVisible.getValue());
        }
    }

    @Override
    public void onRandom() {

    }

    @Override
    public void onReload() {
        loadComic();
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

    public LiveData<Boolean> getIsDetailsVisible() { return mIsDetailsVisible; }

    public LiveData<Boolean> getIsError() { return mIsError; }

}
