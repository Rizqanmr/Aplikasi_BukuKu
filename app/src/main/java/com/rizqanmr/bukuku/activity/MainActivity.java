package com.rizqanmr.bukuku.activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rizqanmr.bukuku.BukuAdapter;
import com.rizqanmr.bukuku.Constant;
import com.rizqanmr.bukuku.R;

import java.util.ArrayList;

import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_ID;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_JUDUL;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_PENERBIT;
import static com.rizqanmr.bukuku.database.BukuContract.Buku.KEY_PENULIS;
import static com.rizqanmr.bukuku.database.BukuContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int BUKU_EDIT = 1;
    public static final int BUKU_ADD = -1;

    private RecyclerView mRecyclerView;
    private BukuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create recycler view.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new BukuAdapter(this);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Constant.color);

        // Add a floating action button click handler for creating new entries.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditBukuActivity.class);
                startActivityForResult(intent, BUKU_EDIT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_search:
                onSearchRequested();
                return true;
           case R.id.setting:
               startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                return true;
            case R.id.about:
                startActivity(new Intent(getApplicationContext(),AboutActivity.class));
                return true;
            default:
                return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BUKU_EDIT) {
            if (resultCode == RESULT_OK) {
                String judul = data.getStringExtra(EditBukuActivity.EXTRA_REPLY_JUDUL);
                String penerbit = data.getStringExtra(EditBukuActivity.EXTRA_REPLY_PENERBIT);
                String penulis = data.getStringExtra(EditBukuActivity.EXTRA_REPLY_PENULIS);

                // Update the database.
                if (judul.length() != 0) {
                    ContentValues values = new ContentValues();
                    values.put(KEY_JUDUL, judul);
                    values.put(KEY_PENERBIT, penerbit);
                    values.put(KEY_PENULIS, penulis);
                    int id = data.getIntExtra(BukuAdapter.EXTRA_ID, -99);

                    if (id == BUKU_ADD) {
                        getContentResolver().insert(CONTENT_URI, values);
                    } else if (id >= 0) {
                        String[] selectionArgs = {Integer.toString(id)};
                        getContentResolver().update(CONTENT_URI, values, KEY_ID, selectionArgs);
                    }
                    // Update the UI.
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(
                            getApplicationContext(), R.string.empty_word_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Cursor> newList = new ArrayList<>();
        return true;
    }
}