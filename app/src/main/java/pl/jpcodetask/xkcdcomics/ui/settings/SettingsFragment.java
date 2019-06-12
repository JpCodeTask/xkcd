package pl.jpcodetask.xkcdcomics.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding mBinding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false);

        setupToolbar();
        setupPreferenceFragment();
        setHasOptionsMenu(true);
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }


    private void setupToolbar() {
        mBinding.toolbar.setTitle(R.string.nav_item_settings);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }

    private void setupPreferenceFragment() {
        getChildFragmentManager().beginTransaction()
                .add(mBinding.preferenceContainer.getId(), PreferencesFragment.newInstance())
                .commit();
    }



    private static class PreferencesFragment extends PreferenceFragmentCompat{


        static PreferencesFragment newInstance() {
            return new PreferencesFragment();
        }


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.settings);
        }

    }
}
