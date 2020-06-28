package com.rizqanmr.bukuku.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.rizqanmr.bukuku.database.BukuContract.ALL_ITEMS;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.BUKU_TABLE;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_ID;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_JUDUL;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_PENERBIT;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_PENULIS;
import static com.rizqanmr.bukuku.database.BukuContract.DATABASE_NAME;

public class BukuOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = BukuOpenHelper.class.getSimpleName();

    // Has to be 1 first time or app will crash.
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase mWritableDB;
    SQLiteDatabase mReadableDB;
    ContentValues mValues = new ContentValues();

    // Build the SQL query that creates the table.
    private static final String BUKU_TABLE_CREATE =
            "CREATE TABLE " + BUKU_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " + // will auto-increment if no value passed
                    KEY_JUDUL + " TEXT, " +
                    KEY_PENERBIT + " TEXT, " +
                    KEY_PENULIS + " TEXT );";

    public BukuOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BUKU_TABLE_CREATE);
        //fillDatabaseWithData(db);
    }

    /**
     * Adds the initial data set to the database.
     * According to the docs, onCreate for the open helper does not run on the UI thread.
     *
     * @param db Database to fill with data since the member variables are not initialized yet.
     */
    public void fillDatabaseWithData(SQLiteDatabase db) {

        String[] buku = {"Android", "Laravel", "Code Igniter"};

        // Create a container for the data.
        ContentValues values = new ContentValues();

        for (int i=0; i < buku.length;i++) {
            // Put column/value pairs into the container, overriding existing values.
            values.put(KEY_JUDUL, buku[i]);
            db.insert(BUKU_TABLE_CREATE, null, values);
        }
    }

    /**
     * Called when a database needs to be upgraded. The most basic version of this method drops
     * the tables, and then recreates them. All data is lost, which is why for a production app,
     * you want to back up your data first. If this method fails, changes are rolled back.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(BukuOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + BUKU_TABLE);
        onCreate(db);
    }

    /**
     * Queries the database for an entry at a given position.
     *
     * @param position The Nth row in the table.
     * @return a WordItem with the requested database entry.
     */
    public Cursor query(int position) {
        String query;
        if (position != ALL_ITEMS) {
            position++; // Because database starts counting at 1.
            query = "SELECT " + KEY_ID + "," + KEY_JUDUL + " FROM " + BUKU_TABLE +
                    " WHERE " + KEY_ID + "=" + position + ";";
        } else {
            query = "SELECT  * FROM " + BUKU_TABLE + " ORDER BY " + KEY_JUDUL + " ASC ";
        }

        Cursor cursor = null;
        try {
            if (mReadableDB == null) {
                mReadableDB = this.getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e);
        } finally {
            return cursor;
        }
    }

    /**
     * Gets the number of rows in the word list table.
     *
     * @return The number of entries in WORD_LIST_TABLE.
     */
    public Cursor count() {
        MatrixCursor cursor = new MatrixCursor(new String[]{BukuContract.CONTENT_PATH});
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            // queryNumEntries returns a long, but we need to pass up an int.
            // With the small number of entries, no worries about losing precision.
            int count = (int) DatabaseUtils.queryNumEntries(mReadableDB, BUKU_TABLE);
            cursor.addRow(new Object[]{count});
        } catch (Exception e) {
            Log.d(TAG, "COUNT EXCEPTION " + e);
        }
        return cursor;
    }

    /**
     * Adds a single word row/entry to the database.
     *
     * @param  values Container for key/value columns/values.
     * @return The id of the inserted word.
     */
    public long insert(ContentValues values){
        long added = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            added = mWritableDB.insert(BUKU_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION " + e);
        }
        return added;
    }

    /**
     * Updates  word with the supplied id to the supplied value.
     *
     * @param id Id of the word to update.
     * @param judul The new value of the word.
     * @return the number of rows affected.
     */
    public int update(int id, String judul, String penerbit, String penulis) {
        int updated = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_JUDUL, judul);
            values.put(KEY_PENERBIT, penerbit);
            values.put(KEY_PENULIS, penulis);

            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            updated = mWritableDB.update(BUKU_TABLE, //table to change
                    values, // new values to insert
                    KEY_ID + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(id)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d (TAG, "UPDATE EXCEPTION " + e);
        }
        return updated;
    }

    /**
     * Deletes one entry identified by its id.
     *
     * @param id ID of the entry to delete.
     * @return The number of rows deleted. Since we are deleting by id, this should be 0 or 1.
     */
    public int delete(int id) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = this.getWritableDatabase();
            }
            deleted = mWritableDB.delete(BUKU_TABLE,
                    KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION " + e);
        }
        return deleted;
    }

}
