package pl.jpcodetask.xkcdcomics.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class XkcdService extends IntentService {

    private static final String NAME = XkcdService.class.getSimpleName();

    public XkcdService() {
        super(NAME);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, XkcdService.class);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
