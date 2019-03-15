package pl.jpcodetask.xkcdcomics.viewmodel;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.jpcodetask.xkcdcomics.ui.list.ComicListViewModel;

@Singleton
public class XkcdViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    public XkcdViewModelFactory(){

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ComicListViewModel.class)){
            return (T) new ComicListViewModel();
        }

        throw new IllegalArgumentException("Model class is not assignable");
    }
}
