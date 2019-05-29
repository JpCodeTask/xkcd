package pl.jpcodetask.xkcdcomics.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.ActivityMainBinding;
import pl.jpcodetask.xkcdcomics.ui.common.NavigationItem;
import pl.jpcodetask.xkcdcomics.ui.favorites.FavoritesFragment;
import pl.jpcodetask.xkcdcomics.ui.item.ComicFragment;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    @Inject
    XkcdViewModelFactory mXkcdViewModelFactory;

    private static final long BACK_BUTTON_EXIT_DELAY_MS = 2000;

    private ActivityMainBinding mBinding;
    private MainViewModel mMainViewModel;
    private long mBackButtonTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupViewModel();
        setupNavigationDrawer();
    }

    private void setupViewModel() {
        mMainViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getNavigationItem().observe(this, item -> {
            FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item){
                case NavigationItem.NAVIGATION_EXPLORE:

                    ComicFragment comicFragment = ComicFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .replace(mBinding.fragmentContainerOne.getId(), comicFragment)
                            .commit();

                    break;


                case NavigationItem.NAVIGATION_FAVORITES:
                    FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .replace(mBinding.fragmentContainerOne.getId(), favoritesFragment)
                            .commit();

                    break;

                case NavigationItem.NAVIGATION_ARCHIVE:


                default:
                    Toast.makeText(this, "To implement", Toast.LENGTH_SHORT).show();
            }

            mBinding.navView.getMenu().getItem(item).setChecked(true);
        });

        mBinding.setViewmodel(mMainViewModel);
        mBinding.setLifecycleOwner(this);
    }

    private void setupNavigationDrawer() {
        mBinding.navView.setNavigationItemSelectedListener(menuItem ->{
            switch (menuItem.getItemId()){
                case R.id.nav_action_explore:
                    mMainViewModel.navigateTo(NavigationItem.NAVIGATION_EXPLORE);
                    break;

                case R.id.nav_action_favorites:
                    mMainViewModel.navigateTo(NavigationItem.NAVIGATION_FAVORITES);
                    break;

                case R.id.nav_action_archive:
                    mMainViewModel.navigateTo(NavigationItem.NAVIGATION_ARCHIVE);
                    break;

                default:
                    break;
            }
            mBinding.drawer.closeDrawers();
            return true;
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStack();
                    return true;
                }

                // Open the navigation drawer when the home icon is selected from the toolbar.
                mBinding.drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(mBinding.drawer.isDrawerOpen(GravityCompat.START)){
            mBinding.drawer.closeDrawers();
            return;
        }


        if( getSupportFragmentManager().getBackStackEntryCount() > 0|| mBackButtonTimestamp + BACK_BUTTON_EXIT_DELAY_MS > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, getString(R.string.back_btn_twice_message), Toast.LENGTH_SHORT).show();
        mBackButtonTimestamp = System.currentTimeMillis();
    }

}
