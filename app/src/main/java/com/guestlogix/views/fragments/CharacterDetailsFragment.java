package com.guestlogix.views.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.guestlogix.R;
import com.guestlogix.views.activities.MainActivity;
import com.guestlogix.listeners.IImageLoadedListener;
import com.guestlogix.models.CharacterModel;
import com.guestlogix.services.LoadImageFromServerTask;
import com.guestlogix.utils.StaticUtils;

public class CharacterDetailsFragment extends DialogFragment {

    private View rootView;
    private MainActivity mainActivity;
    private AppCompatTextView txtCharacterName, txtCreated, txtStatus, txtSpecies, txtGender, txtOrigin, txtLastLocation, txtLoadingFailed;
    private AppCompatImageView imgCharacter;
    private ProgressBar imgProgress;

    private CharacterModel characterModel;

    public CharacterDetailsFragment() {
    }

    static CharacterDetailsFragment newInstance(CharacterModel characterModel) {
        CharacterDetailsFragment charactersFragment = new CharacterDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("characterModel", characterModel);
        charactersFragment.setArguments(bundle);
        return charactersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                characterModel = (CharacterModel) bundle.getSerializable("characterModel");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_character_details, container, false);
        initComponents();
        return rootView;
    }

    private void initComponents() {
        setReferences();
        setData();
    }

    private void setData() {
        txtCharacterName.setText(characterModel.name);
        txtCreated.setText(String.format(getString(R.string.created_string_format), characterModel.id, StaticUtils.getDays(characterModel.created)));

        if (!TextUtils.isEmpty(characterModel.status) && !characterModel.status.equalsIgnoreCase("unknown")) {
            if (characterModel.status.equalsIgnoreCase(getString(R.string.dead))) {
                txtStatus.setTextColor(ContextCompat.getColor(mainActivity, R.color.colorRed));
            } else if (characterModel.status.equalsIgnoreCase(getString(R.string.alive))) {
                txtStatus.setTextColor(ContextCompat.getColor(mainActivity, R.color.colorGreen));
            } else {
                txtStatus.setTextColor(ContextCompat.getColor(mainActivity, R.color.colorGrey));
            }
        } else {
            txtStatus.setTextColor(ContextCompat.getColor(mainActivity, R.color.colorGrey));
        }
        txtStatus.setText(characterModel.status);

        txtSpecies.setText(characterModel.species);
        txtGender.setText(characterModel.gender);
        txtOrigin.setText(characterModel.originModel.name);
        txtLastLocation.setText(characterModel.locationModel.name);
        new LoadImageFromServerTask(new IImageLoadedListener() {
            @Override
            public void successResponse(Bitmap bitmap) {
                txtLoadingFailed.setVisibility(View.GONE);
                imgProgress.setVisibility(View.GONE);
                imgCharacter.setVisibility(View.VISIBLE);
                imgCharacter.setImageBitmap(bitmap);
            }

            @Override
            public void errorResponse() {
                txtLoadingFailed.setVisibility(View.VISIBLE);
                imgProgress.setVisibility(View.GONE);
                imgCharacter.setVisibility(View.GONE);
            }
        }).execute(characterModel.image);
    }

    private void setReferences() {
        txtStatus = rootView.findViewById(R.id.txtStatus);
        txtSpecies = rootView.findViewById(R.id.txtSpecies);
        txtGender = rootView.findViewById(R.id.txtGender);
        txtOrigin = rootView.findViewById(R.id.txtOrigin);
        txtCreated = rootView.findViewById(R.id.txtCreated);
        txtCharacterName = rootView.findViewById(R.id.txtCharacterName);
        txtLastLocation = rootView.findViewById(R.id.txtLastLocation);
        imgCharacter = rootView.findViewById(R.id.imgCharacter);
        imgProgress = rootView.findViewById(R.id.imgProgress);
        txtLoadingFailed = rootView.findViewById(R.id.txtLoadingFailed);
    }

}
