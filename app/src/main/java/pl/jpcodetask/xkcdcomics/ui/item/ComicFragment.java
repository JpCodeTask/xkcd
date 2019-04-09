package pl.jpcodetask.xkcdcomics.ui.item;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicBinding;
import pl.jpcodetask.xkcdcomics.ui.NavigationItem;
import pl.jpcodetask.xkcdcomics.ui.NavigationViewModel;
import pl.jpcodetask.xkcdcomics.utils.GlideApp;
import pl.jpcodetask.xkcdcomics.utils.ViewModelProvider;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class ComicFragment extends Fragment implements ComicNavigator {

    @Inject
    XkcdViewModelFactory mViewModelFactory;

    private ComicViewModel mViewModel;
    private FragmentComicBinding mBinding;

    public ComicFragment(){
        //empty
    }


    public static ComicFragment newInstance(){
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
        setupToolbar();
        setHasOptionsMenu(true);

        mBinding.setLifecycleOwner(getActivity());
        mBinding.setViewmodel(mViewModel);
        mBinding.setNavigator(this);


        return mBinding.getRoot();
    }


    private void setupViewModel(){
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ComicViewModel.class);

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

    private void setupToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if(item.getTitle() == getString(R.string.menu_item_favorite)){
                    mViewModel.setComicFavorite(true);
                    item.setTitle(R.string.menu_item_unfavorite);
                    item.setIcon(R.drawable.baseline_favorite_black_24);
                }else{
                    mViewModel.setComicFavorite(false);
                    item.setTitle(R.string.menu_item_favorite);
                    item.setIcon(R.drawable.baseline_favorite_border_black_24);
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
