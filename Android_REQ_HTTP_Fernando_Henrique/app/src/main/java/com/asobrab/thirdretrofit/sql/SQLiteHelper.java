package com.asobrab.thirdretrofit.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asobrab on 06/11/16.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dbcontributors";
    public static final int DB_VERSION = 1;

    private static SQLiteHelper instance;
    private Context ctx;

    private SQLiteHelper(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
        this.ctx = ctx;
    }

    public static synchronized SQLiteHelper getInstance(Context ctx){
        if(instance == null){
            instance = new SQLiteHelper(ctx);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contributors ("
                + DatabaseConstantes._ID + " integer primary key autoincrement,"
                + DatabaseConstantes.LOGIN + " text null, "
                + DatabaseConstantes.AVATAR_URL + " text null, "
                + DatabaseConstantes.CONTRIBUTORS + " integer null, "
                + DatabaseConstantes.NAME + " text null, "
                + DatabaseConstantes.COMPANY + " text null, "
                + DatabaseConstantes.BLOG + " text null, "
                + DatabaseConstantes.LOCATION + " text null, "
                + DatabaseConstantes.EMAIL + " text null); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
