package com.confortapp.leon.confortapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Leon on 12.02.2018.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "Confort.db";
    private static final int DB_VER = 1;


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //Favorites
    public void addToFavorites(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(ProductsId) VALUES('%s');", productId);
        db.execSQL(query);
    }

    public void removeFromFavorites(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE ProductsId='%s';", productId);
        db.execSQL(query);
    }

    public boolean isFavorite(String productId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE ProductsId='%s';",productId);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
