package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import db.DbHelper;

public class UpdateActivity extends AppCompatActivity {
    DbHelper dbHelper;
    ImageView backButton, saveButton;
    ImageView imageColor1,imageColor2,imageColor3,imageColor4;
    EditText titleNote,subtitleNote,textNote;
    LinearLayout miscLayout;
    TextView dateTime;
    View viewSubtitleIndicator;
    private String selectedNoteColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
    }
}