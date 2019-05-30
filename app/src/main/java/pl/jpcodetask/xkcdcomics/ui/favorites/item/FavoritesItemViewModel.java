package pl.jpcodetask.xkcdcomics.ui.favorites.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.ui.common.ComicState;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;

public class FavoritesItemViewModel extends ViewModel {

    private final FavoritesUseCase mFavoritesUseCase;

    /** State*/
    private final MutableLiveData<ComicState> mState = new MutableLiveData<>();

    /** Data*/
    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();

    public FavoritesItemViewModel(FavoritesUseCase favoritesUseCase) {
        mFavoritesUseCase = favoritesUseCase;
    }

    public void loadComic(int comicNumber){
        if (mComicLiveData.getValue() != null){
            return;
        }

        mFavoritesUseCase.loadComic(comicNumber)
                .doOnSuccess(comicWrapper -> {
                    if (comicWrapper.isSuccess()){
                        mComicLiveData.setValue(comicWrapper.getComic());
                    }
                }).subscribe();
    }

    public LiveData<Comic> getComic() {
        return mComicLiveData;
    }

    public LiveData<ComicState> getState() {
        return mState;
    }
}
