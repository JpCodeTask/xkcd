package pl.jpcodetask.xkcdcomics.ui.list;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class ComicListViewModel extends ViewModel {

    private final DataSource mDataSource;

    private MutableLiveData<List<Comic>> mMutableTitleListLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Integer>> mEventComicDetailsMutableLiveData = new MutableLiveData<>();

    public ComicListViewModel(DataSource dataSource){
        mDataSource = dataSource;
    }

    LiveData<List<Comic>> getTitleList(){
        return mMutableTitleListLiveData;
    }

    LiveData<Event<Integer>> getEvenComicDetails(){
        return mEventComicDetailsMutableLiveData;
    }

    void openComicDetails(int comicNumber){
        mEventComicDetailsMutableLiveData.setValue(new Event<>(comicNumber));
    }

    void start() {
        mDataSource.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .subscribe(new Observer<List<Comic>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Comic> comics) {
                        mMutableTitleListLiveData.setValue(comics);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
