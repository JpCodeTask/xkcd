package pl.jpcodetask.xkcdcomics.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jpcodetask.xkcdcomics.utils.XkcdService;

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract XkcdService contributeXkcdService();
}
