package pl.jpcodetask.xkcdcomics.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;
import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;
import pl.jpcodetask.xkcdcomics.data.source.RepositoryImpl;

public class UpdateJobService extends JobService {

    private static final String TAG = UpdateJobService.class.getSimpleName();

    private static final int NEW_COMIC_JOB_ID = 111;
    private static final String NEW_COMIC_NOTIFICATION_CHANNEL_ID = "channel1";
    private static final int NEW_COMIC_NOTIFICATION_ID = 11;
    private static final int INVALID_LATEST_COMIC_NUMBER = 0;

    @Inject
    SharedPreferenceProvider mSharedPreferenceProvider;
    @Inject
    RepositoryImpl mRepository;
    private Disposable mDisposable;

    public static void schedule(Context context){
        if (isJobServiceOn(context)){
            Log.i(TAG, "Service is running.");
            return;
        }

        ComponentName serviceComponent = new ComponentName(context, UpdateJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(NEW_COMIC_JOB_ID, serviceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(DateUtils.DAY_IN_MILLIS);
        builder.setPersisted(true);
        builder.setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_EXPONENTIAL);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);


        jobScheduler.schedule(builder.build());
    }

    private static boolean isJobServiceOn( Context context ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == NEW_COMIC_JOB_ID ) {
                hasBeenScheduled = true ;
                break ;
            }
        }

        return hasBeenScheduled ;
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        mDisposable = mRepository.getLatestComic()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .doOnSuccess(comicWrapper -> {
                    if (comicWrapper.isSuccess()){
                        int sharedPrefLatestComicNumber = mSharedPreferenceProvider.getLatestComicNumber(INVALID_LATEST_COMIC_NUMBER);
                        int latestComicNumber = comicWrapper.getComic().getNum();

                        if (latestComicNumber > sharedPrefLatestComicNumber ){
                            mSharedPreferenceProvider.setLatestComicNumber(latestComicNumber);

                            if (mSharedPreferenceProvider.isNotifyOn() && !mSharedPreferenceProvider.isFirstLaunch()){
                                notifyAboutNewComic(comicWrapper.getComic());
                            }

                        }


                    }

                    jobFinished(params, false);
                })
                .subscribe();
        return true;
    }

    private void notifyAboutNewComic(Comic comic) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NEW_COMIC_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NEW_COMIC_NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.baseline_share_black_24)
                .setContentTitle(getString(R.string.new_comic_notification_title))
                .setContentText(getString(R.string.new_comic_notification_text));

        notificationManager.notify(NEW_COMIC_NOTIFICATION_ID, builder.build());
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mDisposable.dispose();
        return false;
    }
}
