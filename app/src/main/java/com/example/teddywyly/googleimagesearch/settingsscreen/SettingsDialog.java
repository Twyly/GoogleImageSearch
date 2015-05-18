package com.example.teddywyly.googleimagesearch.settingsscreen;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.teddywyly.googleimagesearch.searchscreen.GoogleSearchSettings;
import com.example.teddywyly.googleimagesearch.R;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

/**
 * Created by teddywyly on 5/12/15.
 */
public class SettingsDialog extends SupportBlurDialogFragment implements View.OnClickListener {

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
        args.putParcelable("settings", settings);
        frag.setArguments(args);
        frag.setStyle(STYLE_NORMAL, R.style.dialog_light);
        return frag;
    }



    @Override
    protected boolean isDimmingEnable() {
        return true;
    }

    @Override
    protected int getBlurRadius() {
        return 40;
    }


    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            SettingsDialogListener listener = (SettingsDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SettingsDialogListener");
        }
    }

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container);
        setupViews(view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return view;
    }

    private void setupViews(View view) {

        Button btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        etSite = (EditText)view.findViewById(R.id.etSite);
        GoogleSearchSettings settings = (GoogleSearchSettings)getArguments().getParcelable("settings");

        searchSettings = settings;
        getDialog().setTitle("Filter Search");

        spnSize = (Spinner) view.findViewById(R.id.spnSize);
        spnSize.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageSize>(getActivity(), R.layout.spinner_item, GoogleSearchSettings.ImageSize.values()));

        spnColor = (Spinner) view.findViewById(R.id.spnColor);
        spnColor.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageColor>(getActivity(), R.layout.spinner_item, GoogleSearchSettings.ImageColor.values()));

        spnType = (Spinner) view.findViewById(R.id.spnType);
        spnType.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageType>(getActivity(), R.layout.spinner_item, GoogleSearchSettings.ImageType.values()));

        spnRights = (Spinner) view.findViewById(R.id.spnRights);
        spnRights.setAdapter(new ArrayAdapter<GoogleSearchSettings.ImageRights>(getActivity(), R.layout.spinner_item, GoogleSearchSettings.ImageRights.values()));

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

}
