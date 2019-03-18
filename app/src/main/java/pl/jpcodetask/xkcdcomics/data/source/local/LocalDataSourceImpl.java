package pl.jpcodetask.xkcdcomics.data.source.local;

import java.util.ArrayList;
import java.util.List;

import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class LocalDataSourceImpl implements DataSource {


    private final List<Comic> mData;

    public LocalDataSourceImpl(){
        mData = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Comic comic = new Comic();
            comic.setNum(i);
            comic.setTitle("Lorem ipsum " + i);
            comic.setDay(i);
            comic.setMonth(i + 2);
            comic.setYear(10 * i + 1000);

            mData.add(comic);
        }
    }

    @Override
    public List<Comic> getData() {
        return mData;
    }

    @Override
    public Comic getComic(int comicNumber) {
        return mData.get(comicNumber);
    }
}
