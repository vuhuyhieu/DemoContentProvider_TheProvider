package com.example.demosqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import static com.example.demosqlite.DBStudent.COL_ID;
import static com.example.demosqlite.DBStudent.TABLE_NAME;

public class MyProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.demosqlite.MyProvider";
    static final String CONTENT_PATH = "backupdata";
    static final String URL = "content://"+AUTHORITY+"/"+CONTENT_PATH;
    static final Uri CONTENT_URI = Uri.parse(URL);

    private static final int STUDENT = 1;
    private static final int STUDENT_ID = 2;
    static final int uriCode = 1;

    static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, uriCode);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH+"/*", uriCode);
    }

    private SQLiteDatabase database;
    @Override
    public boolean onCreate(){
        DBStudent helper = new DBStudent(getContext());
        database = helper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                cursor =  database.rawQuery("select * from "+TABLE_NAME, null);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                return AUTHORITY+"/"+CONTENT_PATH;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert(TABLE_NAME,null,values);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                delCount =  database.delete(TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                updCount =  database.update(TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
}
