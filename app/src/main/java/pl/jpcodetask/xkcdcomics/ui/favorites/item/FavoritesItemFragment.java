package pl.jpcodetask.xkcdcomics.ui.favorites.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesItemBinding;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.common.ComicViewer;
import pl.jpcodetask.xkcdcomics.utils.ComicUtils;
import pl.jpcodetask.xkcdcomics.utils.GlideApp;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class FavoritesItemFragment extends Fragment implements ComicViewer {

    public static final String EXTRA_UNFAVORITE_COMIC = "EXTRA_UNFAVORITE_COMIC";
    private static final String ARG_COMIC_NUMBER = "ARG_COMIC_NUMBER";
    @Inject
    XkcdViewModelFactory mViewModelFactory;
    private FragmentFavoritesItemBinding mBinding;
    private boolean mComicDetailsVisible = false;
    private boolean mComicIsFavorite = false;
    private FavoritesItemViewModel mViewModel;
    private MainViewModel mActivityViewModel;
    private Intent mShareIntent;

    public FavoritesItemFragment(){

    }

    public static FavoritesItemFragment newInstance(int comicNumber){
        FavoritesItemFragment fragment = new FavoritesItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COMIC_NUMBER, comicNumber);

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
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoritesItemBinding.inflate(inflater, container, false);

        setupToolbar();
        setupViewmodel();
        setHasOptionsMenu(true);

        mBinding.setViewer(this);
        mBinding.setViewmodel(mViewModel);
        mBinding.setLifecycleOwner(this);

        return mBinding.getRoot();
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }

    private void setupViewmodel() {
        mActivityViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(MainViewModel.class);
        mActivityViewModel.disableNavigationDrawer();

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FavoritesItemViewModel.class);

        mViewModel.getComic().observe(this, comic -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(comic.getTitle());
            GlideApp.with(this)
                    .load(comic.getImgUrl())
                    .onlyRetrieveFromCache(true)
                    .into(mBinding.imageView);
            setupShareIntent(comic);
        });

        mViewModel.getMessage().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

        mViewModel.getState().observe(this, comicState -> {


            if (comicState.isDataLoading()){
                mBinding.moreBtn.setEnabled(false);
            }else{
                mBinding.moreBtn.setEnabled(true);
            }

            if (comicState.isErrorOccurred()){
                mBinding.moreBtn.setEnabled(false);
            }else{
                mBinding.moreBtn.setEnabled(true);
            }

            if (comicState.isFavorite()){
                mComicIsFavorite = true;
                getActivity().invalidateOptionsMenu();
            }else{
                mComicIsFavorite = false;
                getActivity().invalidateOptionsMenu();
            }

        });
    }


    private void setupShareIntent(Comic comic) {
        mShareIntent = ComicUtils.getComicShareIntent(comic);
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.loadComic(getArguments().getInt(ARG_COMIC_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.item_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem favoriteBtn = menu.findItem(R.id.action_favorite);
        if (mComicIsFavorite){
            favoriteBtn.setTitle(R.string.menu_item_favorite);
            favoriteBtn.setIcon(R.drawable.baseline_favorite_black_24);
        }else{
            favoriteBtn.setIcon(R.drawable.baseline_favorite_border_black_24);
            favoriteBtn.setTitle(R.string.menu_item_unfavorite);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                if (mShareIntent != null){
                    startActivity(Intent.createChooser(mShareIntent, "Share comic image"));
                }
                return true;

            case R.id.action_favorite:
                if(item.getTitle() == getString(R.string.menu_item_favorite)){
                    mViewModel.setComicFavorite(false);
                }else{
                    mViewModel.setComicFavorite(true);
                }

                return true;

            default:
                return true;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityViewModel.availableNavigationDrawer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setupResult();
    }

    private void setupResult(){
        if (getTargetFragment() == null){
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_UNFAVORITE_COMIC, !mComicIsFavorite);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
    }

    @Override
    public void onComicDetails() {
        if (!mComicDetailsVisible){
            mBinding.comicDetailsView.setVisibility(View.VISIBLE);
            mComicDetailsVisible = true;
        }else{
            mBinding.comicDetailsView.setVisibility(View.GONE);
            mComicDetailsVisible = false;
        }
    }

    @Override
    public void onReload() {

    }
}
