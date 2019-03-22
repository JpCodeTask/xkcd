package pl.jpcodetask.xkcdcomics.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicDetailsBinding;
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

    private void setupBindedViewModel(final FragmentComicDetailsBinding binding){
        mViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(ComicDetailsViewModel.class);

        mViewModel.getDataLoadingLiveData().observe(this, isLoading -> {
            if (isLoading){
                binding.progressBar.setVisibility(View.VISIBLE);
            }else{
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentComicDetailsBinding binding  = FragmentComicDetailsBinding.inflate(inflater, container, false);
        setupBindedViewModel(binding);
        binding.setViewmodel(mViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadComic(getArguments().getInt(ARG_COMIC_NUMBER));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.clean();
    }
}
