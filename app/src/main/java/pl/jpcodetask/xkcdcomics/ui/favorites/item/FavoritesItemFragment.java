package pl.jpcodetask.xkcdcomics.ui.favorites.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesItemBinding;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.common.ComicViewer;
import pl.jpcodetask.xkcdcomics.ui.common.SwipeContract;
import pl.jpcodetask.xkcdcomics.utils.GlideApp;
import pl.jpcodetask.xkcdcomics.utils.Utils;
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
    private boolean mIsFullscreen = false;

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
        setupImageListener();
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
        setupActivityViewModel();
        initViewModel();
        observeComic();
        observeMessages();
        observeState();
        observeFullscreen();
    }

    private void setupActivityViewModel(){
        mActivityViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(MainViewModel.class);
        mActivityViewModel.disableNavigationDrawer();
    }

    private void initViewModel(){
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FavoritesItemViewModel.class);
    }

    private void observeComic() {
        mViewModel.getComic().observe(this, comic -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(comic.getTitle());
            GlideApp.with(this)
                    .asBitmap()
                    .load(comic.getImgUrl())
                    .onlyRetrieveFromCache(true)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            setupShareIntent(resource);
                            return false;
                        }
                    })
                    .into(mBinding.imageView);
        });
    }


    private void observeMessages() {
        mViewModel.getMessage().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeState(){
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

    private void observeFullscreen(){
        mViewModel.getIsFullscreen().observe(this, isFullscreen ->{
            if (isFullscreen){
                animateOnFullscreenOn();
            }else{
                animateOnFullscreenOff();
            }
        });
    }

    private void animateOnFullscreenOn(){
        TransitionManager.beginDelayedTransition((ViewGroup) mBinding.getRoot(), new ChangeBounds());
        ConstraintSet set  = new ConstraintSet();
        set.clone((ConstraintLayout) mBinding.getRoot());

        set.clear(mBinding.imageView.getId(), ConstraintSet.TOP);
        set.clear(mBinding.imageView.getId(), ConstraintSet.BOTTOM);
        set.connect(mBinding.imageView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        set.connect(mBinding.imageView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        //visibility
        set.setVisibility(mBinding.appBarLayout.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.moreBtn.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.comicNumberTv.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.comicDetailsView.getId(), ConstraintSet.GONE);

        set.applyTo((ConstraintLayout) mBinding.getRoot());
    }

    private void animateOnFullscreenOff(){
        TransitionManager.beginDelayedTransition((ViewGroup) mBinding.getRoot(), new ChangeBounds());
        ConstraintSet set  = new ConstraintSet();
        set.clone((ConstraintLayout) mBinding.getRoot());

        //visibility
        set.setVisibility(mBinding.appBarLayout.getId(), ConstraintSet.VISIBLE);
        set.setVisibility(mBinding.moreBtn.getId(), ConstraintSet.VISIBLE);
        set.setVisibility(mBinding.comicNumberTv.getId(), ConstraintSet.VISIBLE);


        set.connect(mBinding.imageView.getId(), ConstraintSet.BOTTOM, mBinding.moreBtn.getId(), ConstraintSet.TOP);
        set.connect(mBinding.imageView.getId(), ConstraintSet.TOP, mBinding.appBarLayout.getId(), ConstraintSet.BOTTOM);

        set.applyTo((ConstraintLayout) mBinding.getRoot());
    }


    private void setupShareIntent(Bitmap res) {
        mShareIntent = Utils.getComicShareIntent(getContext(), res);
    }

    private void setupImageListener() {
        mBinding.imageView.setOnPhotoTapListener((view, x, y) -> {
            onFullscreen();
        });

        mBinding.imageView.setOnSingleFlingListener((e1, e2, velocityX, velocityY) -> {
            final float dx = Math.abs(e1.getX() - e2.getX());

            if (dx > SwipeContract.MAX_DX){
                return true;
            }
            if (e1.getY() - e2.getY() < 0 && velocityY > SwipeContract.MIN_VELOCITY_Y ){
                getActivity().getSupportFragmentManager().popBackStack();
            }

            return true;
        });
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
                    startActivity(Intent.createChooser(mShareIntent, getString(R.string.share_comic_title)));
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

    @Override
    public void onFullscreen() {
        mViewModel.fullscreen();
    }
}
