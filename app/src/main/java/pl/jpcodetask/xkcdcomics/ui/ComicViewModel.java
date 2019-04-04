package pl.jpcodetask.xkcdcomics.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class ComicViewModel extends ViewModel {

    private final DataSource mDataSource;

    private MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<String>> mMessageEventLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsDataLoading = new MutableLiveData<>();

    public ComicViewModel(DataSource dataSource) {
        mDataSource = dataSource;
    }

    void loadComic(){
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




    LiveData<Comic> getComicLiveData(){
        return mComicLiveData;
    }

    LiveData<Event<String>> getMessageEventLiveData(){
        return mMessageEventLiveData;
    }

    LiveData<Boolean> getIsDataLoading() {
        return mIsDataLoading;
    }
}
