package com.guestlogix.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CharacterModel implements Serializable {

    public int id;
    public String name, url, created, status, species, type, gender, image;
    public ArrayList<String> episodesArrayList;
    public GeneralModel originModel, locationModel;

    public CharacterModel() {
        episodesArrayList = new ArrayList<>();
    }

    public CharacterModel(JSONObject jsonObject) {
        if (episodesArrayList == null) {
            episodesArrayList = new ArrayList<>();
        } else {
            episodesArrayList.clear();
        }
        try {
            this.id = jsonObject.optInt("id");
            this.name = jsonObject.optString("name");
            this.status = jsonObject.optString("status");
            this.species = jsonObject.optString("species");
            this.type = jsonObject.optString("type");
            this.gender = jsonObject.optString("gender");
            this.image = jsonObject.optString("image");
            this.url = jsonObject.optString("url");
            this.created = jsonObject.optString("created");
            JSONArray episodesArray = jsonObject.optJSONArray("episode");
            if (episodesArray != null && episodesArray.length() > 0) {
                for (int i = 0; i < episodesArray.length(); i++) {
                    episodesArrayList.add(episodesArray.optString(i));
                }
            }
            this.originModel = new GeneralModel(jsonObject.optJSONObject("origin"));
            this.locationModel = new GeneralModel(jsonObject.optJSONObject("location"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
