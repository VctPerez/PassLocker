package es.uma.passlocker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "passlocker.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper databaseHelper;
    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL)";

        String createSiteTable = "CREATE TABLE sites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)";

        String createAuthenticationKeyTable = "CREATE TABLE authenticationKey (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "authKey VARCHAR(255), " +
                "user_id INTEGER, " +
                "site_id INTEGER, " +
                "PRIMARY KEY (id, user_id, site_id), " +
                "FOREIGN KEY (user_id) REFERENCES users(id), " +
                "FOREIGN KEY (site_id) REFERENCES sites(id))";

        db.execSQL(createUserTable);
        db.execSQL(createSiteTable);
        db.execSQL(createAuthenticationKeyTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS sites");
        onCreate(db);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    public static DatabaseHelper getInstance() {
        return databaseHelper;
    }
}
