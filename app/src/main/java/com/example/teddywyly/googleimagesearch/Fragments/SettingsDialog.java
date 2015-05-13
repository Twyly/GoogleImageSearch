package com.example.teddywyly.googleimagesearch.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.teddywyly.googleimagesearch.R;

import java.util.Set;

/**
 * Created by teddywyly on 5/12/15.
 */
public class SettingsDialog extends DialogFragment {

    public SettingsDialog() {

    }

    public static SettingsDialog newInstance(String title) {
        SettingsDialog frag = new SettingsDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container);
        //mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        Spinner imageSizeSpinner = (Spinner) view.findViewById(R.id.spnImageSize);
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_sizes_array, android.R.layout.simple_spinner_dropdown_item);
        imageSizeSpinner.setAdapter(imageSizeAdapter);

        // Show soft keyboard automatically
        //mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}
