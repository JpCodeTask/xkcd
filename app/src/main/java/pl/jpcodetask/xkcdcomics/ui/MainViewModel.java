package pl.jpcodetask.xkcdcomics.ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.jpcodetask.xkcdcomics.ui.common.NavigationItem;
import pl.jpcodetask.xkcdcomics.viewmodel.NetworkLiveData;

public class MainViewModel extends ViewModel {

    private final Context mContext;

    private final MutableLiveData<Integer> mNavigationItem = new MutableLiveData<>();
    private final NetworkLiveData mNetwork;
    private final MutableLiveData<Boolean> mNavigationDrawerAvailable = new MutableLiveData<>();

    public MainViewModel(Context context) {
        mContext = context;
        mNetwork = new NetworkLiveData(mContext);
        navigateTo(NavigationItem.NAVIGATION_EXPLORE);
    }

    public void navigateTo(Integer navigationItem){
        if (navigationItem.equals(mNavigationItem.getValue())){
            return;
        }

        mNavigationItem.setValue(navigationItem);
    }

    public void disableNavigationDrawer(){
        mNavigationDrawerAvailable.setValue(false);
    }

    public void availableNavigationDrawer(){
        mNavigationDrawerAvailable.setValue(true);
    }

    public LiveData<Integer> getNavigationItem(){
        return mNavigationItem;
    }

    public NetworkLiveData getNetwork(){ return mNetwork; }

    public LiveData<Boolean> getNavigationDrawerAvailable() {
        return mNavigationDrawerAvailable;
    }



}
