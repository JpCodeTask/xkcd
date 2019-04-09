package pl.jpcodetask.xkcdcomics.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.jpcodetask.xkcdcomics.XkcdApplication;
import pl.jpcodetask.xkcdcomics.data.source.DataSource;
import pl.jpcodetask.xkcdcomics.data.source.remote.XkcdApi;

@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class,
                ServiceBuilderModule.class
        }
)
public interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(XkcdApplication application);

    @Named("repository")
    DataSource getRepository();

    @Named("application_context")
    Context getAppContext();

    XkcdApi getApi();
}
