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

    public int getKeyLatestComicNumber(int defaultValue) {
        return mSharedPreferences.getInt(KEY_LATEST_COMIC_NUMBER, defaultValue);
    }
}