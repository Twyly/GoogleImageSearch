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
    private EditText etSite;

    public interface SettingsDialogListener {
        void onSaveSettings(GoogleSearchSettings settings);
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

        etSite = (EditText)view.findViewById(R.id.etSite);

        GoogleSearchSettings settings = (GoogleSearchSettings)getArguments().getSerializable("settings");
        searchSettings = settings;
        Log.i("DEBUGCOLOR", settings.color.toString());
        getDialog().setTitle("Filter Search");

        spnSize = (Spinner) view.findViewById(R.id.spnSize);
        spnSize.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageSize>(getActivity(), android.R.layout.simple_spinner_dropdown_item, GoogleSearchSettings.ImageSize.values()));

        spnColor = (Spinner) view.findViewById(R.id.spnColor);
        spnColor.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageColor>(getActivity(), android.R.layout.simple_spinner_dropdown_item, GoogleSearchSettings.ImageColor.values()));

        spnType = (Spinner) view.findViewById(R.id.spnType);
        spnType.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageType>(getActivity(), android.R.layout.simple_spinner_dropdown_item, GoogleSearchSettings.ImageType.values()));

        spnRights = (Spinner) view.findViewById(R.id.spnRights);
        spnRights.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageRights>(getActivity(), android.R.layout.simple_spinner_dropdown_item, GoogleSearchSettings.ImageRights.values()));

        updateViewsFromSettings();
    }


    private void updateViewsFromSettings() {
        spnSize.setSelection(((ArrayAdapter<GoogleSearchSettings.ImageSize>)spnSize.getAdapter()).getPosition(searchSettings.size));
        spnColor.setSelection(((ArrayAdapter<GoogleSearchSettings.ImageColor>)spnColor.getAdapter()).getPosition(searchSettings.color));
        spnType.setSelection(((ArrayAdapter<GoogleSearchSettings.ImageType>)spnType.getAdapter()).getPosition(searchSettings.type));
        spnRights.setSelection(((ArrayAdapter<GoogleSearchSettings.ImageRights>)spnRights.getAdapter()).getPosition(searchSettings.rights));
        etSite.setText(searchSettings.siteFilter);
    }

    private void updateSettingsFromViews() {
        searchSettings.size = (GoogleSearchSettings.ImageSize)spnSize.getSelectedItem();
        searchSettings.color = (GoogleSearchSettings.ImageColor)spnColor.getSelectedItem();
        searchSettings.type = (GoogleSearchSettings.ImageType)spnType.getSelectedItem();
        searchSettings.rights = (GoogleSearchSettings.ImageRights)spnRights.getSelectedItem();
        searchSettings.siteFilter = etSite.getText().toString();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            SettingsDialogListener listener = (SettingsDialogListener) getActivity();
            updateSettingsFromViews();
            listener.onSaveSettings(searchSettings);
            dismiss();
        } else if (view.getId() == R.id.btnCancel) {
            dismiss();
        }
    }

//    public enum ImageSize {
//        ANY("Any"), SMALL("Small"), MEDIUM("Medium"), LARGE("Large"), EXTRA_LARGE("Extra Large");
//        private String displayName;
//        private ImageSize(String displayName) {
//            this.displayName = displayName;
//        }
//
//        @Override
//        public String toString() {
//            return displayName;
//        }
//    }
//
//    public enum ImageColor {
//        ANY("Any"), BLACK("black"), BLUE("blue"), BROWN("brown"), GRAY("gray"), GREEN("green"),
//        ORANGE("orange"), PINK("pink"), PURPLE("purple"), RED("red"), TEAL("teal"),
//        WHITE("white"), YELLOW("yellow");
//
//        private String displayName;
//
//        private ImageColor(String displayName) {
//            this.displayName = displayName;
//        }
//
//        @Override
//        public String toString() {
//            return displayName;
//        }
//    }
//
//    // Spinner Titles
//    public enum ImageType {
//        ANY("Any"), FACE("Face"), PHOTO("photo"), CLIPART("clipart"), LINEART("lineart");
//        private String displayName;
//        private ImageType(String displayName) {
//            this.displayName = displayName;
//        }
//
//        @Override
//        public String toString() {
//            return displayName;
//        }
//    }
//
//    public enum ImageRights {
//        ALL("All"), PUBLIC_DOMAIN("Public Domain"), ATTRIBUTE("Attributable"), NON_COMMERCIAL("Noncommerical");
//        private String displayName;
//        private ImageRights(String displayName) {
//            this.displayName = displayName;
//        }
//
//        @Override
//        public String toString() {
//            displayName;
//        }
//    }
}
