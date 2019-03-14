package pl.jpcodetask.xkcdcomics.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jpcodetask.xkcdcomics.main.MainActivity;

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
            modules = {FragmentBuilderModule.class}
    )
    abstract MainActivity contributeMainActivityInjector();
}
