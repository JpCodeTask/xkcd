package pl.jpcodetask.xkcdcomics;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import pl.jpcodetask.xkcdcomics.di.DaggerAppComponent;

public class XkcdApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> mDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mDispatchingAndroidInjector;
    }
}
