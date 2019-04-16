package pl.jpcodetask.xkcdcomics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comic {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @Expose
    @SerializedName("num")
    @ColumnInfo(name = "num")
    private int mNum;

    @Expose
    @SerializedName("day")
    @ColumnInfo(name = "day")
    private int mDay;

    @Expose
    @SerializedName("month")
    @ColumnInfo(name = "month")
    private int mMonth;

    @Expose
    @SerializedName("year")
    @ColumnInfo(name = "year")
    private int mYear;

    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String mTitle;

    @Expose
    @SerializedName("img")
    @ColumnInfo(name = "img")
    private String mImgUrl;

    @Expose
    @SerializedName("alt")
    @ColumnInfo(name = "alt")
    private String mAlt;

    @Expose
    @SerializedName("transcript")
    @ColumnInfo(name = "transcript")
    private String mTranscript;

    @ColumnInfo(name = "favorite")
    private boolean mFavorite;

    @ColumnInfo(name = "read")
    private boolean mRead;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getAlt() {
        return mAlt;
    }

    public void setAlt(String alt) {
        mAlt = alt;
    }

    public String getTranscript() {
        return mTranscript;
    }

    public void setTranscript(String transcript) {
        mTranscript = transcript;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public boolean isRead() {
        return mRead;
    }

    public void setRead(boolean read) {
        mRead = read;
    }

}
