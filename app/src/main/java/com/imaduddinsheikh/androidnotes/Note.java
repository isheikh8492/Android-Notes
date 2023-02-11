package com.imaduddinsheikh.androidnotes;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Note implements Serializable {
    private String title;

    private String text;

    private long lastUpdateDateTime;

    private int counter = 1;

    Note(String title, String text) {
        this.title = title;
        this.text = text;
        this.lastUpdateDateTime = System.currentTimeMillis();
        this.counter++;
    }

    public void setTitle(String title) {
        this.title = title;
        this.lastUpdateDateTime = System.currentTimeMillis();
    }

    public void setText(String text) {
        this.text = text;
        this.lastUpdateDateTime = System.currentTimeMillis();
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public long getLastUpdateDateTime() {
        return this.lastUpdateDateTime;
    }



    public JSONObject toJSON() {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", getTitle());
            jsonObject.put("text", getText());
            jsonObject.put("lastUpdatedDateTime", getLastUpdateDateTime());

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "title='" + this.title + '\'' +
                ", text='" + this.text + '\'' +
                ", lastUpdatedDateTime=" + this.lastUpdateDateTime +
                '}';
    }
}
