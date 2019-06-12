package pl.jpcodetask.xkcdcomics.ui.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import pl.jpcodetask.xkcdcomics.Event;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.usecase.BaseUseCase;
import pl.jpcodetask.xkcdcomics.usecase.Likeable;
import pl.jpcodetask.xkcdcomics.utils.Schedulers;

public abstract class BaseItemViewModel extends ViewModel {

    protected final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /** State*/
    protected final MutableLiveData<ComicState> mState = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> mIsFullscreen = new MutableLiveData<>();

    /** Data*/
    protected final MutableLiveData<Comic> mComicLiveData = new MutableLiveData<>();
    protected final MutableLiveData<Event<String>> mMessage = new MutableLiveData<>();



    public void setComicFavorite(boolean isFavorite) {

        if(mComicLiveData.getValue() == null){
            return;
        }

        mCompositeDisposable.add(((Likeable) getUseCase()).setFavorite(mComicLiveData.getValue().getNum(), isFavorite)
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
                .subscribe());
    }


    public void fullscreen() {
        if (mIsFullscreen.getValue() != null){
            mIsFullscreen.setValue(!mIsFullscreen.getValue());
        }else{
            mIsFullscreen.setValue(true);
        }
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();
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

    public LiveData<Boolean> getIsFullscreen() {
        return mIsFullscreen;
    }


    protected abstract BaseUseCase getUseCase();
}
