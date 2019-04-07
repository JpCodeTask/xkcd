package pl.jpcodetask.xkcdcomics.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicBinding;
import pl.jpcodetask.xkcdcomics.utils.ViewModelProvider;

public class ComicFragment extends Fragment implements ComicNavigator{

    private ComicViewModel mViewModel;

    public ComicFragment(){
        //empty
    }


    static ComicFragment newInstance(){
        return new ComicFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentComicBinding binding = FragmentComicBinding.inflate(inflater, container, false);

        mViewModel = ((ViewModelProvider<ComicViewModel>) Objects.requireNonNull(getActivity())).obtainViewModel(getActivity());

        mViewModel.getComic().observe(this, comic -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(comic.getTitle());
            Glide.with(this).load(comic.getImgUrl()).into(binding.imageView);
        });

        mViewModel.getMessageEvent().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

        });

        binding.setLifecycleOwner(getActivity());
        binding.setViewmodel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadComic();
    }

    @Override
    public void onNext(int currentComicNumber) {

    }

    @Override
    public void onPrev(int currentComicNumber) {

    }

    @Override
    public void onGoTo(int comicNumber) {

    }

    @Override
    public void showDetails() {

    }

    @Override
    public void hideDetails() {

    }
}
