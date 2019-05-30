package pl.jpcodetask.xkcdcomics.ui.favorites.item;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesItemBinding;
import pl.jpcodetask.xkcdcomics.ui.common.ComicViewer;
import pl.jpcodetask.xkcdcomics.utils.ComicUtils;
import pl.jpcodetask.xkcdcomics.utils.GlideApp;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class FavoritesItemFragment extends Fragment implements ComicViewer {

    @Inject
    XkcdViewModelFactory mViewModelFactory;


    private static final String ARG_COMIC_NUMBER = "ARG_COMIC_NUMBER";

    private FragmentFavoritesItemBinding mBinding;
    private boolean mComicDetailsVisible = false;
    private boolean mIsFavorite = false;
    private FavoritesItemViewModel mViewmodel;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoritesItemBinding.inflate(inflater, container, false);

        setupToolbar();
        setupViewmodel();
        setHasOptionsMenu(true);

        mBinding.setViewer(this);
        mBinding.setViewmodel(mViewmodel);
        mBinding.setLifecycleOwner(this);

        return mBinding.getRoot();
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }

    private void setupViewmodel() {
        mViewmodel = ViewModelProviders.of(this, mViewModelFactory).get(FavoritesItemViewModel.class);
        mViewmodel.getComic().observe(this, comic -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(comic.getTitle());
            GlideApp.with(this).load(comic.getImgUrl()).into(mBinding.imageView);
            setupShareIntent(comic);
        });
    }


    private void setupShareIntent(Comic comic) {
        mShareIntent = ComicUtils.getComicShareIntent(comic);
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewmodel.loadComic(getArguments().getInt(ARG_COMIC_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.item_menu, menu);
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
                Toast.makeText(getContext(), "Favorite", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return true;
        }
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
