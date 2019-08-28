package com.guestlogix.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GeneralModel implements Serializable {

    public String name, url;

    public GeneralModel() {
    }

    public GeneralModel(JSONObject jsonObject) {
        try {
            this.name = jsonObject.optString("name");
            this.url = jsonObject.optString("url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}