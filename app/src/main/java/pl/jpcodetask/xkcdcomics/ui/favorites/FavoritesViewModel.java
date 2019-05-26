package pl.jpcodetask.xkcdcomics.ui.favorites;

import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;

public class FavoritesViewModel extends ViewModel {

    private final FavoritesUseCase mFavoritesUseCase;

    public FavoritesViewModel(FavoritesUseCase favoritesUseCase){
        mFavoritesUseCase = favoritesUseCase;
    }
}
