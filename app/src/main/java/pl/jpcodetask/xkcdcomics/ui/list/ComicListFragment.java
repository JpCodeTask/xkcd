package pl.jpcodetask.xkcdcomics.ui.list;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.databinding.ComicListItemBinding;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicListBinding;
import pl.jpcodetask.xkcdcomics.ui.details.ComicDetailsFragment;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class ComicListFragment extends Fragment implements ComicListNavigator{

    private ComicsAdapter mComicsAdapter;

    @Inject
    XkcdViewModelFactory mXkcdViewModelFactory;
    private ComicListViewModel mViewModel;

    public ComicListFragment(){
        //empty
    }

    public static ComicListFragment newInstance(){
        return new ComicListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBindedViewModel();
        setupAdapter();

        mViewModel.start();
    }

    private void setupBindedViewModel(){
        mViewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(ComicListViewModel.class);

        mViewModel.getComicList().observe(this, comics -> {
            mComicsAdapter.setData(comics);
        });

        mViewModel.getEventComicDetails().observe(this, comicDetailsEvent ->{
            Integer comicNumber = comicDetailsEvent.getEventContentIfNotHandled();

            if (comicNumber != null){
               openComicItem(comicNumber);
            }

        });

    }

    private void setupAdapter(){
        mComicsAdapter = new ComicsAdapter(null, new ComicItemListener() {
            @Override
            public void onItemClicked(View view, Comic item) {
                //Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                mViewModel.openComicDetails(item.getNum());
            }

            @Override
            public void onFavoriteClicked(View view, Comic item) {
                Toast.makeText(getContext(), "Item favorite " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void openComicItem(Integer comicNumber) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ComicDetailsFragment.newInstance(comicNumber))
                .addToBackStack(null)
                .commit();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentComicListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comic_list, container, false);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        } else{
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        binding.recyclerView.setAdapter(mComicsAdapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.clean();
    }

    private static class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>{

        private ComicItemListener mComicItemListener;
        private List<Comic> mData;

        ComicsAdapter(List<Comic> data, @NonNull ComicItemListener listener){
            mData = data;
            mComicItemListener = listener;
        }

        @NonNull
        @Override
        public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ComicListItemBinding binding = ComicListItemBinding.inflate(inflater, parent, false);
            return new ComicViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
            holder.bind(mData.get(position), mComicItemListener);
        }

        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }

        public void setData(List<Comic> data){
            mData = data;
            notifyDataSetChanged();
        }

        private static class ComicViewHolder extends RecyclerView.ViewHolder{

            private final ComicListItemBinding mItemBinding;

            ComicViewHolder(@NonNull ComicListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                mItemBinding = itemBinding;
            }

            void bind(Comic comicItem, @NonNull ComicItemListener comicItemListener){
                mItemBinding.mediaImage.setImageResource(R.drawable.ic_launcher_background);
                mItemBinding.setItem(comicItem);
                mItemBinding.setListener(comicItemListener);
                mItemBinding.executePendingBindings();
            }
        }
    }


}
