package com.rizqanmr.bukuku.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.rizqanmr.bukuku.Constant;
import com.rizqanmr.bukuku.R;

public class DetailBukuActivity extends AppCompatActivity {
    public TextView tvJudul, tvPenerbit, tvPenulis;
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_JUDUL = "JUDUL";
    public static final String EXTRA_PENERBIT = "PENERBIT";
    public static final String EXTRA_PENULIS = "PENULIS";
    public static final String EXTRA_POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buku);

        tvJudul = (TextView) findViewById(R.id.tv_judul);
        tvPenerbit = (TextView) findViewById(R.id.tv_penerbit);
        tvPenulis = (TextView) findViewById(R.id.tv_penulis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Constant.color);

        Intent terimaData = getIntent();
        String nm = terimaData.getStringExtra(EXTRA_JUDUL);
        getSupportActionBar().setTitle(nm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tvJudul.setText(terimaData.getStringExtra(EXTRA_JUDUL));
        tvPenerbit.setText(terimaData.getStringExtra(EXTRA_PENERBIT));
        tvPenulis.setText(terimaData.getStringExtra(EXTRA_PENULIS));

    }
}