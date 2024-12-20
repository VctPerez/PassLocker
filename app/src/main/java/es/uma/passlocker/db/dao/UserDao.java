package es.uma.passlocker.db.dao;

import es.uma.passlocker.db.DatabaseHelper;
import es.uma.passlocker.db.entities.UserEntity;

import android.content.Context;
import android.database.Cursor;

public class UserDao {
    private DatabaseHelper databaseHelper;

    public UserDao(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void insertUser(String username) {
        databaseHelper.getWritableDatabase().execSQL("INSERT INTO users (username) VALUES ('" + username + "')");
    }

    public void deleteUser(String username) {
        databaseHelper.getWritableDatabase().execSQL("DELETE FROM users WHERE username = '" + username + "'");
    }

    public void updateUser(String oldUsername, String newUsername) {
        databaseHelper.getWritableDatabase().execSQL("UPDATE users SET username = '" + newUsername + "' WHERE username = '" + oldUsername + "'");
    }

    public UserEntity getUser(String username) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT id, username FROM users WHERE username = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String user = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            cursor.close();
            return new UserEntity(id, user);
        } else {
            return null;
        }
    }
}
