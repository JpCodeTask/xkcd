package pl.jpcodetask.xkcdcomics.ui.item;

import pl.jpcodetask.xkcdcomics.utils.AbstractBuilder;

public class ComicState {

    private boolean mIsDataLoading;
    private boolean mIsNextAvailable;
    private boolean mIsPrevAvailable;
    private boolean mIsFavorite;
    private boolean mIsErrorOccurred;


    private ComicState(Builder builder){
        this.mIsDataLoading = builder.mIsDataLoading;
        this.mIsNextAvailable = builder.mIsNextAvailable;
        this.mIsPrevAvailable = builder.mIsPrevAvailable;
        this.mIsFavorite = builder.mIsFavorite;
        this.mIsErrorOccurred = builder.mIsErrorOccurred;
    }


    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    public boolean isNextAvailable() {
        return mIsNextAvailable;
    }

    public boolean isPrevAvailable() {
        return mIsPrevAvailable;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public boolean isErrorOccurred() {
        return mIsErrorOccurred;
    }


    public static class Builder implements AbstractBuilder<ComicState>{

        private boolean mIsDataLoading;
        private boolean mIsNextAvailable;
        private boolean mIsPrevAvailable;
        private boolean mIsFavorite;
        private boolean mIsErrorOccurred;

        public Builder(){

        }

        public Builder(ComicState comicState){
            this.mIsDataLoading = comicState.mIsDataLoading;
            this.mIsNextAvailable = comicState.mIsNextAvailable;
            this.mIsPrevAvailable = comicState.mIsPrevAvailable;
            this.mIsFavorite = comicState.mIsFavorite;
            this.mIsErrorOccurred = comicState.mIsErrorOccurred;
        }


        public Builder setDataLoading(boolean dataLoading) {
            mIsDataLoading = dataLoading;
            return this;
        }

        public Builder setNextAvailable(boolean nextAvailable) {
            mIsNextAvailable = nextAvailable;
            return this;
        }

        public Builder setPrevAvailable(boolean prevAvailable) {
            mIsPrevAvailable = prevAvailable;
            return this;
        }

        public Builder setFavorite(boolean favorite) {
            mIsFavorite = favorite;
            return this;
        }

        public Builder setErrorOccurred(boolean errorOccurred) {
            mIsErrorOccurred = errorOccurred;
            return this;
        }

        @Override
        public ComicState build() {
            return new ComicState(this);
        }
    }
}
