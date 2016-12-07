package com.asobrab.thirdretrofit.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.asobrab.thirdretrofit.model.Contributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asobrab on 06/11/16.
 */

public class RepositorioContributor {
    private SQLiteHelper sqLiteHelper;

    public RepositorioContributor(Context ctx){
        sqLiteHelper = SQLiteHelper.getInstance(ctx);
    }

    public void inserir(Contributor contributor){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseConstantes._ID, contributor.getId());
        cv.put(DatabaseConstantes.LOGIN, contributor.getLogin());
        cv.put(DatabaseConstantes.AVATAR_URL, contributor.getAvatar_url());
        cv.put(DatabaseConstantes.CONTRIBUTORS, contributor.getContributions());
        cv.put(DatabaseConstantes.NAME, contributor.getName());
        cv.put(DatabaseConstantes.COMPANY, contributor.getCompany());
        cv.put(DatabaseConstantes.BLOG, contributor.getBlog());
        cv.put(DatabaseConstantes.LOCATION, contributor.getLocation());
        cv.put(DatabaseConstantes.EMAIL, contributor.getEmail());

        db.insert("contributors", null, cv);
        Log.d("Log de inserir", "inserir: inseriu tudo vei");
        db.close();
    }

    public Contributor selecionaEscalar(String login){
        Contributor contributor = new Contributor();

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contributors WHERE " +
                DatabaseConstantes.LOGIN + " = ? ", new String[]{login});

        if(cursor.moveToNext()) {
            contributor = getContributorFromCursor(cursor);
        }
        db.close();
        return contributor;
    }

    public List<Contributor> listaContributors(){
        List<Contributor> lista = new ArrayList<Contributor>();
        String selectQuery =
                "SELECT * FROM contributors ";

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while(cursor.moveToNext()) {
            lista.add(getContributorFromCursor(cursor));
        }
        db.close();
        return lista;
    }


    public void deletaContributor(String login){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.delete(" contributors ", " login = ?",
                new String[]{ login });
        Log.d("Log de apagar", "delPoke: apagou o contribuidor");
        db.close();
    }

    private Contributor getContributorFromCursor(Cursor cursor) {
        Contributor contributor = new Contributor();
        if(cursor != null){
            contributor.setId((int) cursor.getLong(cursor.getColumnIndex(DatabaseConstantes._ID)));
            contributor.setLogin(cursor.getString(cursor.getColumnIndex(DatabaseConstantes.LOGIN)));
            contributor.setAvatar_url(cursor.getString(cursor.getColumnIndex(DatabaseConstantes.AVATAR_URL)));
            contributor.setContributions((int) cursor.getLong(cursor.getColumnIndex(DatabaseConstantes.CONTRIBUTORS)));
            contributor.setName(cursor.getString(cursor.getColumnIndex(DatabaseConstantes.NAME)));
            contributor.setCompany(cursor.getString(cursor.getColumnIndex(DatabaseConstantes.COMPANY)));
            contributor.setBlog(cursor.getString(cursor.getColumnIndex(DatabaseConstantes.BLOG)));
            contributor.setLocation(cursor.getString(cursor.getColumnIndex(DatabaseConstantes.LOCATION)));
            contributor.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseConstantes.EMAIL)));
        }
        return contributor;
    }
}
