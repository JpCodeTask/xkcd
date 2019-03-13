package pl.jpcodetask.xkcdcomics;

import android.app.Application;

import pl.jpcodetask.xkcdcomics.di.DaggerAppComponent;

public class XkcdApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }
}
