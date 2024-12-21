package es.uma.passlocker.db.dao;

import android.database.Cursor;

import es.uma.passlocker.db.DatabaseHelper;
import es.uma.passlocker.db.entities.SiteEntity;

public class SiteDao {
    private DatabaseHelper databaseHelper;
    private static SiteDao siteDao;

    private SiteDao() {
        this.databaseHelper = DatabaseHelper.getInstance();
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

    public SiteEntity getSite(int id) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT id, name FROM sites WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            int siteId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return new SiteEntity(siteId, name);
        } else {
            return null;
        }
    }

    public static SiteDao getInstance() {
        if (siteDao == null) {
            siteDao = new SiteDao();
        }
        return siteDao;
    }
}
