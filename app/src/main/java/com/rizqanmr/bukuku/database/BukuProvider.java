package com.rizqanmr.bukuku.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import static com.rizqanmr.bukuku.database.BukuContract.ALL_ITEMS;
import static com.rizqanmr.bukuku.database.BukuContract.AUTHORITY;
import static com.rizqanmr.bukuku.database.BukuContract.CONTENT_PATH;
import static com.rizqanmr.bukuku.database.BukuContract.CONTENT_URI;
import static com.rizqanmr.bukuku.database.BukuContract.COUNT;
import static com.rizqanmr.bukuku.database.BukuContract.MULTIPLE_RECORDS_MIME_TYPE;
import static com.rizqanmr.bukuku.database.BukuContract.SINGLE_RECORD_MIME_TYPE;
import static java.lang.Integer.parseInt;

public class BukuProvider extends ContentProvider {

    private static final String TAG = BukuProvider.class.getSimpleName();

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Moved all the interaction with the OpenHelper from MainActivity into the ContentProvider.
    // The MainActivity does not know anything about the backend and talks to the resolver instead.
    private BukuOpenHelper mDB;

    private static final int URI_ALL_ITEMS_CODE = 10;
    private static final int URI_ONE_ITEM_CODE = 20;
    private static final int URI_COUNT_CODE = 30;

    @Override
    public boolean onCreate() {
        // Database interface object
        mDB = new BukuOpenHelper(getContext());
        initializeUriMatching();
        return true;
    }

    /**
     * Defines the accepted Uri schemes for this content provider.
     * Calls addURI() for all of the content URI patterns that the provide should recognize.
     */
    private void initializeUriMatching() {

        // Matches a URI that is just the authority + the path, triggering the return of all words.
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH, URI_ALL_ITEMS_CODE);

        // Matches a URI that references one word in the list by its index.
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", URI_ONE_ITEM_CODE);

        // Matches a URI that returns the number of rows in the table.
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/" + COUNT, URI_COUNT_CODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor cursor = null;

        // Determine integer code from the URI matcher and switch on it.
        switch (sUriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                cursor =  mDB.query(ALL_ITEMS);
                Log.d(TAG, "case all items " + cursor);
                break;
            case URI_ONE_ITEM_CODE:
                cursor =  mDB.query(parseInt(uri.getLastPathSegment()));
                Log.d(TAG, "case one item " + cursor);
                break;
            case URI_COUNT_CODE:
                cursor = mDB.count();
                Log.d(TAG, "case count " + cursor);
                break;
            case UriMatcher.NO_MATCH:
                // You should do some error handling here.
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME: " + uri);
                break;
            default:
                // You should do some error handling here.
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED: "  + uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                return MULTIPLE_RECORDS_MIME_TYPE;
            case URI_ONE_ITEM_CODE:
                return SINGLE_RECORD_MIME_TYPE;
            default:
                return null;
        }
    }

    /**
     * Inserts one row.
     *
     * @param uri Uri for insertion.
     * @param values Container for Column/Row key/value pairs.
     * @return URI for the newly created entry.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mDB.insert(values);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    /**
     * Deletes records(s) specified by selectionArgs.
     *
     * @param uri URI for deletion.
     * @param selection Where clause.
     * @param selectionArgs Where clause arguments.
     * @return Number of records affected.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mDB.delete(parseInt(selectionArgs[0]));
    }

    /**
     * Updates records(s) specified by selection/selectionArgs combo.
     *
     * @param uri URI for update.
     * @param values Container for Column/Row key/value pairs.
     * @param selection Where clause.
     * @param selectionArgs Where clause arguments.
     * @return Number of records affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDB.update(parseInt(selectionArgs[0]), values.getAsString("judul"),
                values.getAsString("penerbit"), values.getAsString("penulis"));
    }
}
