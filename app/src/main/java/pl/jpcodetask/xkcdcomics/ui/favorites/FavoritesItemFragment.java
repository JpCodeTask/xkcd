package pl.jpcodetask.xkcdcomics.ui.favorites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesItemBinding;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class FavoritesItemFragment extends Fragment {

    private static final String ARG_COMIC_NUMBER = "ARG_COMIC_NUMBER";
    @Inject
    XkcdViewModelFactory mViewModelFactory;
    private FragmentFavoritesItemBinding mBinding;

    public FavoritesItemFragment(){

    }

    public static FavoritesItemFragment newInstance(int comicNumber){
        FavoritesItemFragment fragment = new FavoritesItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COMIC_NUMBER, comicNumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoritesItemBinding.inflate(inflater, container, false);

        setupToolbar();
        setHasOptionsMenu(true);
        mBinding.setLifecycleOwner(this);

        return mBinding.getRoot();
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.item_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_favorite:
                Toast.makeText(getContext(), "Favorite", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return true;
        }
    }
}
