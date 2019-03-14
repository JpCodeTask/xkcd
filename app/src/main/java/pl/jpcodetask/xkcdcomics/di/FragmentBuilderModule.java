package pl.jpcodetask.xkcdcomics.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jpcodetask.xkcdcomics.main.ComicListFragment;

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract ComicListFragment contributeComicListFragmentInjector();
}
