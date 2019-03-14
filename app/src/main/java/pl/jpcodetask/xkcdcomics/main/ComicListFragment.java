package pl.jpcodetask.xkcdcomics.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.databinding.FragmentComicListBinding;

public class ComicListFragment extends Fragment {

    public ComicListFragment(){
        //empty
    }

    static ComicListFragment newInstance(){
        return new ComicListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentComicListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comic_list, container, false);
        return binding.getRoot();
    }

    private static class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>{


        ComicsAdapter(){

        }

        @NonNull
        @Override
        public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        private static class ComicViewHolder extends RecyclerView.ViewHolder{

            public ComicViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(){

            }
        }
    }


}
