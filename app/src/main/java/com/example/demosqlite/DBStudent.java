package com.example.demosqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Currency;

public class DBStudent extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Student";
    public static final int DATABASE_VERSION = 1;
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_SCORE = "score";
    public DBStudent(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStatement = "create table "+TABLE_NAME+" ( " +
                COL_ID+" integer primary key autoincrement, " +
                COL_NAME+" text, " +
                COL_SCORE+" real)";
        db.execSQL(sqlStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists "+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public void addNewStudent(Student student){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, student.getName());
        values.put(COL_SCORE, student.getScore());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateStudent(Student student){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, student.getName());
        values.put(COL_SCORE, student.getScore());
        db.update(TABLE_NAME, values, COL_ID+" =? ", new String[]{String.valueOf(student.getId())});
        db.close();
    }

    public void deleteStudent(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID+" =? ", new String[]{String.valueOf(id)});
    }

    public ArrayList<Student> getAllStudent(){
        ArrayList<Student> listResult = new ArrayList<>();
        String queryStatement = "select * from "+TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStatement, null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                float score = cursor.getFloat(2);
                listResult.add(new Student(id, name, score));
            }while (cursor.moveToNext());
        }
        return listResult;
    }

    public int getStudentCount(){
        String queryStatement = "select * from "+TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStatement, null);
        return cursor.getCount();
    }

    public void addThreeStudent(){
        Student student1 = new Student("Vu Huy Hieu", 9.5);
        Student student2 = new Student("Nguyen Ha Thu", 8.0);
        Student student3 = new Student("Tran Phuong Thuy", 8.5);
        addNewStudent(student1);
        addNewStudent(student2);
        addNewStudent(student3);
    }
}
