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

    public MainViewModel(Context context) {
        mContext = context;
        mNetwork = new NetworkLiveData(mContext);
        navigateTo(NavigationItem.NAVIGATION_EXPLORE);
    }

    public LiveData<Integer> getNavigationItem(){
        return mNavigationItem;
    }

    public NetworkLiveData getNetwork(){ return mNetwork; }

    public void navigateTo(Integer navigationItem){
        if (navigationItem.equals(mNavigationItem.getValue())){
            return;
        }

        mNavigationItem.setValue(navigationItem);
    }

}
