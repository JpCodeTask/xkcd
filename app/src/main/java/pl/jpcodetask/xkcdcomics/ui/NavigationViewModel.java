package pl.jpcodetask.xkcdcomics.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends ViewModel {

    private final MutableLiveData<Integer> mNavigationItem = new MutableLiveData<>();

    public LiveData<Integer> getNavigationItem(){
        return mNavigationItem;
    }

    public void navigateTo(Integer navigationItem){
        mNavigationItem.setValue(navigationItem);
    }

}
