package pl.jpcodetask.xkcdcomics.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class ComicDetailsViewModel extends ViewModel {

    private final DataSource mDataSource;

    private MutableLiveData<Comic> mComicMutableLiveData = new MutableLiveData<>();

    public ComicDetailsViewModel(DataSource dataSource){
        mDataSource = dataSource;
    }

    public void loadComic(int comicNumber){
        mComicMutableLiveData.setValue(mDataSource.getComic(comicNumber));
    }

    public LiveData<Comic> getComicLiveData(){
        return mComicMutableLiveData;
    }
}
