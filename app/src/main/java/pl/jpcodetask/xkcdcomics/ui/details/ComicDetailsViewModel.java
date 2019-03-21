package pl.jpcodetask.xkcdcomics.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class ComicDetailsViewModel extends ViewModel {

    private final DataSource mDataSource;

    private MutableLiveData<Comic> mComicMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDataLoadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Integer>> mErrorEvent = new MutableLiveData<>();

    public ComicDetailsViewModel(DataSource dataSource){
        mDataSource = dataSource;
    }

    void loadComic(Integer comicNumber){
        if (comicNumber == null){
            mErrorEvent.setValue(new Event<>(R.string.item_loading_error));
            return;
        }

        mDataLoadingLiveData.setValue(true);
        mDataSource.getComic(comicNumber)
                .observeOn(Schedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Comic>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Comic comic) {
                        mDataLoadingLiveData.setValue(false);
                        mComicMutableLiveData.setValue(comic);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mErrorEvent.setValue(new Event<>(R.string.item_loading_error));
                    }
                });

    }



    LiveData<Comic> getComicLiveData(){
        return mComicMutableLiveData;
    }

    LiveData<Boolean> getDataLoadingLiveData() {
        return mDataLoadingLiveData;
    }

    LiveData<Event<Integer>> getErrorEventLiveData() {
        return mErrorEvent;
    }
}
