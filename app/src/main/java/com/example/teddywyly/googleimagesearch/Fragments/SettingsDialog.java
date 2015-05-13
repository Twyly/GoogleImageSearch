package com.example.teddywyly.googleimagesearch.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.teddywyly.googleimagesearch.R;

import java.util.Set;

/**
 * Created by teddywyly on 5/12/15.
 */
public class SettingsDialog extends DialogFragment implements View.OnClickListener {

    public interface SettingsDialogListener {
        void onSaveSettings();
    }

    public SettingsDialog() {

    }

    public static SettingsDialog newInstance(String title) {
        SettingsDialog frag = new SettingsDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            SettingsDialogListener listener = (SettingsDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SettingsDialogListener");
        }
        Log.d("DEBUG", "Attached!");
    }

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container);
        setupViews(view);

        // Show soft keyboard automatically
        //mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    private void setupViews(View view) {
        Button btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        Spinner imageSizeSpinner = (Spinner) view.findViewById(R.id.spnImageSize);
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_sizes_array, android.R.layout.simple_spinner_dropdown_item);
        imageSizeSpinner.setAdapter(imageSizeAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            SettingsDialogListener listener = (SettingsDialogListener) getActivity();
            listener.onSaveSettings();
            dismiss();
        } else if (view.getId() == R.id.btnCancel) {
            dismiss();
        }
    }
}
