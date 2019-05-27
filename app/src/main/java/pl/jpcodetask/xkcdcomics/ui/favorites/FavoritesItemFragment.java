package pl.jpcodetask.xkcdcomics.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.jpcodetask.xkcdcomics.R;

public class FavoritesItemFragment extends Fragment {

    public FavoritesItemFragment(){

    }

    public static FavoritesItemFragment newInstance(){
        return new FavoritesItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites_item, container, false);
    }
}
