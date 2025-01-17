package pl.jpcodetask.xkcdcomics.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class SharedPreferenceProvider {

    private static final String KEY_LATEST_COMIC_NUMBER = "latest_comic_number";
    private static final String KEY_BOOKMARK_COMIC_NUMBER = "bookmark_comic_number";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    private static final String KEY_FAVORITES_SORT = "sort_by";
    private static final String KEY_NOTIFY_ON = "notify_on";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public SharedPreferenceProvider(@Named("application_context") Context context){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLatestComicNumber(int latestComicNumber){
        mSharedPreferences.edit()
                .putInt(KEY_LATEST_COMIC_NUMBER, latestComicNumber)
                .apply();
    }

    public int getLatestComicNumber(int defaultValue) {
        return mSharedPreferences.getInt(KEY_LATEST_COMIC_NUMBER, defaultValue);
    }


    public void setBookmarkComicNumber(int latestComicNumber){
        mSharedPreferences.edit()
                .putInt(KEY_BOOKMARK_COMIC_NUMBER, latestComicNumber)
                .apply();
    }

    public int getBookmarkComicNumber(int defaultValue) {
        return mSharedPreferences.getInt(KEY_BOOKMARK_COMIC_NUMBER, defaultValue);
    }

    public boolean isFirstLaunch(){
        return mSharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(){
        mSharedPreferences.edit()
                .putBoolean(KEY_FIRST_LAUNCH, false)
                .apply();
    }

    public int getFavoritesSortField(int defaultValue){
        return mSharedPreferences.getInt(KEY_FAVORITES_SORT, defaultValue);
    }

    public void setKeyFavoritesSort(int sortField){
        mSharedPreferences.edit()
                .putInt(KEY_FAVORITES_SORT, sortField)
                .apply();
    }

    public boolean isNotifyOn() {
        return mSharedPreferences.getBoolean(KEY_NOTIFY_ON, true);
    }
}
