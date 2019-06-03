package pl.jpcodetask.xkcdcomics.ui.favorites.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;

public class FavoritesViewModel extends ViewModel {

    private final FavoritesUseCase mFavoritesUseCase;

    private final MutableLiveData<List<Comic>> mComicList = new MutableLiveData<>();
    private final MutableLiveData<String> mSearchQuery = new MutableLiveData<>();
    private final MutableLiveData<Integer> mSortField = new MutableLiveData<>();

    public FavoritesViewModel(FavoritesUseCase favoritesUseCase){
        mFavoritesUseCase = favoritesUseCase;
        loadList();
    }

    void loadList(){
        mFavoritesUseCase.loadList()
                .doOnSuccess(comics -> {
                    mComicList.setValue(comics);
                })
                .subscribe();
    }


    public void search(String query){
        mSearchQuery.setValue(query);
    }

    public void sort(Integer sortfield){
        mSortField.setValue(sortfield);
    }

    LiveData<List<Comic>> getComicList() {
        return mComicList;
    }

    LiveData<Integer> getSortField() {
        return mSortField;
    }

    LiveData<String> getSearchQuery() {
        return mSearchQuery;
    }
}
