package pl.jpcodetask.xkcdcomics.ui.list;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.utils.TestUtils;

public class ComicListViewModel extends ViewModel {

    private MutableLiveData<List<String>> mMutableTitleListLiveData = new MutableLiveData<>();

    public ComicListViewModel(){
        mMutableTitleListLiveData.setValue(TestUtils.getData());
    }

    public LiveData<List<String>> getTitleList(){
        return mMutableTitleListLiveData;
    }
}
