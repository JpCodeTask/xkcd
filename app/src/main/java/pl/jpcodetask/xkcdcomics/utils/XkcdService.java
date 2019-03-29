package pl.jpcodetask.xkcdcomics.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import dagger.android.AndroidInjection;

public class XkcdService extends IntentService {

    private static final String NAME = XkcdService.class.getSimpleName();

    public XkcdService() {
        super(NAME);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, XkcdService.class);
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
