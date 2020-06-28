package com.rizqanmr.bukuku.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rizqanmr.bukuku.BukuAdapter;
import com.rizqanmr.bukuku.Constant;
import com.rizqanmr.bukuku.R;


public class EditBukuActivity extends AppCompatActivity {
    private static final int NO_ID = -99;
    private static final String NO_WORD = "";

    private EditText mEditJudul, mEditPenerbit, mEditPenulis;

    // Unique tag for the intent reply.
    public static final String EXTRA_REPLY_JUDUL = "com.rizqanmr.bukuku.judul";
    public static final String EXTRA_REPLY_PENERBIT = "com.rizqanmr.bukuku.penerbit";
    public static final String EXTRA_REPLY_PENULIS = "com.rizqanmr.bukuku.penulis";

    private static final String TAG = EditBukuActivity.class.getSimpleName();

    int mId = MainActivity.BUKU_ADD;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buku);

        mEditJudul = (EditText) findViewById(R.id.edit_judul);
        mEditPenerbit = (EditText) findViewById(R.id.edit_penerbit);
        mEditPenulis = (EditText) findViewById(R.id.edit_penulis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Constant.color);

        // Get data sent from calling activity.
        Bundle extras = getIntent().getExtras();

        // If we are passed content, fill it in for the user to edit.
        if (extras != null) {

            int id = extras.getInt(BukuAdapter.EXTRA_ID, NO_ID);
            String judul = extras.getString(BukuAdapter.EXTRA_JUDUL, NO_WORD);
            String penerbit = extras.getString(BukuAdapter.EXTRA_PENERBIT, NO_WORD);
            String penulis = extras.getString(BukuAdapter.EXTRA_PENULIS, NO_WORD);
            if (id != NO_ID && !judul.equals(NO_WORD)
                    && !penerbit.equals(NO_WORD) && !penulis.equals(NO_WORD)) {
                getSupportActionBar().setTitle("Edit Buku");
                mId = id;
                mEditJudul.setText(judul);
                mEditPenerbit.setText(penerbit);
                mEditPenulis.setText(penulis);
            }
        } // Otherwise, start with empty fields.
    }
    /**
     * Click handler for the Save button.
     * Creates a new intent for the reply, adds the reply message to it as an extra,
     * sets the intent result, and closes the activity.
     */
    public void returnReply(View view) {
        String judul = ((EditText) findViewById(R.id.edit_judul)).getText().toString();
        String penerbit = ((EditText) findViewById(R.id.edit_penerbit)).getText().toString();
        String penulis = ((EditText) findViewById(R.id.edit_penulis)).getText().toString();

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY_JUDUL, judul);
        replyIntent.putExtra(EXTRA_REPLY_PENERBIT, penerbit);
        replyIntent.putExtra(EXTRA_REPLY_PENULIS, penulis);
        replyIntent.putExtra(BukuAdapter.EXTRA_ID, mId);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

}
