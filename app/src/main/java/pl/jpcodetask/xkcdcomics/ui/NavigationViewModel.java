package pl.jpcodetask.xkcdcomics.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends ViewModel {

    private final MutableLiveData<NavigationItem> mNavigationItem = new MutableLiveData<>();

    public LiveData<NavigationItem> getNavigationItem(){
        return mNavigationItem;
    }

    public void navigateTo(NavigationItem navigationItem){
        mNavigationItem.setValue(navigationItem);
    }

}
