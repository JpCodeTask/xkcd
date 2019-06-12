package pl.jpcodetask.xkcdcomics.ui.archive;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentArchiveBinding;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class ArchiveFragment extends Fragment {

    @Inject
    XkcdViewModelFactory mViewModelFactory;

    private FragmentArchiveBinding mBinding;

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentArchiveBinding.inflate(inflater, container, false);

        setupToolbar();

        setHasOptionsMenu(true);
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }

    private void setupToolbar() {
        mBinding.toolbar.setTitle(R.string.nav_item_archive);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }
}
