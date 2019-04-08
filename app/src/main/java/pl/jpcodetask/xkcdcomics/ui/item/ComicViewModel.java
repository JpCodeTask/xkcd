package pl.jpcodetask.xkcdcomics.ui.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class ComicViewModel extends ViewModel {

    private final DataSource mDataSource;

    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mMessageEventLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsDataLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLatest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsDetailsVisible = new MutableLiveData<>();

    public ComicViewModel(DataSource dataSource) {
        mDataSource = dataSource;
    }

    void loadComic(){
        mIsDetailsVisible.setValue(true);
        mIsDataLoading.setValue(true);
        mDataSource.getLatestComic()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .doOnSuccess(comic -> {
                    mIsDataLoading.setValue(false);
                    mComicLiveData.setValue(comic);
                })
                .doOnError(error ->{
                    mIsDataLoading.setValue(false);
                    mMessageEventLiveData.setValue(new Event<>("Error occurred"));
                })
                .subscribe();
    }

    void setComicFavorite(boolean isFavorite){
        //TODO add missing implementation
        if(isFavorite){
            mMessageEventLiveData.setValue(new Event<>("Add to favorites"));
        }else{
            mMessageEventLiveData.setValue(new Event<>("Remove from favorites"));
        }

    }

    void goToComic(int comicNumber){
        //TODO implement
    }

    void prevComic(){
        //TODO implement
    }

    void nextComic(){
        //TODO implement
    }

    void comicDetails(){
        if(mIsDetailsVisible.getValue() != null){
            mIsDetailsVisible.setValue(!mIsDetailsVisible.getValue());
        }else{
            mIsDetailsVisible.setValue(true);
        }
    }


    public LiveData<Comic> getComic(){
        return mComicLiveData;
    }

    public LiveData<Event<String>> getMessageEvent(){
        return mMessageEventLiveData;
    }

    public LiveData<Boolean> getIsDataLoading() {
        return mIsDataLoading;
    }

    public LiveData<Boolean> getIsLatest() { return mIsLatest; }

    public LiveData<Boolean> getIsDetailsVisible() { return mIsDetailsVisible; }
}
