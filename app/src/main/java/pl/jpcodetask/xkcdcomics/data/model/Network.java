package pl.jpcodetask.xkcdcomics.data.model;

public class Network {

    public static final int NO_NETWORK = 0;
    public static final int WIFI_NETWORK = 1;
    public static final int MOBILE_NETWORK = 2;

    private int mType;
    private boolean mIsConnected;;

    public Network(int type, boolean isConnected){
        mType = type;
        mIsConnected = isConnected;
    }

    public int getType() {
        return mType;
    }

    public boolean isConnected() {
        return mIsConnected;
    }
}
