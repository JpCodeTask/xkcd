package pl.jpcodetask.xkcdcomics.ui.list;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class ComicListViewModel extends ViewModel {

    private final DataSource mDataSource;

    private MutableLiveData<List<String>> mMutableTitleListLiveData = new MutableLiveData<>();

    public ComicListViewModel(DataSource dataSource){
        mDataSource = dataSource;
        mMutableTitleListLiveData.setValue(mDataSource.getData());
    }

    public LiveData<List<String>> getTitleList(){
        return mMutableTitleListLiveData;
    }
}
