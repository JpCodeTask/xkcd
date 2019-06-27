package pl.jpcodetask.xkcdcomics.ui.explore;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentExploreBinding;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.common.BackPressedHandler;
import pl.jpcodetask.xkcdcomics.ui.common.ComicNavigator;
import pl.jpcodetask.xkcdcomics.ui.common.NavigationItem;
import pl.jpcodetask.xkcdcomics.ui.common.SwipeContract;
import pl.jpcodetask.xkcdcomics.utils.GlideApp;
import pl.jpcodetask.xkcdcomics.utils.Utils;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class ComicFragment extends Fragment implements ComicNavigator {

    private static final String TAG = ComicFragment.class.getSimpleName();
    @Inject
    XkcdViewModelFactory mViewModelFactory;

    private ComicViewModel mViewModel;
    private MainViewModel mActivityViewModel;
    private FragmentExploreBinding mBinding;

    private Bitmap mBitmapToShare;

    private boolean mExecuteSpinnerSelected = false;
    private boolean mComicDetailsVisible = false;
    private boolean mComicIsFavorite = false;
    private boolean mSwipeNextAvailable = false;
    private boolean mSwipePrevAvailable = false;

    private ArrayAdapter<Integer> mArrayAdapter;

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
        mBinding = FragmentExploreBinding.inflate(inflater, container, false);

        setupViewModel();
        setupToolbar();
        setHasOptionsMenu(true);
        setupSpinner();
        setupArchiveBtn();
        setupImageListener();

        mBinding.setLifecycleOwner(getActivity());
        mBinding.setViewmodel(mViewModel);
        mBinding.setActivityviewmodel(mActivityViewModel);
        mBinding.setNavigator(this);

        return mBinding.getRoot();
    }


    private void setupViewModel(){
        setupActivityViewModel();
        initViewModel();
        observeComic();
        observeMessages();
        observeState();
        observeFullscreen();
        observeDataState();
    }

    private void setupActivityViewModel(){
        mActivityViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(MainViewModel.class);
    }

    private void initViewModel(){
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ComicViewModel.class);
    }

    private void observeComic(){
        mViewModel.getComic().observe(this, comic -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(comic.getTitle());
            GlideApp.with(this)
                    .asBitmap()
                    .load(comic.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            mBitmapToShare = resource;
                            return false;
                        }
                    })
                    .into(mBinding.imageView);

        });

    }

    private void observeMessages(){
        mViewModel.getMessage().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerSelectionWithoutCallback(int comicNumber){
        mExecuteSpinnerSelected = false;

        if (comicNumber > mArrayAdapter.getCount()){
            mArrayAdapter.clear();
            mArrayAdapter.addAll(mViewModel.getComicRange());
            mArrayAdapter.notifyDataSetChanged();
        }

        mBinding.comicNumberSpinner.setSelection(comicNumber - 1);
    }

    private void observeState(){
        mViewModel.getState().observe(this, comicState -> {

            if (comicState.isNextAvailable()){
                if (mActivityViewModel.getNetwork().getValue() == null || mActivityViewModel.getNetwork().getValue().isConnected()){
                    mSwipeNextAvailable = true;
                }

                mBinding.nextBtn.setEnabled(true);
            }else{
                if (mActivityViewModel.getNetwork().getValue() == null || mActivityViewModel.getNetwork().getValue().isConnected()) {
                    mSwipeNextAvailable = false;
                }

                mBinding.nextBtn.setEnabled(false);
            }

            if (comicState.isPrevAvailable()){
                if (mActivityViewModel.getNetwork().getValue() == null || mActivityViewModel.getNetwork().getValue().isConnected()) {
                    mSwipePrevAvailable = true;
                }

                mBinding.prevBtn.setEnabled(true);
            }else{
                if (mActivityViewModel.getNetwork().getValue() == null || mActivityViewModel.getNetwork().getValue().isConnected()){
                    mSwipePrevAvailable = false;
                }
                mBinding.prevBtn.setEnabled(false);
            }

            if (comicState.isDataLoading()){
                mBinding.moreBtn.setEnabled(false);
                mBinding.randomFloatingBtn.setEnabled(false);
                mBinding.comicNumberSpinner.setEnabled(false);
            }else{
                mBinding.moreBtn.setEnabled(true);
                mBinding.randomFloatingBtn.setEnabled(true);
                mBinding.comicNumberSpinner.setEnabled(true);
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
                ((BackPressedHandler) getActivity()).setOnBackPressedCallback(this::onFullscreen);
                animateOnFullscreenOn();
            }else{
                ((BackPressedHandler) getActivity()).setOnBackPressedCallbackDefault();
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
        set.setVisibility(mBinding.archiveBtn.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.prevBtn.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.nextBtn.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.comicNumberSpinner.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.randomFloatingBtn.getId(), ConstraintSet.GONE);
        set.setVisibility(mBinding.comicDetailsView.getId(), ConstraintSet.GONE);

        set.applyTo((ConstraintLayout) mBinding.getRoot());
    }

    private void animateOnFullscreenOff(){
        TransitionManager.beginDelayedTransition((ViewGroup) mBinding.getRoot(), new ChangeBounds());
        ConstraintSet set  = new ConstraintSet();
        set.clone((ConstraintLayout) mBinding.getRoot());

        //visibility
        if (!mActivityViewModel.getNetwork().getValue().isConnected()){
            set.setVisibility(mBinding.archiveBtn.getId(), ConstraintSet.VISIBLE);
        }else{
            set.setVisibility(mBinding.prevBtn.getId(), ConstraintSet.VISIBLE);
            set.setVisibility(mBinding.nextBtn.getId(), ConstraintSet.VISIBLE);
            set.setVisibility(mBinding.comicNumberSpinner.getId(), ConstraintSet.VISIBLE);
            set.setVisibility(mBinding.randomFloatingBtn.getId(), ConstraintSet.VISIBLE);
        }

        set.setVisibility(mBinding.appBarLayout.getId(), ConstraintSet.VISIBLE);
        set.setVisibility(mBinding.moreBtn.getId(), ConstraintSet.VISIBLE);

        set.connect(mBinding.imageView.getId(), ConstraintSet.BOTTOM, mBinding.moreBtn.getId(), ConstraintSet.TOP);
        set.connect(mBinding.imageView.getId(), ConstraintSet.TOP, mBinding.appBarLayout.getId(), ConstraintSet.BOTTOM);

        set.applyTo((ConstraintLayout) mBinding.getRoot());
    }

    private void observeDataState(){
       mActivityViewModel.getNetwork().observe(this, network -> {
            if (network.isConnected() && mViewModel.getState().getValue().isErrorOccurred()){
                onReload();
            }

            if (!network.isConnected()){
                mSwipeNextAvailable = false;
                mSwipePrevAvailable = false;
            }else{
                mSwipePrevAvailable = true;
                mSwipeNextAvailable = true;
            }
        });

       mViewModel.getRequestComicNumber().observe(this, comicNumber -> setSpinnerSelectionWithoutCallback(comicNumber));
    }


    private void setupToolbar(){
        mBinding.toolbar.setTitle(R.string.nav_item_explore);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }

    private void setupSpinner() {
        mArrayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_comic_number_item);
        mArrayAdapter.addAll(mViewModel.getComicRange());
        mBinding.comicNumberSpinner.setAdapter(mArrayAdapter);
        mBinding.comicNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mExecuteSpinnerSelected){
                    onGoTo(mArrayAdapter.getItem(position));
                }

                mExecuteSpinnerSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupArchiveBtn() {
        mBinding.archiveBtn.setOnClickListener(view -> {
            mActivityViewModel.navigateTo(NavigationItem.NAVIGATION_FAVORITES);
        });
    }

    private void setupImageListener() {
        mBinding.imageView.setOnPhotoTapListener((view, x, y) -> {
            onFullscreen();
        });

        mBinding.imageView.setOnSingleFlingListener((e1, e2, velocityX, velocityY) -> {
            final float dy = Math.abs(e1.getY() - e2.getY());

            if (dy > SwipeContract.MAX_DY){
                return true;
            }

            if (e1.getX() - e2.getX() > 0 && velocityX < -SwipeContract.MIN_VELOCITY_X && mSwipePrevAvailable){
                onPrev();
            }else if(e1.getX() - e2.getX() < 0 && velocityX > SwipeContract.MIN_VELOCITY_X && mSwipeNextAvailable){
                onNext();
            }

            return true;
        });
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
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if(item.getTitle() == getString(R.string.menu_item_favorite)){
                    mViewModel.setComicFavorite(false);
                }else{
                    mViewModel.setComicFavorite(true);
                }

                return true;

            case R.id.action_share :
                if (mBitmapToShare != null){
                    startActivity(Intent.createChooser(Utils.getComicShareIntent(getContext(), mBitmapToShare), getString(R.string.share_comic_title)));
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNext() {
        mViewModel.loadNext();
    }

    @Override
    public void onPrev() {
        mViewModel.loadPrev();
    }

    @Override
    public void onGoTo(int comicNumber) {
        mViewModel.goToComic(comicNumber);
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
    public void onRandom() {
        mViewModel.loadRandom();
    }

    @Override
    public void onReload() {
        mViewModel.reload();
    }

    @Override
    public void onFullscreen() {
        mViewModel.fullscreen();
    }
}
