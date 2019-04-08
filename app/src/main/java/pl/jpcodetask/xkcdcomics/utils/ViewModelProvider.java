package pl.jpcodetask.xkcdcomics.utils;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

public interface ViewModelProvider<T extends ViewModel> {
    T obtainViewModel(FragmentActivity context);
}
