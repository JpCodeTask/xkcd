package pl.jpcodetask.xkcdcomics.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class ComicDetailsFragment extends Fragment {

    private static final String ARG_COMIC_NUMBER = "arg_comic_number";

    private ComicDetailsViewModel mViewModel;

    @Inject
    XkcdViewModelFactory mXkcdViewModelFactory;

    public ComicDetailsFragment(){
        //empty
    }

    public static ComicDetailsFragment newInstance(int comicNumber){
        Bundle args = new Bundle();
        args.putInt(ARG_COMIC_NUMBER, comicNumber);

        ComicDetailsFragment fragment = new ComicDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        setupBindedViewModel();
        mViewModel.loadComic(args.getInt(ARG_COMIC_NUMBER));
    }

    private void setupBindedViewModel(){
        mViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(ComicDetailsViewModel.class);

        mViewModel.getComicLiveData().observe(this, comic -> {
            Toast.makeText(getContext(), " " + comic.getTitle(), Toast.LENGTH_SHORT).show();
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comic_details, container, false);
    }
}
