package pl.jpcodetask.xkcdcomics.data.source;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class Repository implements DataSource {

    private final DataSource mLocalDataSource;

    @Inject
    public Repository(@Named("local_data_source") DataSource localDataSource){
        mLocalDataSource = localDataSource;
    }

    @Override
    public List<String> getData() {
        return mLocalDataSource.getData();
    }
}
