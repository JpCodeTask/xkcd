package pl.jpcodetask.xkcdcomics;

public final class Event<T> {

    private final T mEventContent;
    private boolean mIsEventHandled = false;

    public Event(T eventContent){
        if (eventContent == null){
            throw new IllegalArgumentException("null values in event are not allowed");
        }
        mEventContent = eventContent;
    }

    public T getEventContentIfNotHandled(){
        if (isEventHandled()){
            return null;
        }else{
            mIsEventHandled = true;
            return mEventContent;
        }
    }

    public boolean isEventHandled(){
        return mIsEventHandled;
    }

}
