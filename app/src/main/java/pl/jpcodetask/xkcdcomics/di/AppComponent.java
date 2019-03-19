package pl.jpcodetask.xkcdcomics.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.jpcodetask.xkcdcomics.XkcdApplication;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;

@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class,
        }
)
public interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(XkcdApplication application);

        AppComponent build();
    }

    void inject(XkcdApplication application);

    @Named("repository")
    DataSource getRepository();
}
