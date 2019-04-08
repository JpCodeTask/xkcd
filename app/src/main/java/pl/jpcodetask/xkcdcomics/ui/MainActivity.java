package pl.jpcodetask.xkcdcomics.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.ActivityMainBinding;
import pl.jpcodetask.xkcdcomics.utils.ViewModelProvider;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, ViewModelProvider<ComicViewModel> {

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    @Inject
    XkcdViewModelFactory mViewModelFactory;

    private ActivityMainBinding mBinding;
    private ComicViewModel mComicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupViewModel();
        setupToolbar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragmentManager.beginTransaction()
                    .add(mBinding.fragmentContainer.getId(), ComicFragment.newInstance())
                    .commit();
        }
    }

    private void setupViewModel() {
        mComicViewModel = obtainViewModel(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbar);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mBinding.drawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_favorite:
                if(item.getTitle() == getString(R.string.menu_item_favorite)){
                    mComicViewModel.setComicFavorite(true);
                    item.setTitle(R.string.menu_item_unfavorite);
                    item.setIcon(R.drawable.baseline_favorite_black_24);
                }else{
                    mComicViewModel.setComicFavorite(false);
                    item.setTitle(R.string.menu_item_favorite);
                    item.setIcon(R.drawable.baseline_favorite_border_black_24);
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ComicViewModel obtainViewModel(FragmentActivity fragmentActivity) {
        return ViewModelProviders.of(fragmentActivity, mViewModelFactory).get(ComicViewModel.class);
    }
}
