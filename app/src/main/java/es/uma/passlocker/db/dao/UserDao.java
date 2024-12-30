package es.uma.passlocker.db.dao;

import es.uma.passlocker.db.DatabaseHelper;
import es.uma.passlocker.db.entities.UserEntity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.Arrays;

public class UserDao {
    private DatabaseHelper databaseHelper;
    private static UserDao userDao;

    private UserDao() {
        this.databaseHelper = DatabaseHelper.getInstance();
    }

    public void insertUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.insert("user", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.delete("user", "username LIKE ?", new String[]{username});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(String oldUsername, String newUsername) {
        ContentValues values = new ContentValues();
        values.put("username", newUsername);
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.update("user", values, "username LIKE ?", new String[]{oldUsername});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserEntity getUser(String username) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM user WHERE username = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            System.out.println("Columnas: " +Arrays.toString(cursor.getColumnNames()));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String user = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            cursor.close();
            return new UserEntity(id, user, password);
        } else {
            return null;
        }
    }

    public UserEntity getUser(int id) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM user WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            cursor.close();
            return new UserEntity(userId, username, password);
        } else {
            return null;
        }
    }

    public static UserDao getInstance() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }
}
