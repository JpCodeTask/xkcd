package pl.jpcodetask.xkcdcomics.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicListBinding;
import pl.jpcodetask.xkcdcomics.viewmodel.XkcdViewModelFactory;

public class ComicListFragment extends Fragment {

    private ComicsAdapter mComicsAdapter;

    @Inject
    XkcdViewModelFactory mXkcdViewModelFactory;

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
        mComicsAdapter = new ComicsAdapter(null);
        ComicListViewModel viewModel = ViewModelProviders.of(this, mXkcdViewModelFactory).get(ComicListViewModel.class);
        viewModel.getTitleList().observe(this, strings -> {
            mComicsAdapter.setData(strings);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentComicListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comic_list, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(mComicsAdapter);

        return binding.getRoot();
    }

    private static class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>{

        private List<String> mData;

        ComicsAdapter(List<String> data){
            mData = data;
        }

        @NonNull
        @Override
        public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_list_item, parent, false);
            return new ComicViewHolder(root);
        }

        @Override
        public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
            holder.bind(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }

        public void setData(List<String> data){
            mData = data;
            notifyDataSetChanged();
        }

        private static class ComicViewHolder extends RecyclerView.ViewHolder{

            private ImageView mImageView;
            private TextView mTextView;

            public ComicViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.media_image);
                mTextView = itemView.findViewById(R.id.supporting_text);
            }

            public void bind(String text){
                mImageView.setImageResource(R.drawable.ic_launcher_background);
                mTextView.setText(text);
            }
        }
    }


}
