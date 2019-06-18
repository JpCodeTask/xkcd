package pl.jpcodetask.xkcdcomics.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import pl.jpcodetask.xkcdcomics.R;
import pl.jpcodetask.xkcdcomics.data.model.Comic;

public class Utils {

    private Utils(){
        //empty
    }

    public static Intent getComicShareIntent(Comic comic){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, comic.getImgUrl());
        shareIntent.setType("text/plain");

        return shareIntent;
    }

    public static Intent getFeedbackIntent(Context context){
        Uri uri = getFeedbackUri(context);

        Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO);
        feedbackIntent.setData(uri);

        return feedbackIntent;
    }

    private static Uri getFeedbackUri(Context context) {
        String stringBuilder = "mailto:" + context.getString(R.string.feedback_email) +
                "?subject=" + context.getString(R.string.feedback_subject);

        return Uri.parse(stringBuilder);
    }
}
