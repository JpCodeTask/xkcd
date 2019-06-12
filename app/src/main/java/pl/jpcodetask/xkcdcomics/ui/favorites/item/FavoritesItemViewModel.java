package pl.jpcodetask.xkcdcomics.ui.favorites.item;

import pl.jpcodetask.xkcdcomics.ui.common.BaseItemViewModel;
import pl.jpcodetask.xkcdcomics.ui.common.ComicState;
import pl.jpcodetask.xkcdcomics.usecase.BaseUseCase;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;

public class FavoritesItemViewModel extends BaseItemViewModel {

    private final FavoritesUseCase mFavoritesUseCase;

    public FavoritesItemViewModel(FavoritesUseCase favoritesUseCase) {
        mFavoritesUseCase = favoritesUseCase;
    }

    public void loadComic(int comicNumber){
        if (mComicLiveData.getValue() != null){
            return;
        }
        setStateOnLoading();
        mCompositeDisposable.add(mFavoritesUseCase.loadComic(comicNumber)
                .doOnSuccess(comicWrapper -> {
                    if (comicWrapper.isSuccess()){
                        mComicLiveData.setValue(comicWrapper.getComic());
                        setStateOnSuccess(comicWrapper.getComic().isFavorite());
                    }else{
                        setViewStateOnError();
                    }
                }).subscribe());
    }

    private void setStateOnLoading(){
        mState.setValue(
                new ComicState.Builder()
                        .setDataLoading(true)
                        .setErrorOccurred(false)
                        .setNextAvailable(false)
                        .setPrevAvailable(false)
                        .build()
        );
    }

    private void setStateOnSuccess(boolean isFavorite){
        mState.setValue(
                new ComicState.Builder()
                        .setDataLoading(false)
                        .setErrorOccurred(false)
                        .setNextAvailable(false)
                        .setPrevAvailable(false)
                        .setFavorite(isFavorite)
                        .build()
        );
    }

    private void setViewStateOnError(){
        mState.setValue(
                new ComicState.Builder()
                        .setDataLoading(false)
                        .setErrorOccurred(true)
                        .setNextAvailable(false)
                        .setPrevAvailable(false)
                        .build()
        );
    }

    @Override
    protected BaseUseCase getUseCase() {
        return mFavoritesUseCase;
    }

}
