package pl.jpcodetask.xkcdcomics.di;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.jpcodetask.xkcdcomics.XkcdApplication;

@Singleton
@Component(
        modules = {
                AppModule.class,
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class,
                LocalDataModule.class
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
}
