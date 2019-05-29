package pl.jpcodetask.xkcdcomics.ui.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;

public class FavoritesViewModel extends ViewModel {

    private final FavoritesUseCase mFavoritesUseCase;

    private final MutableLiveData<List<Comic>> mComicList = new MutableLiveData<>();

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

    public LiveData<List<Comic>> getComicList() {
        return mComicList;
    }
}
