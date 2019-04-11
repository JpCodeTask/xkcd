package pl.jpcodetask.xkcdcomics.viewmodel;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.item.ComicViewModel;
import pl.jpcodetask.xkcdcomics.utils.SharedPreferenceProvider;

@Singleton
public class XkcdViewModelFactory implements ViewModelProvider.Factory {

    private final Context mContext;
    private final DataSource mRepository;
    private final SharedPreferenceProvider mSharedPreferenceProvider;

    @Inject
    public XkcdViewModelFactory(@Named("application_context") Context context, @Named("repository") DataSource repository, SharedPreferenceProvider preferenceProvider){
        mContext = context;
        mRepository = repository;
        mSharedPreferenceProvider = preferenceProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ComicViewModel.class)){
            return (T) new ComicViewModel(mRepository, mSharedPreferenceProvider);
        }

        if (modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(mContext);
        }
        throw new IllegalArgumentException("Model class is not assignable");
    }
}
