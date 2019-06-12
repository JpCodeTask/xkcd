package pl.jpcodetask.xkcdcomics.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jpcodetask.xkcdcomics.ui.archive.ArchiveFragment;
import pl.jpcodetask.xkcdcomics.ui.explore.ComicFragment;
import pl.jpcodetask.xkcdcomics.ui.favorites.item.FavoritesItemFragment;
import pl.jpcodetask.xkcdcomics.ui.favorites.list.FavoritesFragment;

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract ComicFragment contributeComicFragmentInjector();

    @ContributesAndroidInjector
    abstract FavoritesFragment contributeFavoritesFragmentInjector();

    @ContributesAndroidInjector
    abstract FavoritesItemFragment contributeFavoritesItemFragmentInjector();

    @ContributesAndroidInjector
    abstract ArchiveFragment contributeArchiveFragmentInjector();
}
