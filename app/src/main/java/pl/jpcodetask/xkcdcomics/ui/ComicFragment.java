package pl.jpcodetask.xkcdcomics.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicBinding;
import pl.jpcodetask.xkcdcomics.utils.ViewModelProvider;

public class ComicFragment extends Fragment {

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ((ViewModelProvider<ComicViewModel>) Objects.requireNonNull(getActivity())).obtainViewModel(getActivity());
        mViewModel.getComicLiveData().observe(this, comic -> Toast.makeText(getContext(), "" + comic.getNum(), Toast.LENGTH_SHORT).show());
        mViewModel.getMessageEventLiveData().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

        });

        mViewModel.loadComic();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentComicBinding binding = FragmentComicBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
