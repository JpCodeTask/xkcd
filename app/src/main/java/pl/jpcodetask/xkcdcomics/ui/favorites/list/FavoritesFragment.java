package pl.jpcodetask.xkcdcomics.ui.favorites.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.databinding.FavoritesListItemBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBindingImpl;
import pl.jpcodetask.xkcdcomics.ui.MainViewModel;
import pl.jpcodetask.xkcdcomics.ui.favorites.DetailsTransition;
import pl.jpcodetask.xkcdcomics.ui.favorites.item.FavoritesItemFragment;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class FavoritesFragment extends Fragment {

    @Inject
    XkcdViewModelFactory mXkcdViewModelFactory;

    private static final String DIALOG_SORT = "DialogSort";
    private static final int REQUEST_SORT_BY_OPTION = 1001;
    private static final int REQUEST_FAVORITES_ITEM = 1002;

    private FragmentFavoritesBinding mBinding;
    private FavoritesAdapter mAdapter;
    private FavoritesViewModel mViewModel;
    private int mLastViewedItemPosition = 0;
    private SearchView mSearchView;
    private MainViewModel mMainViewModel;

    public FavoritesFragment(){
        //empty
    }

    public static FavoritesFragment newInstance(){
        return new FavoritesFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAdapter();
    }

    private void setupAdapter() {
        mAdapter = new FavoritesAdapter((view, comic, position) -> {
            mLastViewedItemPosition = position;

            FavoritesItemFragment favoritesItemFragment = FavoritesItemFragment.newInstance(comic.getNum());
            favoritesItemFragment.setTargetFragment(this, REQUEST_FAVORITES_ITEM);

            favoritesItemFragment.setSharedElementEnterTransition(new DetailsTransition());
            favoritesItemFragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            favoritesItemFragment.setSharedElementReturnTransition(new DetailsTransition());


            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(view, "comic")
                    .replace(R.id.fragment_container_one, favoritesItemFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoritesBindingImpl.inflate(inflater, container, false);

        setupViewModel();
        setupRecyclerView();
        setupToolbar();
        setHasOptionsMenu(true);

        mBinding.setLifecycleOwner(this);

        return mBinding.getRoot();
    }

    private void setupRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(FavoritesViewModel.class);
        mViewModel.getComicList().observe(this, comics -> {
            mAdapter.setFavoritesList(comics);
            if (!comics.isEmpty()){
                mBinding.emptyListView.setVisibility(View.GONE);
            }else{
                mBinding.emptyListView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupToolbar() {
        mBinding.toolbar.setTitle(R.string.nav_item_favorites);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorites_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mViewModel.search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()){
                    mViewModel.search(null);
                }
                return false;
            }
        });
        mSearchView.setOnCloseListener(() -> {
            mViewModel.search(null);
            return false;
        });

        if(!TextUtils.isEmpty(mViewModel.getSearchQuery().getValue())){
            mSearchView.setQuery(mViewModel.getSearchQuery().getValue(), true);
            mSearchView.clearFocus();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sort:
                openSortDialogFragment();
                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void openSortDialogFragment(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        DialogFragment dialogFragment = SortDialogFragment.newInstance(mViewModel.getSortField().getValue());
        dialogFragment.setTargetFragment(this, REQUEST_SORT_BY_OPTION);
        dialogFragment.show(manager, DIALOG_SORT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_SORT_BY_OPTION && data != null){
            int optionId = data.getIntExtra(SortDialogFragment.EXTRA_SELECTED_OPTION, FavoritesViewModel.SORT_BY_TITLE);
            switch (optionId){
                case R.string.sort_by_title:
                    mViewModel.sort(FavoritesViewModel.SORT_BY_TITLE);
                    break;

                case R.string.sort_by_number:
                    mViewModel.sort(FavoritesViewModel.SORT_BY_NUM);
                    break;
            }
        }

        if (requestCode == REQUEST_FAVORITES_ITEM && data != null){
            boolean isUnfavorite = data.getBooleanExtra(FavoritesItemFragment.EXTRA_UNFAVORITE_COMIC, false);
            if (isUnfavorite){
              mAdapter.removeAtPosition(mLastViewedItemPosition);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mSearchView.clearFocus();
    }

    private interface FavoriteItemClickListener {
        void OnFavoriteItemClick(View view, Comic comic, int position);
    }

    private static class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>{

        private List<Comic> mFavoritesList = new ArrayList<>();
        private FavoriteItemClickListener mFavoriteItemClickListener;

        FavoritesAdapter(@NonNull FavoriteItemClickListener favoriteItemClickListener){
            mFavoriteItemClickListener = favoriteItemClickListener;
        }

        void setFavoritesList(@NonNull List<Comic> favoritesList){
            mFavoritesList = favoritesList;
            notifyDataSetChanged();
        }

        void removeAtPosition(int position){
            mFavoritesList.remove(position);
            notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            FavoritesListItemBinding binding = FavoritesListItemBinding.inflate(inflater, parent, false);
            return new FavoriteViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
            Comic comic = mFavoritesList.get(position);
            holder.bind(comic, mFavoriteItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mFavoritesList != null ? mFavoritesList.size() : 0;
        }


        private static class FavoriteViewHolder extends RecyclerView.ViewHolder{

            private final FavoritesListItemBinding mBinding;

            FavoriteViewHolder(FavoritesListItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            void bind(final @NonNull Comic comic, final FavoriteItemClickListener favoriteItemClickListener){
                mBinding.setItem(comic);
                mBinding.getRoot().setTransitionName(comic.getTitle() + "_" + comic.getNum());
                mBinding.getRoot().setOnClickListener(v -> {
                    favoriteItemClickListener.OnFavoriteItemClick(v, comic, getAdapterPosition());
                });
                mBinding.executePendingBindings();
            }
        }
    }
}
