package com.rizqanmr.bukuku.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class BukuContract {
    private static final String TAG = BukuContract.class.getSimpleName();

    // Prevents class from being instantiated.
    private BukuContract() {}

    public static final int ALL_ITEMS = -4;
    public static final String COUNT = "count";


    public static final String AUTHORITY =
            "com.rizqanmr.bukuku.provider";

    // Only one public table.
    public static final String CONTENT_PATH = "buku";

    // Content URI for this table. Returns all items.
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);

    // URI to get the number of entries.
    public static final Uri ROW_COUNT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH + "/" + COUNT);

    static final String SINGLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.rizqanmr.bukuku.provider.buku";
    static final String MULTIPLE_RECORDS_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.rizqanmr.bukuku.provider.buku";

    /*
     * Constants for the database are moved out of WordListOpenHelper into the contract.
     * A common way to organize a contract class is to put definitions that are global to your
     * database in the root level of the class. Then create an inner class for each table
     * that enumerates its columns.
     */


    public static final String DATABASE_NAME = "tb_buku";

    /**
     *  Inner class that defines the table contents.
     *
     * By implementing the BaseColumns interface, your inner class can inherit a primary
     * key field called _ID that some Android classes such as cursor adaptors will expect it to
     * have. It's not required, but this can help your database work harmoniously with the
     * Android framework.
     */
    public static abstract class Buku implements BaseColumns {

        public static final String BUKU_TABLE = "buku_entries";

        // Column names
        public static final String KEY_ID = "_id";
        public static final String KEY_JUDUL = "judul";
        public static final String KEY_PENERBIT = "penerbit";
        public static final String KEY_PENULIS = "penulis";
    }
}
