package medantechno.com.tokohh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import medantechno.com.tokohh.R;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.model.ModelDriver;
import medantechno.com.tokohh.model.ModelMenu;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterDetailPesanan extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelPesanan> modelMenuList;


    public AdapterDetailPesanan(Activity activity, List<ModelPesanan> modelEndeList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_detail_pesanan, null);


        TextView Nama  = (TextView) convertView.findViewById(R.id.Nama);
        TextView harga = (TextView) convertView.findViewById(R.id.harga);
        TextView subtotal = (TextView) convertView.findViewById(R.id.subtotal);
        TextView jumlah = (TextView) convertView.findViewById(R.id.jumlah);
        ImageView  imageMenu = (ImageView) convertView.findViewById(R.id.Foto);


        final ModelPesanan md = modelMenuList.get(position);
        Context context = parent.getContext();
        try {
            Picasso.with(context).load(Config.StringUrl.base_gambar_menu +md.getFileGambar()).into(imageMenu);
        }catch (Exception e)
        {
            Log.d("load gambar",Config.StringUrl.base_gambar_menu +md.getFileGambar());
        }


        Nama.setText(md.getMenu());
        harga.setText("Harga : "+Rupiah(md.getHarga()));
        subtotal.setText("Subtotal : "+Rupiah(md.getSubtotal()));
        jumlah.setText(md.getJumlah());


        return convertView;

    }



    public String Rupiah(String a)
    {
        double uang = Double.parseDouble(a);
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(uang);
    }

}
