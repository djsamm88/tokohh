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
import medantechno.com.tokohh.RestoActivity;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.database.DbPesanan;
import medantechno.com.tokohh.model.ModelMenu;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterMenuResto extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelMenu> modelMenuList;


    Button tambah,kurang,beli;


    int total;


    public AdapterMenuResto(Activity activity, List<ModelMenu> modelEndeList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_menu_resto, null);


        TextView Menu  = (TextView) convertView.findViewById(R.id.Menu);
        TextView Harga = (TextView) convertView.findViewById(R.id.Harga);
        TextView IsBuka = (TextView) convertView.findViewById(R.id.IsBuka);
        ImageView  imageMenu = (ImageView) convertView.findViewById(R.id.imageMenu);
        final EditText Catatan = (EditText) convertView.findViewById(R.id.Catatan);

        final ModelMenu mMenu = modelMenuList.get(position);


        tambah = (Button) convertView.findViewById(R.id.tambah);
        kurang = (Button) convertView.findViewById(R.id.kurang);
        //beli = (Button) convertView.findViewById(R.id.beli);

        final EditText jumlah = (EditText) convertView.findViewById(R.id.jumlah);
        jumlah.setEnabled(false);
        final EditText hargaKali = (EditText) convertView.findViewById(R.id.hargaKali);
        hargaKali.setEnabled(false);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int harganya = Integer.parseInt(mMenu.getHarga());
                    int jum;
                    try {
                        jum = Integer.parseInt(jumlah.getText().toString());

                    }catch (Exception x)
                    {
                        jum =0;

                    }
                    jum += 1;


                    String hasil = String.valueOf(jum);
                    jumlah.setText(hasil);
                    int hargasem = harganya*jum;
                    hargaKali.setText(Rupiah(Double.valueOf(hargasem)));

                    total+=harganya;
                    System.out.println("catatan="+Catatan.getText().toString());

                    ModelPesanan mp = new ModelPesanan();
                    mp.setIdMenu(Integer.parseInt(mMenu.getIdMenu()));
                    mp.setHarga(mMenu.getHarga());
                    mp.setJumlah(String.valueOf(jum));
                    mp.setCatatan(Catatan.getText().toString());
                    mp.setSubtotal(String.valueOf(hargasem));
                    mp.setIdResto(mMenu.getIdResto());
                    mp.setFileGambar(mMenu.getFileGambar());
                    mp.setMenu(mMenu.getMenu());
                    simpanKeDb(mp,view.getContext());

                    System.out.println("menu="+mMenu.getMenu()+"-gambar"+mMenu.getFileGambar());

                    Intent i = new Intent("TOTAL_DAPAT");
                    i.putExtra("total",total);
                    i.putExtra("IdResto",mMenu.getIdResto());
                    Context c = view.getContext();
                    c.sendBroadcast(i);

                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });



        kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    int harga_satuan = Integer.parseInt(mMenu.getHarga());
                    int jum;
                    try {
                        jum = Integer.parseInt(jumlah.getText().toString());
                    }catch (Exception x)
                    {
                        jum =0;
                    }
                    jum -=1;


                    if(jum<0)
                    {
                        total-=0;
                    }else{
                        total-=harga_satuan;
                    }


                    if(jum<0 || jum==0)
                    {
                        jum=0;

                    }


                    String hasil = String.valueOf(jum);
                    int hargasem = harga_satuan*jum;

                    hargaKali.setText(Rupiah(Double.valueOf(hargasem)));
                    jumlah.setText(hasil);

                    System.out.println("total="+total);


                    ModelPesanan mp = new ModelPesanan();
                    mp.setIdMenu(Integer.parseInt(mMenu.getIdMenu()));
                    mp.setHarga(mMenu.getHarga());
                    mp.setJumlah(String.valueOf(jum));
                    mp.setCatatan(Catatan.getText().toString());
                    mp.setSubtotal(String.valueOf(hargasem));
                    mp.setIdResto(mMenu.getIdResto());
                    mp.setFileGambar(mMenu.getFileGambar());
                    mp.setMenu(mMenu.getMenu());
                    simpanKeDb(mp,view.getContext());

                    Intent i = new Intent("TOTAL_DAPAT");
                    i.putExtra("total",total);
                    i.putExtra("IdResto",mMenu.getIdResto());
                    Context c = view.getContext();
                    c.sendBroadcast(i);


                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });



        /*

        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int jum,hargasem;
                try {
                    jum = Integer.parseInt(jumlah.getText().toString());
                }catch (Exception x)
                {
                    jum =0;
                }

                hargasem = Integer.parseInt(hargaKali.getText().toString());


                System.out.println("hargasem:"+hargasem);


                Intent i = new Intent("TOTAL_DAPAT");
                i.putExtra("total",total);
                i.putExtra("Harga",mMenu.getHarga());
                i.putExtra("Jumlah",jum);
                i.putExtra("IdMenu",mMenu.getIdMenu());
                i.putExtra("Subtotal",hargasem);
                i.putExtra("Catatan",Catatan.getText().toString());
                Context c = view.getContext();
                c.sendBroadcast(i);

            }
        });
         */

        Context context = parent.getContext();
        try {
            Picasso.with(context).load(Config.StringUrl.base_gambar_menu +mMenu.getFileGambar()).into(imageMenu);
        }catch (Exception e)
        {
            Log.d("load gambar",Config.StringUrl.base_gambar_menu +mMenu.getFileGambar());
        }

        Menu.setText(mMenu.getMenu());
        Harga.setText(Rupiah(Double.valueOf(mMenu.getHarga())));

        if(mMenu.getIsTersedia().equals("1"))
        {
            IsBuka.setText("Tersedia");
            IsBuka.setTextColor(Color.parseColor("#009688"));
        }else{
            IsBuka.setText("Habis");
            IsBuka.setTextColor(Color.parseColor("#ff0000"));
        }

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
