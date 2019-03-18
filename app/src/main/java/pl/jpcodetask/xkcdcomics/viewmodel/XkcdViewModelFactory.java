package pl.jpcodetask.xkcdcomics.viewmodel;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.ui.details.ComicDetailsViewModel;
import pl.jpcodetask.xkcdcomics.ui.list.ComicListViewModel;

@Singleton
public class XkcdViewModelFactory implements ViewModelProvider.Factory {

    private final DataSource mRepository;

    @Inject
    public XkcdViewModelFactory(@Named("repository") DataSource repository){
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ComicListViewModel.class)){
            return (T) new ComicListViewModel(mRepository);
        }

        if (modelClass.isAssignableFrom(ComicListViewModel.class)){
            return (T) new ComicDetailsViewModel(mRepository);
        }

        throw new IllegalArgumentException("Model class is not assignable");
    }
}
