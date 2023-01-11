package model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Notes implements Serializable {
    private int id;
    private String title;
    private String subtitle;
    private String dateTime;
    private String noteText;
    private String color;
    private byte[] image;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public byte[] getImage() {return image; }

    public void setImage(byte[] image) {this.image = image;}
    @NonNull
    @Override
    public String toString(){
        return dateTime;
    }
}
