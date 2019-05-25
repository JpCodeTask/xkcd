package pl.jpcodetask.xkcdcomics.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBindingImpl;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding mBinding;

    public FavoritesFragment(){
        //empty
    }

    public static FavoritesFragment newInstance(){
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoritesBindingImpl.inflate(inflater, container, false);
        return mBinding.getRoot();
    }
}
