package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

public class SettingDialogFragments extends DialogFragment {

    public static final String TAG = "MyRunsFragment.java";
    public static final int DIALOG_ID_ERROR = -1;
    public static final int DIALOG_ID_HOME = 0;
    public static final int DIALOG_ID_WORK = 1;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        int currentPosition = getArguments().getInt(Settings.INDEX);
        Log.d("curPs",Integer.toString(currentPosition));
        final Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        final EditText et = new EditText(parent);


        switch(currentPosition) {
            case DIALOG_ID_HOME:
                Log.d(TAG, "TITLE fragment");
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setHint(R.string.home_address_prompt);
                builder.setTitle(R.string.home_address);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // save?? shared prefs?
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();

            case DIALOG_ID_WORK:
                Log.d(TAG, "TITLE fragment");
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setHint(R.string.work_address_prompt);
                builder.setTitle(R.string.work_address);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((CreateEvent)getActivity()).getEvent().setTitle(et.getText().toString());
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();
        }
        return null;
    }
}