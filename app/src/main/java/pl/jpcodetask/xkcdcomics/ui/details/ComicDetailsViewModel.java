package pl.jpcodetask.xkcdcomics.ui.details;

import androidx.lifecycle.ViewModel;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

public class ComicDetailsViewModel extends ViewModel {

    private final DataSource mDataSource;

    public ComicDetailsViewModel(DataSource dataSource){
        mDataSource = dataSource;
    }
}
