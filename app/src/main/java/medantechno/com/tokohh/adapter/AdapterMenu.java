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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import medantechno.com.tokohh.DetailActivity;
import medantechno.com.tokohh.R;
import medantechno.com.tokohh.RestoActivity;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.model.ModelMenu;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterMenu extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelMenu> modelMenuList;


    public AdapterMenu(Activity activity, List<ModelMenu> modelEndeList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_menu, null);


        TextView Menu  = (TextView) convertView.findViewById(R.id.Menu);
        TextView Harga = (TextView) convertView.findViewById(R.id.Harga);
        TextView Nama = (TextView) convertView.findViewById(R.id.Nama);
        TextView IsBuka = (TextView) convertView.findViewById(R.id.IsBuka);
        ImageView  imageMenu = (ImageView) convertView.findViewById(R.id.imageMenu);


        final ModelMenu mMenu = modelMenuList.get(position);
        Context context = parent.getContext();
        try {
            Picasso.with(context).load(Config.StringUrl.base_gambar_menu +mMenu.getFileGambar()).into(imageMenu);
        }catch (Exception e)
        {
            Log.d("load gambar",Config.StringUrl.base_gambar_menu +mMenu.getFileGambar());
        }

        Menu.setText(mMenu.getMenu());
        Harga.setText("Rp."+mMenu.getHarga());
        Nama.setText(mMenu.getNama()+" ("+mMenu.getAlamat()+")");
        if(mMenu.getIsTersedia().equals("1"))
        {
            IsBuka.setText("Tersedia");
            IsBuka.setTextColor(Color.parseColor("#009688"));
        }else{
            IsBuka.setText("Habis");
            IsBuka.setTextColor(Color.parseColor("#ff0000"));
        }


        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ke detail
                Intent i = new Intent(activity, RestoActivity.class);
                i.putExtra("IdMenu",mMenu.getIdMenu());
                i.putExtra("IdResto",mMenu.getIdResto());
                i.putExtra("Menu",mMenu.getMenu());
                i.putExtra("Deskripsi",mMenu.getDeskripsi());
                i.putExtra("Harga",mMenu.getHarga());
                i.putExtra("IsTersedia",mMenu.getIsTersedia());
                i.putExtra("IsAktif",mMenu.getIsAktif());
                i.putExtra("FileGambar",mMenu.getFileGambar());
                i.putExtra("IdPemilik",mMenu.getIdPemilik());
                i.putExtra("Nama",mMenu.getNama());
                i.putExtra("Alamat",mMenu.getAlamat());
                i.putExtra("Lat",mMenu.getLat());
                i.putExtra("Long",mMenu.getLong());
                i.putExtra("IsHalal",mMenu.getIsHalal());
                i.putExtra("IsBuka",mMenu.getIsBuka());
                activity.startActivity(i);
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //ke detail
                Intent i = new Intent(activity, RestoActivity.class);
                i.putExtra("IdMenu",mMenu.getIdMenu());
                i.putExtra("IdResto",mMenu.getIdResto());
                i.putExtra("Menu",mMenu.getMenu());
                i.putExtra("Deskripsi",mMenu.getDeskripsi());
                i.putExtra("Harga",mMenu.getHarga());
                i.putExtra("IsTersedia",mMenu.getIsTersedia());
                i.putExtra("IsAktif",mMenu.getIsAktif());
                i.putExtra("FileGambar",mMenu.getFileGambar());
                i.putExtra("IdPemilik",mMenu.getIdPemilik());
                i.putExtra("Nama",mMenu.getNama());
                i.putExtra("Alamat",mMenu.getAlamat());
                i.putExtra("Lat",mMenu.getLat());
                i.putExtra("Long",mMenu.getLong());
                i.putExtra("IsHalal",mMenu.getIsHalal());
                i.putExtra("IsBuka",mMenu.getIsBuka());


                activity.startActivity(i);
            }
        });

        return convertView;

    }
}
