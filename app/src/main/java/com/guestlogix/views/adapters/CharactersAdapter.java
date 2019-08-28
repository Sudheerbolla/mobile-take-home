package com.guestlogix.views.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.R;
import com.guestlogix.listeners.IClickListener;
import com.guestlogix.models.CharacterModel;
import com.guestlogix.utils.StaticUtils;

import java.util.ArrayList;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.MyViewHolder> {

    private ArrayList<CharacterModel> episodeModelArrayList;
    private Context context;
    private IClickListener iClickListener;

    public CharactersAdapter(Context context, ArrayList<CharacterModel> episodeModelArrayList, IClickListener iClickListener) {
        this.context = context;
        this.iClickListener = iClickListener;
        this.episodeModelArrayList = episodeModelArrayList;
    }

    @NonNull
    @Override
    public CharactersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharactersAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_characters, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CharactersAdapter.MyViewHolder holder, final int position) {
        CharacterModel characterModel = episodeModelArrayList.get(position);
        holder.txtCharacterName.setText(characterModel.name);
        holder.txtCreated.setText(String.format(context.getString(R.string.created_string_format), characterModel.id, StaticUtils.getDays(characterModel.created)));
        if (!TextUtils.isEmpty(characterModel.status) && !characterModel.status.equalsIgnoreCase("unknown")) {
            if (characterModel.status.equalsIgnoreCase("Dead")) {
                holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                holder.relImage.setBackground(ContextCompat.getDrawable(context, R.drawable.red_border));
            } else if (characterModel.status.equalsIgnoreCase("Alive")) {
                holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                holder.relImage.setBackground(ContextCompat.getDrawable(context, R.drawable.green_border));
            } else {
                holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGrey));
                holder.relImage.setBackground(ContextCompat.getDrawable(context, R.drawable.edt_background));
            }
        } else {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGrey));
            holder.relImage.setBackground(ContextCompat.getDrawable(context, R.drawable.edt_background));
        }
        holder.txtStatus.setText(characterModel.status);

        holder.txtSpecies.setText(String.format("%s: %s", context.getString(R.string.species), characterModel.species));
        holder.txtGender.setText(String.format("%s: %s", context.getString(R.string.gender), characterModel.gender));
        holder.txtOrigin.setText(String.format("%s: %s", context.getString(R.string.origin), characterModel.originModel.name));
        holder.txtLastLocation.setText(String.format("%s: %s", context.getString(R.string.last_location), characterModel.locationModel.name));

        holder.relBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iClickListener != null) iClickListener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView txtCharacterName, txtCreated, txtStatus, txtSpecies, txtGender, txtOrigin, txtLastLocation;
        private RelativeLayout relBody, relImage;

        MyViewHolder(View view) {
            super(view);
            txtCharacterName = view.findViewById(R.id.txtCharacterName);
            txtCreated = view.findViewById(R.id.txtCreated);
            txtStatus = view.findViewById(R.id.txtStatus);
            relBody = view.findViewById(R.id.relBody);
            txtSpecies = view.findViewById(R.id.txtSpecies);
            txtGender = view.findViewById(R.id.txtGender);
            txtOrigin = view.findViewById(R.id.txtOrigin);
            txtLastLocation = view.findViewById(R.id.txtLastLocation);
            relImage = view.findViewById(R.id.relImage);
        }
    }

}