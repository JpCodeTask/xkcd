package pl.jpcodetask.xkcdcomics.ui.item;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicBinding;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.utils.GlideApp;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class ComicFragment extends Fragment implements ComicNavigator{

    @Inject
    XkcdViewModelFactory mViewModelFactory;

    private ComicViewModel mViewModel;
    private MainViewModel mActivityViewModel;
    private FragmentComicBinding mBinding;

    private boolean mExecuteOnItemSelected = false;
    private boolean mComicDetailsVisible = false;
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
        mBinding = FragmentComicBinding.inflate(inflater, container, false);

        setupViewModel();
        setupToolbar();
        setHasOptionsMenu(true);
        setupSpinner();

        mBinding.setLifecycleOwner(getActivity());
        mBinding.setViewmodel(mViewModel);
        mBinding.setActivityviewmodel(mActivityViewModel);
        mBinding.setNavigator(this);

        return mBinding.getRoot();
    }

    private void setupViewModel(){
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ComicViewModel.class);
        mActivityViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(MainViewModel.class);
        //TODO separate layout from network state and navigationitem
        observeData();
        observeViewState();
        observeDataState();
    }

    private void observeData(){
        mViewModel.getComic().observe(this, comic -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(comic.getTitle());
            setSpinnerSelectionWithoutCallback(comic.getNum());
            GlideApp.with(this).load(comic.getImgUrl()).into(mBinding.imageView);
        });

        mViewModel.getSnackBarMessage().observe(this, eventString -> {
            String message = eventString.getEventContentIfNotHandled();
            if (message != null){
                Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }

        });
    }

    private void setSpinnerSelectionWithoutCallback(int comicNumber){
        mExecuteOnItemSelected = false;

        if (comicNumber >= mArrayAdapter.getCount()){
            mArrayAdapter.clear();
            mArrayAdapter.addAll(mViewModel.getComicRange());
            mArrayAdapter.notifyDataSetChanged();
        }

        mBinding.comicNumberSpinner.setSelection(comicNumber - 1);

        mExecuteOnItemSelected = true;
    }

    private void observeViewState(){
        mViewModel.getState().observe(this, comicState -> {

            if (comicState.isNextAvailable()){
                mBinding.nextBtn.setEnabled(true);
            }else{
                mBinding.nextBtn.setEnabled(false);
            }

            if (comicState.isPrevAvailable()){
                mBinding.prevBtn.setEnabled(true);
            }else{
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

        });
    }

    private void observeDataState(){
       mActivityViewModel.getNetwork().observe(this, network -> {
            if (network.isConnected() && mViewModel.getState().getValue().isErrorOccurred()){
                onReload();
            }
        });

       mViewModel.getRequestComicNumber().observe(this, comicNumber -> setSpinnerSelectionWithoutCallback(comicNumber));
    }


    private void setupToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }

    private void setupSpinner() {
        mArrayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_comic_number_item);
        mArrayAdapter.addAll(mViewModel.getComicRange());
        mBinding.comicNumberSpinner.setAdapter(mArrayAdapter);
        mBinding.comicNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mExecuteOnItemSelected){
                    onGoTo(mArrayAdapter.getItem(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadComic();
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
}
