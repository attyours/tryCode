package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.project3.adapter.NotesAdapter;
import db.DbHelper;
import model.Notes;

public class MainActivity extends AppCompatActivity {
    DbHelper dbHelper;
    ImageView addButton, searchButton;
    EditText searchText;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    ArrayList<Notes> notesArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView= (RecyclerView) findViewById(R.id.rview);


        addButton=findViewById(R.id.imageAddNoteMain);
        searchButton=findViewById(R.id.searchIcon);
        searchText=findViewById(R.id.inputSearch);



        notesAdapter = new NotesAdapter( this);
        dbHelper = new DbHelper(this);
        notesArrayList = dbHelper.getAllNotes();

        notesAdapter.setNotesArrayList(notesArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(notesAdapter);

//      When add button clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    dbHelper.getAllNotes();
                    Intent intent = new Intent(MainActivity.this,CreateActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"error :"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
//      When try to search
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    dbHelper.getSearch(searchText.getText().toString());
                    String value = searchText.getText().toString();
                    int size = dbHelper.getSearch(searchText.getText().toString()).size();
                    Toast.makeText(MainActivity.this,"success "+size +" "+value,Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"error :"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}