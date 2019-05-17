package pl.jpcodetask.xkcdcomics.ui.item;

import pl.jpcodetask.xkcdcomics.utils.AbstractBuilder;

public class ComicViewState {

    private boolean mIsDataLoading;
    private boolean mIsNextAvailable;
    private boolean mIsPrevAvailable;
    private boolean mIsErrorOccurred;


    private ComicViewState(Builder builder){
        this.mIsDataLoading = builder.mIsDataLoading;
        this.mIsNextAvailable = builder.mIsNextAvailable;
        this.mIsPrevAvailable = builder.mIsPrevAvailable;
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

    public boolean isErrorOccurred() {
        return mIsErrorOccurred;
    }


    public static class Builder implements AbstractBuilder<ComicViewState>{

        private boolean mIsDataLoading;
        private boolean mIsNextAvailable;
        private boolean mIsPrevAvailable;
        private boolean mIsErrorOccurred;

        public Builder(){

        }

        public Builder(ComicViewState comicViewState){
            this.mIsDataLoading = comicViewState.mIsDataLoading;
            this.mIsNextAvailable = comicViewState.mIsNextAvailable;
            this.mIsPrevAvailable = comicViewState.mIsPrevAvailable;
            this.mIsErrorOccurred = comicViewState.mIsErrorOccurred;
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

        public Builder setErrorOccurred(boolean errorOccurred) {
            mIsErrorOccurred = errorOccurred;
            return this;
        }

        @Override
        public ComicViewState build() {
            return new ComicViewState(this);
        }
    }
}
