package pl.jpcodetask.xkcdcomics.utils;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

public final class Schedulers {

    private Schedulers(){

    }

    public static Scheduler io(){
        return io.reactivex.schedulers.Schedulers.io();
    }

    public static Scheduler computation(){
        return io.reactivex.schedulers.Schedulers.computation();
    }

    public static Scheduler mainThread(){
        return AndroidSchedulers.mainThread();
    }
}
