package pl.jpcodetask.xkcdcomics.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.lifecycle.LiveData;
import pl.jpcodetask.xkcdcomics.data.model.Network;

public class NetworkLiveData extends LiveData<Network> {

    private final Context mContext;

    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!=null) {
                NetworkInfo activeNetwork = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected) {
                    switch (activeNetwork.getType()){
                        case ConnectivityManager.TYPE_WIFI:
                            postValue(new Network(Network.WIFI_NETWORK,true));
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            postValue(new Network(Network.MOBILE_NETWORK,true));
                            break;
                    }
                } else {
                    postValue(new Network(Network.NO_NETWORK,false));
                }
            }
        }
    };

    @Inject
    public NetworkLiveData(@Named("application_context") Context context){
        mContext = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter intentFilter  = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mNetworkReceiver, intentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mContext.unregisterReceiver(mNetworkReceiver);
    }
}
