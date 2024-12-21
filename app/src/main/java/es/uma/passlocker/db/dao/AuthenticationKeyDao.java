package es.uma.passlocker.db.dao;

import android.database.Cursor;

import es.uma.passlocker.db.DatabaseHelper;
import es.uma.passlocker.db.entities.AuthenticationKeyEntity;

public class AuthenticationKeyDao {
    private DatabaseHelper databaseHelper;
    private static AuthenticationKeyDao instance;


    private AuthenticationKeyDao() {
        databaseHelper = DatabaseHelper.getInstance();
    }

    public void insertAuthenticationKey(String key, Integer UserId, Integer SiteId) {
        databaseHelper.getWritableDatabase().execSQL("INSERT INTO authentication_keys (key, user_id, site_id) VALUES ('" + key + "', " + UserId + ", " + SiteId + ")");
    }

    public void deleteAuthenticationKey(String key) {
        databaseHelper.getWritableDatabase().execSQL("DELETE FROM authentication_keys WHERE key = '" + key + "'");
    }

    public void updateAuthenticationKey(String oldKey, String newKey) {
        databaseHelper.getWritableDatabase().execSQL("UPDATE authentication_keys SET key = '" + newKey + "' WHERE key = '" + oldKey + "'");
    }

    public AuthenticationKeyEntity getAuthenticationKey(String key) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT id, authKey, user_id, site_id FROM authentication_keys WHERE key = ?", new String[]{key});
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));

            int siteId = cursor.getInt(cursor.getColumnIndexOrThrow("site_id"));
            cursor.close();
            return new AuthenticationKeyEntity(id, key,
                    UserDao.getInstance().getUser(userId),
                    SiteDao.getInstance().getSite(siteId));
        } else {
            return null;
        }
    }

    public AuthenticationKeyEntity getAuthenticationKey(int id) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT id, authKey, user_id, site_id FROM authentication_keys WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            int keyId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String key = cursor.getString(cursor.getColumnIndexOrThrow("key"));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            int siteId = cursor.getInt(cursor.getColumnIndexOrThrow("site_id"));
            cursor.close();
            return new AuthenticationKeyEntity(keyId, key,
                    UserDao.getInstance().getUser(userId),
                    SiteDao.getInstance().getSite(siteId));
        } else {
            return null;
        }
    }

    public static AuthenticationKeyDao getInstance() {
        if (instance == null) {
            instance = new AuthenticationKeyDao();
        }
        return instance;
    }

}
