package pl.jpcodetask.xkcdcomics.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.ActivityMainBinding;
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

        mMainViewModel.navigateTo(NavigationItem.NAVIGATION_EXPLORE);

    }

    private void setupViewModel() {
        mMainViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getNavigationItem().observe(this, item -> {
            switch (item){
                case NavigationItem.NAVIGATION_EXPLORE:

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
                    if(fragment == null){
                        ComicFragment comicFragment = ComicFragment.newInstance();
                        fragmentManager.beginTransaction()
                                .add(mBinding.fragmentContainer.getId(), comicFragment)
                                .commit();
                    }
                    break;

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
                    break;

                case R.id.nav_action_favorites:
                    Toast.makeText(this, "To implement", Toast.LENGTH_SHORT).show();
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
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mBinding.drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(mBackButtonTimestamp + BACK_BUTTON_EXIT_DELAY_MS > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, getString(R.string.back_btn_twice_message), Toast.LENGTH_SHORT).show();
        mBackButtonTimestamp = System.currentTimeMillis();
    }

}
