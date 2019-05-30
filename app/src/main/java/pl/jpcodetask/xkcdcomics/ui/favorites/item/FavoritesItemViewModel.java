package pl.jpcodetask.xkcdcomics.ui.favorites.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.ui.common.ComicState;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public class FavoritesItemViewModel extends ViewModel {

    private final FavoritesUseCase mFavoritesUseCase;

    /** State*/
    private final MutableLiveData<ComicState> mState = new MutableLiveData<>();

    /** Data*/
    private final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mMessage = new MutableLiveData<>();

    public FavoritesItemViewModel(FavoritesUseCase favoritesUseCase) {
        mFavoritesUseCase = favoritesUseCase;
    }

    public void loadComic(int comicNumber){
        if (mComicLiveData.getValue() != null){
            return;
        }
        setStateOnLoading();
        mFavoritesUseCase.loadComic(comicNumber)
                .doOnSuccess(comicWrapper -> {
                    if (comicWrapper.isSuccess()){
                        mComicLiveData.setValue(comicWrapper.getComic());
                        setStateOnSuccess(comicWrapper.getComic().isFavorite());
                    }else{
                        setViewStateOnError();
                    }
                }).subscribe();
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

    public void setComicFavorite(boolean isFavorite) {

        if(mComicLiveData.getValue() == null){
            return;
        }

        mFavoritesUseCase.setFavorite(mComicLiveData.getValue().getNum(), isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .doOnComplete(() -> {
                    if (isFavorite){
                        mMessage.setValue(new Event<>("Add to favorites"));
                        mState.setValue(new ComicState.Builder(mState.getValue()).setFavorite(true).build());
                    }else{
                        mMessage.setValue(new Event<>("Remove from favorites"));
                        mState.setValue(new ComicState.Builder(mState.getValue()).setFavorite(false).build());

                    }
                })
                .subscribe();
    }


    public LiveData<Comic> getComic() {
        return mComicLiveData;
    }

    public LiveData<ComicState> getState() {
        return mState;
    }

    public LiveData<Event<String>> getMessage(){
        return mMessage;
    }


}
