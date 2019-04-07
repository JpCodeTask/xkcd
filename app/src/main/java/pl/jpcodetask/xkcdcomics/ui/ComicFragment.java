package pl.jpcodetask.xkcdcomics.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicBinding;
import pl.jpcodetask.xkcdcomics.utils.GlideApp;
import pl.jpcodetask.xkcdcomics.utils.ViewModelProvider;

public class ComicFragment extends Fragment implements ComicNavigator{

    private ComicViewModel mViewModel;
    private FragmentComicBinding mBinding;

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
        mBinding = FragmentComicBinding.inflate(inflater, container, false);

        setupViewModel();

        mBinding.setLifecycleOwner(getActivity());
        mBinding.setViewmodel(mViewModel);
        mBinding.setNavigator(this);
        return mBinding.getRoot();
    }


    private void setupViewModel(){
        mViewModel = ((ViewModelProvider<ComicViewModel>) Objects.requireNonNull(getActivity())).obtainViewModel(getActivity());

        mViewModel.getComic().observe(this, comic -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(comic.getTitle());
            GlideApp.with(this).load(comic.getImgUrl()).into(mBinding.imageView);
        });

        mViewModel.getMessageEvent().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

        });

        mViewModel.getIsDetailsVisible().observe(this, isDetailsVisible ->{
            if(isDetailsVisible){
                slideOut();
            }else{
                slideIn();
            }
        });
    }

    private void slideOut(){

        mBinding.randomFloatingBtn.setVisibility(View.VISIBLE);
       /* TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                mBinding.comicDetailsView.getHeight(),
                0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        mBinding.comicDetailsView.startAnimation(animate);*/
        mBinding.comicDetailsView.setVisibility(View.GONE);
    }

    private void slideIn(){

        mBinding.comicDetailsView.setVisibility(View.VISIBLE);
       /* TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                mBinding.comicDetailsView.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        mBinding.comicDetailsView.startAnimation(animate);*/
        mBinding.randomFloatingBtn.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadComic();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onPrev() {

    }

    @Override
    public void onGoTo(int comicNumber) {

    }

    @Override
    public void onComicDetails() {
        mViewModel.comicDetails();
    }

    @Override
    public void onRandom() {

    }

}
