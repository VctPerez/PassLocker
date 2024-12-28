package es.uma.passlocker.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import es.uma.passlocker.db.DatabaseHelper;
import es.uma.passlocker.db.entities.PasswordInfoEntity;

public class PasswordInfoDao {
    private DatabaseHelper databaseHelper;
    private static PasswordInfoDao instance;


    private PasswordInfoDao() {
        databaseHelper = DatabaseHelper.getInstance();
    }

    public void insertPasswordInfo(Integer userId, String siteName, String siteUrl, String notes, String password) {
        String query = "INSERT INTO password_info (user_id, site_name, site_url, notes, password) VALUES (?, ?, ?, ?, ?)";
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(query);
            statement.bindLong(1, userId);
            statement.bindString(2, siteName);
            statement.bindString(3, siteUrl);
            statement.bindString(4, notes);
            statement.bindString(5, password);
            statement.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePasswordInfo(Integer passwordId) {
        String query = "DELETE FROM password_info WHERE id = ?";
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(query);
            statement.bindLong(1, passwordId);
            statement.executeUpdateDelete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePasswordInfo(Integer id, String siteName, String siteUrl, String notes, String password) {
        String query = "UPDATE password_info SET site_name = ?, site_url = ?, notes = ?, password = ? WHERE id = ?";
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(query);
            statement.bindString(1, siteName);
            statement.bindString(2, siteUrl);
            statement.bindString(3, notes);
            statement.bindString(4, password);
            statement.bindLong(5, id);

            statement.executeUpdateDelete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PasswordInfoEntity getPasswordInfo(Integer id) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT id, user_id, site_name, site_url, notes, password FROM password_info WHERE id = ?", new String[]{id.toString()});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String siteName = cursor.getString(cursor.getColumnIndexOrThrow("site_name"));
            String siteUrl = cursor.getString(cursor.getColumnIndexOrThrow("site_url"));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            cursor.close();
            return new PasswordInfoEntity(id, UserDao.getInstance().getUser(userId), siteName, siteUrl, notes, password);
        } else {
            return null;
        }
    }

    public PasswordInfoEntity getPasswordInfo(String name) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT id, user_id, site_name, site_url, notes, password FROM password_info WHERE site_name = ?", new String[]{name});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String siteName = cursor.getString(cursor.getColumnIndexOrThrow("site_name"));
            String siteUrl = cursor.getString(cursor.getColumnIndexOrThrow("site_url"));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            cursor.close();
            return new PasswordInfoEntity(id, UserDao.getInstance().getUser(userId), siteName, siteUrl, notes, password);
        } else {
            return null;
        }
    }

    public List<String> getUserPasswords(String user_id) {
        List<String> siteNames = new ArrayList<>();
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT site_name FROM password_info WHERE user_id = ?", new String[]{user_id});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String siteName = cursor.getString(cursor.getColumnIndexOrThrow("site_name"));
                siteNames.add(siteName);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return siteNames;
    }


    public static PasswordInfoDao getInstance() {
        if (instance == null) {
            instance = new PasswordInfoDao();
        }
        return instance;
    }

}
