package medantechno.com.tokohh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import medantechno.com.tokohh.R;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.database.DbPesanan;
import medantechno.com.tokohh.model.ModelMenu;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterBeli extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelPesanan> modelMenuList;


    Button tambah,kurang,beli;


    int total;


    public AdapterBeli(Activity activity, List<ModelPesanan> modelEndeList){
        this.activity=activity;
        this.modelMenuList=modelEndeList;

    }


    @Override
    public int getCount() {
        return modelMenuList.size();
    }

    @Override
    public Object getItem(int location) {
        return modelMenuList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_pesanan, null);


        TextView Menu  = (TextView) convertView.findViewById(R.id.Menu);
        TextView Harga = (TextView) convertView.findViewById(R.id.Harga);
        ImageView  imageMenu = (ImageView) convertView.findViewById(R.id.imageMenu);
        TextView Catatan = (TextView) convertView.findViewById(R.id.Catatan);
        TextView subtotal = (TextView) convertView.findViewById(R.id.subtotal);

        final ModelPesanan mMenu = modelMenuList.get(position);




        final TextView jumlah = (TextView) convertView.findViewById(R.id.jumlah);
        jumlah.setEnabled(false);
        try {
            jumlah.setText(mMenu.getJumlah());
            Catatan.setText(mMenu.getCatatan());
        }catch (Exception e){
            System.out.println(e);
        }


        Context context = parent.getContext();
        try {
            Picasso.with(context).load(Config.StringUrl.base_gambar_menu +mMenu.getFileGambar()).into(imageMenu);
        }catch (Exception e)
        {
            Log.d("load gambar",Config.StringUrl.base_gambar_menu +mMenu.getFileGambar());
        }

        Menu.setText(mMenu.getMenu());
        subtotal.setText(Rupiah(Double.valueOf(mMenu.getSubtotal())));
        Harga.setText(Rupiah(Double.valueOf(mMenu.getHarga())));

        System.out.println(mMenu.getIdMenu()+"-"+mMenu.getMenu()+"-"+mMenu.getFileGambar());
        return convertView;

    }



    private void simpanKeDb(ModelPesanan mp,Context context)
    {
        try {
            new DbPesanan(context).insert(mp);
            System.out.println("insert_db");
        }catch (Exception e)
        {
            new DbPesanan(context).update(mp);
            System.out.println("update_db"+e);
        }
    }


    public String Rupiah(Double uang)
    {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(uang);
    }
}
