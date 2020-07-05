package medantechno.com.tokohh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import medantechno.com.tokohh.model.ModelLokasi;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 27/09/18.
 */

public class DbPesanan extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "db_pesanan.db";
    private static final String table       = "tbl_pesanan";

    private static final String IdMenu   = "IdMenu";
    private static final String Harga         = "Harga";
    private static final String Jumlah     = "Jumlah";
    private static final String Subtotal         = "Subtotal";
    private static final String Catatan         = "Catatan";
    private static final String IdResto         = "IdResto";
    private static final String FileGambar         = "FileGambar";
    private static final String Menu         = "Menu";



    public DbPesanan(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE  "+table+" ("+IdMenu+" INTEGER PRIMARY KEY,"+Harga+" TEXT,"+Jumlah+" TEXT,"+Subtotal+" TEXT,"+Catatan+" TEXT,"+IdResto+" TEXT,"+FileGambar+" TEXT,"+Menu+" TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+table);

        // Create tables again
        onCreate(db);
    }

    public void insert(ModelPesanan modelPesanan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IdMenu,modelPesanan.getIdMenu());
        values.put(Harga,modelPesanan.getHarga());
        values.put(Jumlah,modelPesanan.getJumlah());
        values.put(Subtotal,modelPesanan.getSubtotal());
        values.put(Catatan,modelPesanan.getCatatan());
        values.put(IdResto,modelPesanan.getIdResto());
        values.put(FileGambar,modelPesanan.getFileGambar());
        values.put(Menu,modelPesanan.getMenu());
        db.insertOrThrow(table, null, values);
        //db.insert(table,null,values);
        db.close();
    }


    public void update(ModelPesanan modelPesanan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IdMenu,modelPesanan.getIdMenu());
        values.put(Harga,modelPesanan.getHarga());
        values.put(Jumlah,modelPesanan.getJumlah());
        values.put(Subtotal,modelPesanan.getSubtotal());
        values.put(Catatan,modelPesanan.getCatatan());
        values.put(IdResto,modelPesanan.getIdResto());
        values.put(FileGambar,modelPesanan.getFileGambar());
        values.put(Menu,modelPesanan.getMenu());
        db.update(table, values,IdMenu+"=?",new String[]{String.valueOf(modelPesanan.getIdMenu())});
        //db.insert(table,null,values);
        db.close();
    }



    // Getting All
    public List<ModelPesanan> getAll(String IdResto) {
        List<ModelPesanan> semuanya = new ArrayList<ModelPesanan>();

        String selectQuery = "SELECT  * FROM " + table + " WHERE IdResto='"+IdResto+"' ORDER BY "+IdMenu+" DESC";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelPesanan modelPesanan = new ModelPesanan();

                modelPesanan.setIdMenu(cursor.getInt(0));
                modelPesanan.setHarga(cursor.getString(1));
                modelPesanan.setJumlah(cursor.getString(2));
                modelPesanan.setSubtotal(cursor.getString(3));
                modelPesanan.setCatatan(cursor.getString(4));
                modelPesanan.setIdResto(cursor.getString(5));
                modelPesanan.setFileGambar(cursor.getString(6));
                modelPesanan.setMenu(cursor.getString(7));
                semuanya.add(modelPesanan);

            } while (cursor.moveToNext());
        }

        return semuanya;
    }


    public void hapusSemua()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+table+"");

    }


}
