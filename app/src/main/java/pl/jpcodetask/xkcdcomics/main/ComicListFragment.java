package pl.jpcodetask.xkcdcomics.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pl.jpcodetask.xkcdcomics.R;

public class ComicListFragment extends Fragment {

    public ComicListFragment(){
        //empty
    }

    public static ComicListFragment newInstance(){
        return new ComicListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comic_list, container, false);
    }
}
