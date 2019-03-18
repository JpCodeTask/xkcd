package pl.jpcodetask.xkcdcomics.ui.list;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class ComicListViewModel extends ViewModel {

    private final DataSource mDataSource;

    private MutableLiveData<List<Comic>> mMutableTitleListLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Integer>> mEventComicDetailsMutableLiveData = new MutableLiveData<>();

    public ComicListViewModel(DataSource dataSource){
        mDataSource = dataSource;
        mMutableTitleListLiveData.setValue(mDataSource.getData());
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
}
