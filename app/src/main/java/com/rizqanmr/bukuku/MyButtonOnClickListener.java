package com.rizqanmr.bukuku;

import android.view.View;

public class MyButtonOnClickListener implements View.OnClickListener {
    private static final String TAG = View.OnClickListener.class.getSimpleName();

    int id;
    String judul, penerbit, penulis;

    public MyButtonOnClickListener(int id, String judul, String penerbit, String penulis) {
        this.id = id;
        this.judul = judul;
        this.penerbit = penerbit;
        this.penulis = penulis;
    }

    @Override
    public void onClick(View view) {
        //implementasi di BukuAdapter
    }
}
