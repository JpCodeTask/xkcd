package pl.jpcodetask.xkcdcomics.viewmodel;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.favorites.FavoritesViewModel;
import pl.jpcodetask.xkcdcomics.ui.item.ComicViewModel;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCaseImpl;
import pl.jpcodetask.xkcdcomics.usecase.SingleComicUseCase;
import pl.jpcodetask.xkcdcomics.usecase.SingleComicUseCaseImpl;

@Singleton
public class XkcdViewModelFactory implements ViewModelProvider.Factory {

    private final Context mContext;
    private final SingleComicUseCase mSingleComicUseCase;
    private final FavoritesUseCase mFavoritesUseCase;

    @Inject
    public XkcdViewModelFactory(@Named("application_context") Context context, SingleComicUseCaseImpl exploreUseCase, FavoritesUseCaseImpl favoritesUseCase){
        mContext = context;
        mSingleComicUseCase = exploreUseCase;
        mFavoritesUseCase = favoritesUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ComicViewModel.class)){
            return (T) new ComicViewModel(mSingleComicUseCase);
        }

        if (modelClass.isAssignableFrom(FavoritesViewModel.class)){
            return (T) new FavoritesViewModel(mFavoritesUseCase);
        }


        if (modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(mContext);
        }
        throw new IllegalArgumentException("Model class is not assignable");
    }
}
