package com.rizqanmr.bukuku;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rizqanmr.bukuku.activity.DetailBukuActivity;
import com.rizqanmr.bukuku.activity.EditBukuActivity;
import com.rizqanmr.bukuku.activity.MainActivity;
import com.rizqanmr.bukuku.database.BukuContract;

import static com.rizqanmr.bukuku.database.BukuContract.CONTENT_PATH;
import static com.rizqanmr.bukuku.database.BukuContract.CONTENT_URI;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {

    class BukuViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvJudul, tvPenerbit, tvPenulis;
        ImageButton btnEdit, btnHapus;
        RelativeLayout itemLayout;

        public BukuViewHolder(View itemView) {
            super(itemView);
            tvJudul = (TextView) itemView.findViewById(R.id.judul);
            tvPenerbit = (TextView) itemView.findViewById(R.id.penerbit);
            tvPenulis = (TextView) itemView.findViewById(R.id.penulis);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btn_edit);
            btnHapus = (ImageButton) itemView.findViewById(R.id.btn_hapus);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
        }
    }

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_JUDUL = "JUDUL";
    public static final String EXTRA_PENERBIT = "PENERBIT";
    public static final String EXTRA_PENULIS = "PENULIS";
    public static final String EXTRA_POSITION = "POSITION";

    private static final String TAG = BukuAdapter.class.getSimpleName();

    // Query parameters are very similar to SQL queries.
    private String queryUri = CONTENT_URI.toString(); //base uri
    private static final String[] projection = new String[] {CONTENT_PATH}; //table
    private String selectionClause = null;
    private String selectionArgs[] = null;
    private String sortOrder = "ASC";

    private final LayoutInflater mInflater;
    private Context mContext;

    public BukuAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public BukuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_buku, parent, false);
        return new BukuViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(BukuViewHolder holder, int position) {
        // Create a reference to the view holder for the click listener
        // Must be final for use in callback
        final BukuViewHolder h = holder;

        String judul = "";
        String penerbit = "";
        String penulis = "";
        int id = -1;

        // Position != id !!!
        // position == index == row; can't get nth row, so have to get all and then pick row
        Cursor cursor =
                mContext.getContentResolver().query(Uri.parse(
                        queryUri), null, null, null, sortOrder);
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                int indexJudul = cursor.getColumnIndex(BukuContract.Buku.KEY_JUDUL);
                judul = cursor.getString(indexJudul);
                holder.tvJudul.setText(judul);
                int indexPenerbit = cursor.getColumnIndex(BukuContract.Buku.KEY_PENERBIT);
                penerbit = cursor.getString(indexPenerbit);
                holder.tvPenerbit.setText(penerbit);
                int indexPenulis = cursor.getColumnIndex(BukuContract.Buku.KEY_PENULIS);
                penulis = cursor.getString(indexPenulis);
                holder.tvPenulis.setText(penulis);
                int indexId = cursor.getColumnIndex(BukuContract.Buku.KEY_ID);
                id = cursor.getInt(indexId);
            } else {
                holder.tvJudul.setText(R.string.error_no_word);
            }
            cursor.close();
        } else {
            Log.e (TAG, "onBindViewHolder: Cursor is null.");
        }

        // Attach a click listener to the DELETE button
        holder.btnHapus.setOnClickListener(new MyButtonOnClickListener(id,judul,penerbit,penulis) {
            @Override
            public void onClick(View view) {
                selectionArgs = new String[]{Integer.toString(id)};
                int deleted = mContext.getContentResolver().delete(CONTENT_URI, CONTENT_PATH,
                        selectionArgs);
                if (deleted > 0) {
                    // Need both calls
                    notifyItemRemoved(h.getAdapterPosition());
                    notifyItemRangeChanged(h.getAdapterPosition(), getItemCount());
                    Toast.makeText(mContext, "Data terhapus",Toast.LENGTH_LONG).show();
                } else {
                    Log.d (TAG, mContext.getString(R.string.not_deleted) + deleted);
                    Toast.makeText(mContext, "Gagal menghapus data",Toast.LENGTH_LONG).show();
                }

            }
        });

        // Attach a click listener to the EDIT button
        holder.btnEdit.setOnClickListener(new MyButtonOnClickListener(id,judul,penerbit,penulis) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditBukuActivity.class);

                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                intent.putExtra(EXTRA_JUDUL, judul);
                intent.putExtra(EXTRA_PENERBIT, penerbit);
                intent.putExtra(EXTRA_PENULIS, penulis);

                ((Activity) mContext).startActivityForResult(intent, MainActivity.BUKU_EDIT);
            }
        });

        // Pindah ke detail activity
        holder.itemLayout.setOnClickListener(new MyButtonOnClickListener(id,judul,penerbit,penulis) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailBukuActivity.class);

                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                intent.putExtra(EXTRA_JUDUL, judul);
                intent.putExtra(EXTRA_PENERBIT, penerbit);
                intent.putExtra(EXTRA_PENULIS, penulis);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Cursor cursor =
                mContext.getContentResolver().query(
                        BukuContract.ROW_COUNT_URI, new String[] {"count(*) AS count"},
                        selectionClause, selectionArgs, sortOrder);
        try {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        catch (Exception e){
            Log.d(TAG, "EXCEPTION getItemCount: " + e);
            return -1;
        }
    }


}
