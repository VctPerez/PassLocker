package es.uma.passlocker.db.dao;

import android.content.Context;
import android.database.Cursor;

import es.uma.passlocker.db.DatabaseHelper;
import es.uma.passlocker.db.entities.SiteEntity;

public class SiteDao {
    private DatabaseHelper databaseHelper;

    public SiteDao(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void insertSite(String name) {
        databaseHelper.getWritableDatabase().execSQL("INSERT INTO sites (name) VALUES ('" + name + "')");
    }

    public void deleteSite(String name) {
        databaseHelper.getWritableDatabase().execSQL("DELETE FROM sites WHERE name = '" + name + "'");
    }

    public void updateSite(String oldName, String newName) {
        databaseHelper.getWritableDatabase().execSQL("UPDATE sites SET name = '" + newName + "' WHERE name = '" + oldName + "'");
    }

    public SiteEntity getSite(String name) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT id, name FROM sites WHERE name = ?", new String[]{name});
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String site = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return new SiteEntity(id, site);
        } else {
            return null;
        }
    }

}
