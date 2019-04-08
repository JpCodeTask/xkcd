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
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;



    private ActivityMainBinding mBinding;
    private ComicViewModel mComicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupNavigationDrawer();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragmentManager.beginTransaction()
                    .add(mBinding.fragmentContainer.getId(), ComicFragment.newInstance())
                    .commit();
        }
    }

    private void setupNavigationDrawer() {
        mBinding.navView.getMenu().getItem(0).setChecked(true);
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
            menuItem.setChecked(true);
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
}
