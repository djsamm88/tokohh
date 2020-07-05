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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import medantechno.com.tokohh.BuildConfig;
import medantechno.com.tokohh.R;
import medantechno.com.tokohh.RestoActivity;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.model.ModelDriver;
import medantechno.com.tokohh.model.ModelMenu;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterDriver extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelDriver> modelMenuList;


    public AdapterDriver(Activity activity, List<ModelDriver> modelEndeList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_driver, null);


        TextView Nama  = (TextView) convertView.findViewById(R.id.Nama);
        TextView no_hp = (TextView) convertView.findViewById(R.id.no_hp);
        TextView NoPlat = (TextView) convertView.findViewById(R.id.NoPlat);
        ImageView  imageMenu = (ImageView) convertView.findViewById(R.id.Foto);
        Button btnPilih = (Button) convertView.findViewById(R.id.btnPilih);


        final ModelDriver md = modelMenuList.get(position);
        Context context = parent.getContext();
        try {
            Picasso.with(context).load(Config.StringUrl.base_gambar_menu +md.getFoto()).into(imageMenu);
        }catch (Exception e)
        {
            Log.d("load gambar",Config.StringUrl.base_gambar_menu +md.getFoto());
        }

        Nama.setText(md.getNama());
        no_hp.setText(md.getNo_hp());
        NoPlat.setText(md.getNoPlat());

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //ke detail
                Intent i = new Intent("DAPAT_DRIVER");
                i.putExtra("Nama",md.getNama());
                i.putExtra("IdDriver",md.getIdDriver());
                i.putExtra("IdPengguna",md.getIdPengguna());
                i.putExtra("NoSIM",md.getNoSIM());
                i.putExtra("NoPlat",md.getNoPlat());
                i.putExtra("no_hp",md.getNo_hp());
                i.putExtra("Foto",md.getFoto());
                Context c = view.getContext();
                c.sendBroadcast(i);
                //LinearLayout bgnya = view.findViewById(R.id.bgnya);
                //bgnya.setBackgroundColor(Color.parseColor("#ff0000"));

            }
        });

        return convertView;

    }
}
