package pl.jpcodetask.xkcdcomics.ui.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class OnSwipeItemCallback extends ItemTouchHelper.SimpleCallback{
        
        private final OnSwipeListener mSwipeListener;

        public OnSwipeItemCallback(OnSwipeListener swipeListener) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            mSwipeListener = swipeListener;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            
            switch (direction){
                case ItemTouchHelper.LEFT:
                    mSwipeListener.onSwipeLeft(position);
                    break;
                    
                case ItemTouchHelper.RIGHT:
                    mSwipeListener.onSwipeRight(position);
                    break;
            }
        }
        
        public interface OnSwipeListener{
            void onSwipeLeft(int position);
            void onSwipeRight(int position);
        }
    }