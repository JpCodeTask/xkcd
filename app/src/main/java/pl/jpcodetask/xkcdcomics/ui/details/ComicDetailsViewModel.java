package pl.jpcodetask.xkcdcomics.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class ComicDetailsViewModel extends ViewModel {

    private CompositeDisposable mDisposable;

    private final DataSource mDataSource;

    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mDataLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> mErrorEvent = new MutableLiveData<>();

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
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .subscribe(new SingleObserver<Comic>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Comic comic) {
                        mDataLoadingLiveData.setValue(false);
                        mComicLiveData.setValue(comic);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mErrorEvent.setValue(new Event<>(R.string.item_loading_error));
                    }
                });

    }

    void clean(){
        mDisposable.clear();
    }



    public LiveData<Comic> getComicLiveData(){
        return mComicLiveData;
    }

    public LiveData<Boolean> getDataLoadingLiveData() {
        return mDataLoadingLiveData;
    }

    LiveData<Event<Integer>> getErrorEventLiveData() {
        return mErrorEvent;
    }
}
