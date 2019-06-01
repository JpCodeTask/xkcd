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

    static final String EXTRA_SELECTED_OPTION = "EXTRA_SELECTED_OPTION";
    private CharSequence[] mOptions;

    static SortDialogFragment newInstance() {
        return new SortDialogFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOptions = getResources().getTextArray(R.array.sort_by_array);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.baseline_sort_black_24)
                .setTitle(getString(R.string.sort_by))
                .setItems(
                        mOptions,
                        (dialogInterface, i) -> {
                           sendResult(Activity.RESULT_OK, mOptions[i].toString());
                        }
                ).create();
    }

    private void sendResult(int resultCode, String selectedOption){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_OPTION, selectedOption);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
