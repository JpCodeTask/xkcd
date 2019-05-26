package pl.jpcodetask.xkcdcomics.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.databinding.FavoritesListItemBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentFavoritesBindingImpl;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding mBinding;
    private FavoritesAdapter mAdapter;

    public FavoritesFragment(){
        //empty
    }

    public static FavoritesFragment newInstance(){
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoritesBindingImpl.inflate(inflater, container, false);

        setupToolbar();
        setHasOptionsMenu(true);

        mAdapter = new FavoritesAdapter();
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(mAdapter);


        List<Comic> testList = new ArrayList<>();
        for (int i = 20; i >= 0; i--){
            Comic comic = new Comic();
            comic.setTitle("Lorem impsum dorem");
            comic.setNum(2100 + i);
            testList.add(comic);
        }

        mAdapter.setFavoritesList(testList);


        return mBinding.getRoot();
    }

    private void setupToolbar() {
        mBinding.toolbar.setTitle(R.string.nav_item_favorites);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.favorites_menu, menu);
    }

    private static class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>{

        private List<Comic> mFavoritesList = new ArrayList<>();

        void setFavoritesList(@NonNull List<Comic> favoritesList){
            mFavoritesList = favoritesList;
            notifyDataSetChanged();
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
            holder.bind(comic);
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

            void bind(@NonNull Comic comic){
                mBinding.setItem(comic);
                mBinding.executePendingBindings();
            }
        }
    }
}
