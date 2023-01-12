package com.example.project3;

import static com.example.project3.CreateActivity.getBytes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import db.DbHelper;
import model.Notes;

public class UpdateActivity extends AppCompatActivity {
    DbHelper dbHelper;
    ImageView backButton, deleteButton, saveButton, imageNote;
    ImageView imageColor1,imageColor2,imageColor3,imageColor4, imageColor5;
    EditText titleNote,subtitleNote,textNote;
    LinearLayout miscLayout, miscImageLayout;
    TextView dateTime;
    View viewSubtitleIndicator;
    private String selectedNoteColor;
    private Notes note;
    byte[] imageBytes=null;
    byte[] bytes = null;
    Uri imageUri = null;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbHelper = new DbHelper(this);

        backButton = findViewById(R.id.imageBack);
        deleteButton = findViewById(R.id.imageDelete);
        saveButton = findViewById(R.id.imageSave);
        titleNote = findViewById(R.id.inputNoteTitle);
        subtitleNote = findViewById(R.id.inputNoteSubtitle);
        textNote = findViewById(R.id.inputNote);
        dateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        imageNote = findViewById(R.id.imgNote);

        Intent intent = getIntent();
        note = (Notes) intent.getSerializableExtra("note");

        bytes = note.getImage();

        titleNote.setText(note.getTitle());
        subtitleNote.setText(note.getSubtitle());
        textNote.setText(note.getNoteText());
        dateTime.setText(note.getDateTime());


        id = note.getId();

        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageNote.setImageBitmap(bitmap);
            imageNote.setVisibility(View.VISIBLE);
        }

        final LinearLayout miscImageLayout = findViewById(R.id.miscImageLayout);
        miscImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

//      Call note card color indicator function
        indicator();

//      Function to get date local
        dateTime.setText(
                new SimpleDateFormat("dd MMMM yyy HH:mm", Locale.getDefault())
                        .format(new Date())
        );
//      Define function when back button clicked
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(titleNote.getText().toString().isEmpty()){
                    Toast.makeText(UpdateActivity.this,"Error:Title need to be filled!",
                            Toast.LENGTH_SHORT).show();
                }else if(subtitleNote.getText().toString().isEmpty()){
                    Toast.makeText(UpdateActivity.this,"Error:Subtitle need to be filled!",
                            Toast.LENGTH_SHORT).show();
                }else if(textNote.getText().toString().isEmpty()){
                    Toast.makeText(UpdateActivity.this,"Error:Notes need to be filled!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        if(imageUri != null){
                            imageBytes = getBytes(UpdateActivity.this, imageUri);
                        }else{
                            imageBytes = bytes;
                        }
                        final Notes notes = new Notes();
                        notes.setTitle(titleNote.getText().toString());
                        notes.setSubtitle(subtitleNote.getText().toString());
                        notes.setDateTime(dateTime.getText().toString());
                        notes.setNoteText(textNote.getText().toString());
                        notes.setColor(selectedNoteColor);
                        dbHelper.updateNotes(id,notes.getTitle(),notes.getSubtitle(),imageBytes ,notes.getDateTime()
                                ,notes.getNoteText(),notes.getColor());
                        Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Error: "+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public void showConfirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("Delete confirmation");
        builder.setMessage("This NOtes will be deleted form your device");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.deleteNotes(id, imageUri);
                Toast.makeText(UpdateActivity.this, "Notes successfully deleted.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                UpdateActivity.this.finish();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void setViewSubtitleIndicator(){
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
    //    Function set indicator color
    private void indicator(){
        final LinearLayout miscLayout = findViewById(R.id.miscLayout);
        final ImageView imageColor1=miscLayout.findViewById(R.id.imageColor1);
        final ImageView imageColor2=miscLayout.findViewById(R.id.imageColor2);
        final ImageView imageColor3=miscLayout.findViewById(R.id.imageColor3);
        final ImageView imageColor4=miscLayout.findViewById(R.id.imageColor4);
        final ImageView imageColor5=miscLayout.findViewById(R.id.imageColor5);




        miscLayout.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor ="#80171C26";
                imageColor1.setImageResource(R.drawable.ic_select);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        miscLayout.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor ="#229203";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_select);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        miscLayout.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor ="#750A0A";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_select);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        miscLayout.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor ="#0C80DA";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_select);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        miscLayout.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor ="#4C4C4C";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_select);
                setViewSubtitleIndicator();
            }
        });
    }

    public void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(intent);
    }

    public static byte[] getBytes(Context context, Uri uri) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        try {
            return getBytes(iStream);
        } finally {
            // close the stream
            try {
                iStream.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try{ byteBuffer.close(); } catch (IOException ignored){ /* do nothing */ }
        }
        return bytesResult;
    }

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent  = result.getData();
                            if(intent != null && intent.getData() != null){
                                imageUri = intent.getData();
                                imageNote.setImageURI(imageUri);
                                imageNote.setVisibility(View.VISIBLE);
                            }
                        }
                    }
            );

}