package pl.jpcodetask.xkcdcomics.viewmodel;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.item.ComicViewModel;
import pl.jpcodetask.xkcdcomics.usecase.ExploreUseCase;
import pl.jpcodetask.xkcdcomics.usecase.ExploreUseCaseImpl;

@Singleton
public class XkcdViewModelFactory implements ViewModelProvider.Factory {

    private final Context mContext;
    private final ExploreUseCase mExploreUseCase;

    @Inject
    public XkcdViewModelFactory(@Named("application_context") Context context, ExploreUseCaseImpl exploreUseCase){
        mContext = context;
        mExploreUseCase = exploreUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ComicViewModel.class)){
            return (T) new ComicViewModel(mExploreUseCase);
        }

        if (modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(mContext);
        }
        throw new IllegalArgumentException("Model class is not assignable");
    }
}
