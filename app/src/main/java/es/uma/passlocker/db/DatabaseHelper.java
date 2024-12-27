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
        String userScript =
                "CREATE TABLE user (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "    username TEXT UNIQUE NOT NULL," +
                "    password TEXT NOT NULL" +
                ");";
        String passwordScript =
                "CREATE TABLE password_info (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "    user_id INTEGER NOT NULL," +
                        "    site_name TEXT NOT NULL," +
                        "    site_url TEXT," +
                        "    notes TEXT," +
                        "    password TEXT NOT NULL," +
                        "    FOREIGN KEY (user_id) REFERENCES user(id)" +
                        ");";
        
        db.execSQL(userScript);
        db.execSQL(passwordScript);
        System.out.println("Database created");
        System.out.println(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS password_info");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS password_info");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        System.out.println("Database opened");
        super.onOpen(db);
    }

    public static void init(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
    }

    public static DatabaseHelper getInstance() {
        return databaseHelper;
    }
}
