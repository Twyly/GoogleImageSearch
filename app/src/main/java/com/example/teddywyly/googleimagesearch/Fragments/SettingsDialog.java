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

import com.example.teddywyly.googleimagesearch.Models.GoogleSearchSettings;
import com.example.teddywyly.googleimagesearch.R;

import java.util.Set;

/**
 * Created by teddywyly on 5/12/15.
 */
public class SettingsDialog extends DialogFragment implements View.OnClickListener {

    private GoogleSearchSettings searchSettings;
    private Spinner spnSize;
    private Spinner spnColor;
    private Spinner spnType;
    private Spinner spnRights;

    public interface SettingsDialogListener {
        void onSaveSettings();
    }

    public SettingsDialog() {

    }

    public static SettingsDialog newInstance(GoogleSearchSettings settings) {
        SettingsDialog frag = new SettingsDialog();
        Bundle args = new Bundle();
        args.putSerializable("settings", settings);
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

        GoogleSearchSettings settings = (GoogleSearchSettings)getArguments().getSerializable("settings");
        searchSettings = settings;
        getDialog().setTitle(settings.resultsPerPage + "");

        spnSize = (Spinner) view.findViewById(R.id.spnSize);
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_sizes_array, android.R.layout.simple_spinner_dropdown_item);
        spnSize.setAdapter(imageSizeAdapter);

        spnColor = (Spinner) view.findViewById(R.id.spnColor);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_colors_array, android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(colorAdapter);

        spnType = (Spinner) view.findViewById(R.id.spnType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_types_array, android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(typeAdapter);

        spnRights = (Spinner) view.findViewById(R.id.spnRights);
        ArrayAdapter<CharSequence> rightsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.image_rights_array, android.R.layout.simple_spinner_dropdown_item);
        spnRights.setAdapter(rightsAdapter);
    }

    private void updateViewsFromSettings() {

    }

    private void updateSettingsFromViews() {

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
