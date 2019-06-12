package pl.jpcodetask.xkcdcomics.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.explore.ComicViewModel;
import pl.jpcodetask.xkcdcomics.ui.favorites.item.FavoritesItemViewModel;
import pl.jpcodetask.xkcdcomics.ui.favorites.list.FavoritesViewModel;
import pl.jpcodetask.xkcdcomics.usecase.ExploreUseCase;
import pl.jpcodetask.xkcdcomics.usecase.ExploreUseCaseImpl;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCaseImpl;

@Singleton
public class XkcdViewModelFactory implements ViewModelProvider.Factory {

    private final Context mContext;
    private final ExploreUseCase mExploreUseCase;
    private final FavoritesUseCase mFavoritesUseCase;
    @Inject
    public XkcdViewModelFactory(@Named("application_context") Context context, ExploreUseCaseImpl exploreUseCase, FavoritesUseCaseImpl favoritesUseCase){
        mContext = context;
        mExploreUseCase = exploreUseCase;
        mFavoritesUseCase = favoritesUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ComicViewModel.class)){
            return (T) new ComicViewModel(mExploreUseCase);
        }

        if (modelClass.isAssignableFrom(FavoritesViewModel.class)){
            return (T) new FavoritesViewModel(mFavoritesUseCase);
        }

        if (modelClass.isAssignableFrom(FavoritesItemViewModel.class)){
            return (T) new FavoritesItemViewModel(mFavoritesUseCase);
        }

        if (modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(mContext);
        }
        throw new IllegalArgumentException("Model class is not assignable");
    }
}
