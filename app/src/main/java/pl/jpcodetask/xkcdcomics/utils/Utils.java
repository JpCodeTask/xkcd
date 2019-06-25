package pl.jpcodetask.xkcdcomics.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static Intent getComicShareIntent(Context context, Bitmap image){
        saveImage(context, image);
        return buildIntent(context);
    }

    private static void saveImage(Context context, Bitmap image){
        File cachePath = new File(context.getCacheDir(), "images");
        cachePath.mkdirs();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(cachePath + "/share_image.png");
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Intent buildIntent(Context context) {
        File imagePath = new File(context.getCacheDir(), "images/");
        File newFile = new File(imagePath, "share_image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "pl.jpcodetask.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            return shareIntent;
        }
        return null;
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
