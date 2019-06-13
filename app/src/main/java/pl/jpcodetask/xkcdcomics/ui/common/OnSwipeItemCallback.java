package pl.jpcodetask.xkcdcomics.ui.common;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class OnSwipeItemCallback extends ItemTouchHelper.SimpleCallback {

    private final OnSwipeListener mSwipeListener;
    private ColorDrawable mLeftBackground;
    private ColorDrawable mRightBackground;
    private Drawable mLeftIcon;
    private Drawable mRightIcon;

    public OnSwipeItemCallback(OnSwipeListener swipeListener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mSwipeListener = swipeListener;
    }

    public void setLeftBackground(ColorDrawable leftBackground) {
        mLeftBackground = leftBackground;
    }

    public void setRightBackground(ColorDrawable rightBackground) {
        mRightBackground = rightBackground;
    }

    public void setLeftIcon(Drawable leftIcon) {
        mLeftIcon = leftIcon;
    }

    public void setRightIcon(Drawable rightIcon) {
        mRightIcon = rightIcon;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        switch (direction) {
            case ItemTouchHelper.LEFT:
                mSwipeListener.onSwipeLeft(position);
                break;

            case ItemTouchHelper.RIGHT:
                mSwipeListener.onSwipeRight(position);
                break;
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) { // Swiping to the right

            int iconMargin = (itemView.getHeight() - mLeftIcon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - mLeftIcon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + mLeftIcon.getIntrinsicHeight();

            int iconLeft = itemView.getLeft() + iconMargin + mLeftIcon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            mLeftIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            mLeftBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconMargin = (itemView.getHeight() - mRightIcon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - mRightIcon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + mRightIcon.getIntrinsicHeight();

            int iconLeft = itemView.getRight() - iconMargin - mRightIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            mRightIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            mRightBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            mLeftBackground.setBounds(0, 0, 0, 0);
            mRightBackground.setBounds(0, 0, 0, 0);
        }

        mRightBackground.draw(c);
        mLeftBackground.draw(c);
        mLeftIcon.draw(c);
        mRightIcon.draw(c);
    }

    public interface OnSwipeListener {
        void onSwipeLeft(int position);

        void onSwipeRight(int position);
    }
}