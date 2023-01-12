package db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

import model.Notes;

public class DbHelper extends SQLiteOpenHelper {
    public static String DB_NAME="dbnote";
    private static final int DB_VERSION =1;
    private static final String TABLE_NAME="note";
    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_SUBTITLE="subtitle";
    private static final String KEY_DATE="dates";
    private static final String KEY_TEXTS="text";
    private static final String KEY_COLORS="color";
    private static final String KEY_IMAGE="image";

    private static final String CREATE_TABLE_NOTES =
            "CREATE TABLE "+ TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_SUBTITLE + " TEXT,"
                    + KEY_IMAGE + " BLOB,"
                    + KEY_DATE + " TEXT,"
                    + KEY_TEXTS + " TEXT,"
                    + KEY_COLORS + " TEXT);";


    public DbHelper(Context context){super(context,DB_NAME,null,DB_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTES);
//        sqLiteDatabase.execSQL(TESTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS'"+TABLE_NAME+"'");
        onCreate(sqLiteDatabase);
    }

    public long addNotes(String title, String subtitle, byte [] image, String date, String text, String colors){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE,title);
        values.put(KEY_SUBTITLE,subtitle);
        values.put(KEY_IMAGE, image);
        values.put(KEY_DATE,date);
        values.put(KEY_TEXTS,text);
        values.put(KEY_COLORS,colors);

        long insert= sqLiteDatabase.insert(TABLE_NAME,null,values);
        return insert;
    }

    //    Get all data from database
    @SuppressLint("Range")
    public ArrayList<Notes> getAllNotes(){
        ArrayList<Notes> notesArrayList = new ArrayList<>();
        String q = "SELECT * FROM " + TABLE_NAME ;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(q, null);

        if(c.moveToFirst()){
            do{
                Notes note= new Notes();
                note.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                note.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                note.setImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
                note.setSubtitle(c.getString(c.getColumnIndex(KEY_SUBTITLE)));
                note.setDateTime(c.getString(c.getColumnIndex(KEY_DATE)));
                note.setNoteText(c.getString(c.getColumnIndex(KEY_TEXTS)));
                note.setColor(c.getString(c.getColumnIndex(KEY_COLORS)));
                notesArrayList.add(note);
            }while(c.moveToNext());
        }
        return notesArrayList;
    }

    // Get data based on search input
    @SuppressLint("Range")
    public ArrayList<Notes> getSearch(String value){
        ArrayList<Notes> notesArrayList = new ArrayList<>();
        String search ="SELECT * FROM "+TABLE_NAME +" WHERE " +KEY_TITLE+" = ? AND "+KEY_SUBTITLE+" = ?";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery(search,new String[]{value});

        if(c.moveToFirst()){
            do{
                Notes note= new Notes();
                note.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                note.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                note.setSubtitle(c.getString(c.getColumnIndex(KEY_SUBTITLE)));
                note.setDateTime(c.getString(c.getColumnIndex(KEY_DATE)));
                note.setNoteText(c.getString(c.getColumnIndex(KEY_TEXTS)));
                note.setColor(c.getString(c.getColumnIndex(KEY_COLORS)));
                note.setImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
            }while(c.moveToNext());
        }
        return notesArrayList;
    }
    //  Delete data from database

    public void deleteNotes(int id, Uri uri){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (uri != null){
            String path = uri.getEncodedPath();
            File file = new File(path);
            if (file.exists()){
                file.delete();
            }
        }
        sqLiteDatabase.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    //   Update data from database
    public int updateNotes(int id, String title, String subtitle,byte[] image,String date,String text, String color){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE,title);
        values.put(KEY_SUBTITLE,subtitle);
        values.put(KEY_IMAGE,image);
        values.put(KEY_DATE,date);
        values.put(KEY_TEXTS,text);
        values.put(KEY_COLORS,color);

        return sqLiteDatabase.update(TABLE_NAME,values,KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

}