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

        mViewModel.getIsDataLoading().observe(this, isDataLoading ->{
            if(isDataLoading){
                binding.progressBar.setVisibility(View.VISIBLE);
            }else{
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        mViewModel.getComicLiveData().observe(this, comic -> {
            binding.comicNumberTextView.setText(String.valueOf(comic.getNum()));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(comic.getTitle());
            Glide.with(this).load(comic.getImgUrl()).into(binding.imageView);
        });

        mViewModel.getMessageEventLiveData().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

        });

        mViewModel.getIsLatest().observe(this, isLatest ->{
            if (isLatest){
                binding.nextBtn.setEnabled(false);
            }else{
                binding.nextBtn.setEnabled(true);
            }
        });

        mViewModel.loadComic();

        return binding.getRoot();
    }

    @Override
    public void onNext(int currentComicNumber) {

    }

    @Override
    public void onPrev(int currentComicNumber) {

    }

    @Override
    public void onGoTo(int currentComicNumber) {

    }

    @Override
    public void showDetails() {

    }

    @Override
    public void hideDetails() {

    }
}
