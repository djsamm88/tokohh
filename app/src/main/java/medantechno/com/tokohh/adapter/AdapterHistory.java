package medantechno.com.tokohh.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import medantechno.com.tokohh.BeliActivity;
import medantechno.com.tokohh.R;
import medantechno.com.tokohh.RestoActivity;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.model.ModelDriver;
import medantechno.com.tokohh.model.ModelHistory;
import medantechno.com.tokohh.model.ModelMenu;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class AdapterHistory extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelHistory> modelMenuList;


    public AdapterHistory(Activity activity, List<ModelHistory> modelEndeList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.adapter_history, null);


        TextView NamaResto  = (TextView) convertView.findViewById(R.id.NamaResto);
        TextView AlamatResto  = (TextView) convertView.findViewById(R.id.AlamatResto);
        TextView BiayaKirim  = (TextView) convertView.findViewById(R.id.BiayaKirim);
        TextView BiayaTotal  = (TextView) convertView.findViewById(R.id.BiayaTotal);
        TextView Status  = (TextView) convertView.findViewById(R.id.Status);
        TextView NamaDriver  = (TextView) convertView.findViewById(R.id.NamaDriver);
        TextView NoHpDriver  = (TextView) convertView.findViewById(R.id.NoHpDriver);
        TextView CreatedOn  = (TextView) convertView.findViewById(R.id.CreatedOn);



        Button Detail  = (Button) convertView.findViewById(R.id.Detail);
        Button HubDriver  = (Button) convertView.findViewById(R.id.HubDriver);
        Button HubPenjual  = (Button) convertView.findViewById(R.id.HubPenjual);
        Button Selesai  = (Button) convertView.findViewById(R.id.Selesai);
        ImageView imageMenu = (ImageView) convertView.findViewById(R.id.imageMenu);


        final ModelHistory mMenu = modelMenuList.get(position);
        Context context = parent.getContext();
        try {
            if(mMenu.getFoto().equals("null"))
            {
                imageMenu.setImageResource(R.drawable.noimage);
            }else{
                Picasso.with(context).load(Config.StringUrl.base_gambar_pengiriman +mMenu.getFoto()).into(imageMenu);
            }

            Log.d("load gambar",Config.StringUrl.base_gambar_pengiriman +mMenu.getFoto());
        }catch (Exception e)
        {
            Log.d("load gambar",Config.StringUrl.base_gambar_menu +mMenu.getFoto());

            imageMenu.setImageResource(R.drawable.noimage);
        }


        NamaResto.setText(mMenu.getNamaResto());
        AlamatResto.setText(mMenu.getAlamatResto()+" ("+mMenu.getNoHP_Resto()+")");
        String rpBiayaKirim = Rupiah(Double.parseDouble(mMenu.getBiayaKirim()));
        String rpBiayaTotal = Rupiah(Double.parseDouble(mMenu.getBiayaTotal()));
        BiayaKirim.setText("Biaya Kirim: "+rpBiayaKirim);
        BiayaTotal.setText("Biaya Total: "+rpBiayaTotal);
        Status.setText("Status: "+mMenu.getStatus());
        NamaDriver.setText("Driver: "+mMenu.getNamaDriver());
        NoHpDriver.setText("Hp Driver: "+mMenu.getNoHpDriver());
        CreatedOn.setText(mMenu.getCreatedOn());

        HubDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+mMenu.getNoHpDriver()));
                    activity.startActivity(intent);
                }catch (Exception e)
                {
                    System.out.println(e);
                }
            }
        });
        HubPenjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+mMenu.getNoHP_Resto()));
                    activity.startActivity(intent);
                }catch (Exception e)
                {
                    System.out.println(e);
                }
            }
        });

        Selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //action memnyelesaikan
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Anda ingin menyelesaikan?");
                alert.setMessage("Pastikan pesanan sudah diterima.");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //finish();


                        RequestQueue queue = Volley.newRequestQueue(activity);
                        String url =Config.StringUrl.selesaikan_pesanan+mMenu.getIdPesanan();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(activity,"Pesanan berhasil diselesaikan.",Toast.LENGTH_SHORT).show();
                                        activity.finish();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(activity,"Offline Mode. No inet.."+error.toString(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(stringRequest);

                    }
                });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

                alert.show();
            }
        });
        Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalPesanan(mMenu.getIdPesanan());
            }
        });

        Selesai.setVisibility(View.INVISIBLE);
        if(mMenu.getStatus().equals("DIKIRIM"))
        {
            Selesai.setVisibility(View.VISIBLE);
        }

        if(mMenu.getStatus().equals("SELESAI"))
        {
          HubDriver.setVisibility(View.INVISIBLE);
          HubPenjual.setVisibility(View.INVISIBLE);
          Selesai.setVisibility(View.INVISIBLE);
        }

        if(mMenu.getStatus().equals("DITOLAK"))
        {
            HubDriver.setVisibility(View.INVISIBLE);
            HubPenjual.setVisibility(View.INVISIBLE);
            Selesai.setVisibility(View.INVISIBLE);
        }

        if(mMenu.getStatus().equals("MENUNGGU"))
        {
            Selesai.setVisibility(View.INVISIBLE);
        }

        if(mMenu.getStatus().equals("DITERIMA"))
        {
            Selesai.setVisibility(View.INVISIBLE);
        }



        return convertView;

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



    private void modalPesanan(String IdPesanan)
    {
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            final View converView = (View) inflater.inflate(R.layout.layout_alert,null);
            alert.setView(converView);
            alert.setTitle("Detail Pesanan Anda:");

            // alert.setMessage("Message");

            ListView lv = converView.findViewById(R.id.listDriver);
            final List<ModelPesanan> modelDriverList = new ArrayList<ModelPesanan>();
            final AdapterDetailPesanan adapterDriver = new AdapterDetailPesanan(activity,modelDriverList);
            //converView.setAdapter(adapterMenu);
            lv.setAdapter(adapterDriver);



            RequestQueue queue = Volley.newRequestQueue(activity);
            JsonArrayRequest sptRequest = new JsonArrayRequest(Config.StringUrl.detail_pesanan+IdPesanan,new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("Hasil", response.toString());

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject obj = response.getJSONObject(i);
                            ModelPesanan modelDriver = new ModelPesanan();

                            modelDriver.setMenu(obj.getString("Menu"));
                            modelDriver.setFileGambar(obj.getString("FileGambar"));
                            modelDriver.setHarga(obj.getString("Harga"));
                            modelDriver.setSubtotal(obj.getString("Subtotal"));
                            modelDriver.setJumlah(obj.getString("Jumlah"));

                            modelDriverList.add(modelDriver);
                            adapterDriver.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(activity,"Offline Mode. No inet.."+error.toString(),Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());


                }
            });

            queue.add(sptRequest);
            adapterDriver.notifyDataSetChanged();




            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Your action here

                }
            });

            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

            alert.show();
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }


}
