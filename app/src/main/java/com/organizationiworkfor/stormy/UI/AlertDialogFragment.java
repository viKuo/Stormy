package com.organizationiworkfor.stormy.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.organizationiworkfor.stormy.R;

/**
 * Created by Vivien on 9/16/2015.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_Pos_Button, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
