package com.imaduddinsheikh.androidnotes;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class Note {

    private final String title;

    private final String text;

    private final LocalDateTime lastUpdateDateTime;

    public Note(String title, String text, LocalDateTime lastUpdateDateTime) {
        this.title = title;
        this.text = text;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public JSONObject toJSON() {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", getTitle());
            jsonObject.put("text", getText());
            jsonObject.put("lastUpdatedDateTime", getLastUpdateDateTime().toString());

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
                ", lastUpdatedDateTime=" + this.lastUpdateDateTime.toString() +
                '}';
    }
}
