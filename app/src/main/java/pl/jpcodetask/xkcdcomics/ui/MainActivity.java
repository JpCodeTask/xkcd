package pl.jpcodetask.xkcdcomics.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
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
import pl.jpcodetask.xkcdcomics.ui.archive.ArchiveFragment;
import pl.jpcodetask.xkcdcomics.ui.common.NavigationItem;
import pl.jpcodetask.xkcdcomics.ui.explore.ComicFragment;
import pl.jpcodetask.xkcdcomics.ui.favorites.list.FavoritesFragment;
import pl.jpcodetask.xkcdcomics.ui.settings.SettingsFragment;
import pl.jpcodetask.xkcdcomics.utils.UpdateJobService;
import pl.jpcodetask.xkcdcomics.utils.Utils;
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

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupViewModel();
        setupNavigationDrawer();

        UpdateJobService.schedule(getApplicationContext());
    }

    private void setupViewModel() {
        mMainViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getNavigationItem().observe(this, item -> {
            FragmentManager fragmentManager = getSupportFragmentManager();

            if(fragmentManager.getBackStackEntryCount() > 0){
                return;
            }

            switch (item){
                case NavigationItem.NAVIGATION_EXPLORE:

                    ComicFragment comicFragment = ComicFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(mBinding.fragmentContainerOne.getId(), comicFragment)
                            .commit();

                    break;


                case NavigationItem.NAVIGATION_FAVORITES:
                    FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(mBinding.fragmentContainerOne.getId(), favoritesFragment)
                            .commit();

                    break;

                case NavigationItem.NAVIGATION_ARCHIVE:

                    ArchiveFragment archiveFragment = ArchiveFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(mBinding.fragmentContainerOne.getId(), archiveFragment)
                            .commit();
                    break;

                case NavigationItem.NAVIGATION_SETTINGS:
                    SettingsFragment settingsFragment = SettingsFragment.newInstance();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(mBinding.fragmentContainerOne.getId(), settingsFragment)
                            .commit();
                    break;

                case NavigationItem.NAVIGATION_FEEDBACK:
                    startActivity(Intent.createChooser(Utils.getFeedbackIntent(getApplicationContext()), getString(R.string.send_feedback)));
                    break;


                default:
                    Toast.makeText(this, "To implement", Toast.LENGTH_SHORT).show();
            }

            if(item != NavigationItem.NAVIGATION_FEEDBACK){
                mBinding.navView.getMenu().getItem(item).setChecked(true);
            }
        });

        mMainViewModel.getNavigationDrawerAvailable().observe(this, isAvailable -> {
            if (isAvailable){
                mBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }else{
                mBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
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

                case R.id.nav_action_settings:
                    mMainViewModel.navigateTo(NavigationItem.NAVIGATION_SETTINGS);
                    break;

                case R.id.nav_action_feedback:
                    mMainViewModel.navigateTo(NavigationItem.NAVIGATION_FEEDBACK);
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


        if( getSupportFragmentManager().getBackStackEntryCount() > 0 || mBackButtonTimestamp + BACK_BUTTON_EXIT_DELAY_MS > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, getString(R.string.back_btn_twice_message), Toast.LENGTH_SHORT).show();
        mBackButtonTimestamp = System.currentTimeMillis();
    }

}
