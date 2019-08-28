package com.guestlogix.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class EpisodeModel implements Serializable {

    public int id;
    public String name, airDate, episode, url, created;
    public ArrayList<String> charactersArrayList;

    public EpisodeModel() {
        charactersArrayList = new ArrayList<>();
    }

    public EpisodeModel(JSONObject jsonObject) {
        if (charactersArrayList == null) {
            charactersArrayList = new ArrayList<>();
        } else {
            charactersArrayList.clear();
        }
        try {
            this.id = jsonObject.getInt("id");
            this.name = jsonObject.getString("name");
            this.airDate = jsonObject.getString("air_date");
            this.episode = jsonObject.getString("episode");
            this.url = jsonObject.getString("url");
            this.created = jsonObject.getString("created");
            JSONArray charactersArray = jsonObject.getJSONArray("characters");
            if (charactersArray.length() > 0) {
                for (int i = 0; i < charactersArray.length(); i++) {
                    charactersArrayList.add(charactersArray.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
