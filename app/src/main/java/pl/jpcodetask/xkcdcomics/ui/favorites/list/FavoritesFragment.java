package pl.jpcodetask.xkcdcomics.ui.favorites.list;

import android.content.Context;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.databinding.FavoritesListItemBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBindingImpl;
import pl.jpcodetask.xkcdcomics.ui.favorites.DetailsTransition;
import pl.jpcodetask.xkcdcomics.ui.favorites.item.FavoritesItemFragment;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class FavoritesFragment extends Fragment {

    @Inject
    XkcdViewModelFactory mXkcdViewModelFactory;

    private FragmentFavoritesBinding mBinding;
    private FavoritesAdapter mAdapter;
    private FavoritesViewModel mViewModel;

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

        mAdapter = new FavoritesAdapter((view, comic) -> {

            FavoritesItemFragment favoritesItemFragment = FavoritesItemFragment.newInstance(comic.getNum());
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

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(FavoritesViewModel.class);
        mViewModel.getComicList().observe(this, comics -> {
            if (comics != null && !comics.isEmpty()){
                mAdapter.setFavoritesList(comics);
            }
        });
    }

    private void setupToolbar() {
        mBinding.toolbar.setTitle(R.string.nav_item_favorites);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.favorites_menu, menu);
    }

    private interface FavoriteItemClickListener {
        void OnFavoriteItemClick(View view, Comic comic);
    }

    private static class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>{

        private static final String SORT_BY_NUM = "SORT_BY_NUM";
        private static final String SORT_BY_TITLE = "SORT_BY_TITLE";

        private List<Comic> mFavoritesList = new ArrayList<>();
        private final Comparator<Comic> mComicComparator;
        private FavoriteItemClickListener mFavoriteItemClickListener;
        private String mCurrentSortField = SORT_BY_NUM;

        public FavoritesAdapter(@NonNull FavoriteItemClickListener favoriteItemClickListener){
            mFavoriteItemClickListener = favoriteItemClickListener;
            mComicComparator = (o1, o2) -> {
               switch (mCurrentSortField){
                   case SORT_BY_TITLE:
                       return o1.getTitle().compareTo(o2.getTitle());
                   case SORT_BY_NUM:
                       return o2.getNum() - o1.getNum();

                   default:
                       return 0;
               }
            };
        }

        void setFavoritesList(@NonNull List<Comic> favoritesList){
            mFavoritesList = favoritesList;
            Collections.sort(mFavoritesList, mComicComparator);
            notifyDataSetChanged();
        }

        void sortBy(String sortField){

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
                    favoriteItemClickListener.OnFavoriteItemClick(v, comic);
                });
                mBinding.executePendingBindings();
            }
        }
    }
}