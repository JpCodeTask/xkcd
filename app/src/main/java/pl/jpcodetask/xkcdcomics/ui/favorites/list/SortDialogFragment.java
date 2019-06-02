package pl.jpcodetask.xkcdcomics.ui.favorites.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import pl.jpcodetask.xkcdcomics.R;

public class SortDialogFragment extends DialogFragment {

    private static final String ARG_CURRENT_SORT_FIELD_ID = "ARG_CURRENT_SORT_FIELD_ID";

    static final String EXTRA_SELECTED_OPTION = "EXTRA_SELECTED_OPTION";

    private CharSequence[] mOptions;
    private Integer[] mOptionsIds = new Integer[]{
        R.string.sort_by_title,
        R.string.sort_by_number
    };

    static SortDialogFragment newInstance(int currentSortFieldId) {
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_SORT_FIELD_ID, currentSortFieldId);

        SortDialogFragment fragment = new SortDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOptions = new CharSequence[mOptionsIds.length];
        for (int i = 0; i < mOptionsIds.length; i++){
            mOptions[i] = getString(mOptionsIds[i]);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.baseline_sort_black_24)
                .setTitle(getString(R.string.sort_by))
                .setSingleChoiceItems(
                        mOptions,
                        getCurrentOptionIndex(),
                        (dialogInterface, optionIndex) -> {
                           sendResult(Activity.RESULT_OK, optionIndex);
                           dialogInterface.dismiss();
                        }
                ).create();
    }

    private int getCurrentOptionIndex(){
        int currentSortFieldId = getArguments().getInt(ARG_CURRENT_SORT_FIELD_ID);

        for (int i = 0; i < mOptionsIds.length; i++){
            if (mOptionsIds[i] == currentSortFieldId){
                return i;
            }
        }

        return 0;
    }

    private void sendResult(int resultCode, int optionIndex){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_OPTION, mOptionsIds[optionIndex]);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
