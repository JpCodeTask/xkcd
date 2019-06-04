package pl.jpcodetask.xkcdcomics.ui.favorites.list;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.usecase.FavoritesUseCase;

public class FavoritesViewModel extends ViewModel {

    static final int SORT_BY_NUM = R.string.sort_by_number;
    static final int SORT_BY_TITLE = R.string.sort_by_title;

    private final FavoritesUseCase mFavoritesUseCase;

    private final MutableLiveData<List<Comic>> mComicList = new MutableLiveData<>();
    private final MutableLiveData<String> mSearchQuery = new MutableLiveData<>();
    private final MutableLiveData<Integer> mSortField = new MutableLiveData<>();

    private List<Comic> mOriginalComicList = new ArrayList<>();

    public FavoritesViewModel(FavoritesUseCase favoritesUseCase){
        mFavoritesUseCase = favoritesUseCase;
        loadList();
    }

    void loadList(){
        mFavoritesUseCase.loadList()
                .doOnSuccess(comics -> {
                    mOriginalComicList = comics;
                    mComicList.setValue(comics);
                    sort(mFavoritesUseCase.getSortField(SORT_BY_TITLE));
                })
                .subscribe();
    }


    void search(String query){
        mSearchQuery.setValue(query);

        if (TextUtils.isEmpty(query)){
            mComicList.setValue(mOriginalComicList);
            sort(mSortField.getValue());
        }else{
            List<Comic> searchedComicList = new ArrayList<>();
            for (Comic comic :  mOriginalComicList){
                if (Pattern.compile(query.toLowerCase()).matcher(comic.getTitle().toLowerCase()).find() || Pattern.compile(query.toLowerCase()).matcher(String.valueOf(comic.getNum())).find() ){
                    searchedComicList.add(comic);
                }
            }

            mComicList.setValue(searchedComicList);
            sort(mSortField.getValue());
        }

    }

    void sort(Integer sortfield){

        mSortField.setValue(sortfield);
        mFavoritesUseCase.setFavoriteField(sortfield);

        if (mComicList.getValue() == null){
            return;
        }


        List<Comic> sortedList = new ArrayList<>(mComicList.getValue());
        Comparator<Comic> mComicComparator = (o1, o2) -> {
            switch (sortfield){
                case SORT_BY_TITLE:
                    return o1.getTitle().compareTo(o2.getTitle());
                case SORT_BY_NUM:
                    return o2.getNum() - o1.getNum();

                default:
                    return 0;
            }
        };

        Collections.sort(sortedList, mComicComparator);
        mComicList.setValue(sortedList);
    }

    LiveData<List<Comic>> getComicList() {
        return mComicList;
    }

    LiveData<Integer> getSortField() {
        return mSortField;
    }

    LiveData<String> getSearchQuery() {
        return mSearchQuery;
    }
}
