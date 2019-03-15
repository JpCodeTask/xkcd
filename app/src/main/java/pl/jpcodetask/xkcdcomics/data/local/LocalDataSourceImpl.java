package pl.jpcodetask.xkcdcomics.data.local;

import java.util.ArrayList;
import java.util.List;

import pl.jpcodetask.xkcdcomics.data.DataSource;

public class LocalDataSourceImpl implements DataSource {
    @Override
    public List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("Lorem ipsum 1");
        data.add("Lorem ipsum 2");
        data.add("Lorem ipsum 3");
        data.add("Lorem ipsum 4");
        data.add("Lorem ipsum 5");
        data.add("Lorem ipsum 6");
        data.add("Lorem ipsum 7");
        data.add("Lorem ipsum 8");

        return data;
    }
}
