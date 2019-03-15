package pl.jpcodetask.xkcdcomics.data.source;

import java.util.List;

import pl.jpcodetask.xkcdcomics.data.model.Comic;

public interface DataSource {
    List<Comic> getData();
}
