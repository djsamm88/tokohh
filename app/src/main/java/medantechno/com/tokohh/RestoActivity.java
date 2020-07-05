package medantechno.com.tokohh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import medantechno.com.tokohh.adapter.AdapterMenu;
import medantechno.com.tokohh.adapter.AdapterMenuResto;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.database.DbLokasi;
import medantechno.com.tokohh.database.DbPesanan;
import medantechno.com.tokohh.model.ModelLokasi;
import medantechno.com.tokohh.model.ModelMenu;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class RestoActivity extends AppCompatActivity {

    private ProgressDialog pDialog,pDialog_awal;

    Bundle j;
    TextView total_belanja,alamat_warung,nama_warung;
    Button btn_go_beli,reset;
    String latku,lngku;
    ModelLokasi modelLokasiList;
    String harga_ongkir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto);
        ongkirPerKM();

        total_belanja = findViewById(R.id.total_belanja);
        total_belanja.setEnabled(false);
        btn_go_beli = findViewById(R.id.btn_go_beli);
        nama_warung = findViewById(R.id.nama_warung);
        alamat_warung = findViewById(R.id.alamat_warung);

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disini nanti reset db jika ada
                try{
                    new DbPesanan(getApplicationContext()).hapusSemua();
                }catch (Exception e)
                {

                }
                finish();
                startActivity(getIntent());

            }
        });

        Intent i= getIntent();
        j = i.getExtras();

        try{
            DbLokasi dbLokasi = new DbLokasi(getApplicationContext());
            modelLokasiList = dbLokasi.get_terbesar();
            latku = modelLokasiList.getLat();
            lngku = modelLokasiList.getLng();
        }catch (Exception e)
        {

        }


        try {


            setTitle(j.getString("Nama"));
        }catch (Exception x){
            System.out.println(x);
        }

        String lat_warung = j.getString("Lat");
        String lng_warung = j.getString("Long");

        Double a_lat = Double.parseDouble(latku);
        Double a_lng = Double.parseDouble(lngku);

        Double b_lat = Double.parseDouble(lat_warung);
        Double b_lng = Double.parseDouble(lng_warung);

        //final Double jarak = distance(a_lat,a_lng,b_lat,b_lng);
        final Double jarak = distance_dua(a_lat,a_lng,b_lat,b_lng);

        nama_warung.setText(j.getString("Nama"));
        alamat_warung.setText(j.getString("Alamat") +" ("+jarak+" Km dari anda.)");

        btn_go_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RestoActivity.this,BeliActivity.class);
                i.putExtra("IdMenu",j.getString("IdMenu"));
                i.putExtra("IdResto",j.getString("IdResto"));
                i.putExtra("Nama",j.getString("Nama"));
                i.putExtra("Alamat",j.getString("Alamat"));
                i.putExtra("Jarak",String.valueOf(jarak));
                i.putExtra("Lat",latku);
                i.putExtra("Long",lngku);
                i.putExtra("harga_ongkir",harga_ongkir);
                startActivity(i);
            }
        });

        /********* list view *****/
        ListView listRekomendasi = (ListView)findViewById(R.id.listResto);
        final List<ModelMenu> modelMenuList = new ArrayList<ModelMenu>();
        final AdapterMenuResto adapterMenu = new AdapterMenuResto(this,modelMenuList);
        listRekomendasi.setAdapter(adapterMenu);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        modelMenuList.clear();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest sptRequest = new JsonArrayRequest(Config.StringUrl.list_menu_resto+j.getString("IdResto"),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Hasil", response.toString());

                System.out.println("abc:"+Config.StringUrl.list_menu_resto+j.getString("IdResto"));
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);

                        ModelMenu modelMenu = new ModelMenu();

                        modelMenu.setIdMenu(obj.getString("IdMenu"));
                        modelMenu.setIdResto(obj.getString("IdResto"));
                        modelMenu.setMenu(obj.getString("Menu"));
                        modelMenu.setDeskripsi(obj.getString("Deskripsi"));
                        modelMenu.setHarga(obj.getString("Harga"));
                        modelMenu.setIsTersedia(obj.getString("IsTersedia"));
                        modelMenu.setIsAktif(obj.getString("IsAktif"));
                        modelMenu.setFileGambar(obj.getString("FileGambar"));
                        modelMenu.setIdPemilik(obj.getString("IdPemilik"));
                        modelMenu.setNama(obj.getString("Nama"));
                        modelMenu.setAlamat(obj.getString("Alamat"));
                        modelMenu.setLat(obj.getString("Lat"));
                        modelMenu.setLong(obj.getString("Long"));
                        modelMenu.setIsHalal(obj.getString("IsHalal"));
                        modelMenu.setIsBuka(obj.getString("IsBuka"));



                        modelMenuList.add(modelMenu);
                        adapterMenu.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                hidePDialog();

                Toast.makeText(getApplicationContext(),"Offline Mode. No inet.."+error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());


            }
        });

        queue.add(sptRequest);
        adapterMenu.notifyDataSetChanged();

        /**** list view ****/



        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("TOTAL_DAPAT");
        registerReceiver(receiver, filter);
        /****** ini untuk receiver ****/

    }


    private void ongkirPerKM()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest sptRequest = new JsonArrayRequest(Config.StringUrl.harga_ongkir,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Ongkir", response.toString());
                try {
                    JSONObject obj = response.getJSONObject(0);
                    harga_ongkir = obj.getString("harga_ongkir");
                    System.out.println("ongkir"+obj.getString("harga_ongkir"));
                }catch (Exception e)
                {
                    System.out.println("ongkir="+e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(sptRequest);

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

    /**** receiver dari adapter ***/
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT).show();
            String  total = String.valueOf(intent.getIntExtra("total",0));
            total_belanja.setText(Rupiah(Double.valueOf(total)));
            Toast.makeText(getApplicationContext(), total, Toast.LENGTH_SHORT).show();


            DbPesanan dbPesanan = new DbPesanan(getApplicationContext());
            List<ModelPesanan> modelPesananList = dbPesanan.getAll(intent.getStringExtra("IdResto"));

            for(ModelPesanan mp : modelPesananList)
            {
                if(mp.getJumlah().equals("0") || mp.getJumlah().equals(null) || mp.getJumlah().equals(""))
                {
                    System.out.println("Kosong");
                }else{
                    System.out.println(mp.getMenu());

                }

            }


        }
    };


    /******** ambil jarak *****/
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        double km = dist * 1.609344;//mile ke km
        km = Math.round(km*100D)/100D;//pembulatan
        return (km);
    }

    private double distance_dua(double lat1, double lon1, double lat2, double lon2)
    {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat2);
        locationB.setLongitude(lon2);

        double jaraknya = locationA.distanceTo(locationB)/1000;
        return Math.round(jaraknya);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    /************/


    @Override
    protected void onResume() {

        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("TOTAL_DAPAT");
        registerReceiver(receiver, filter);
        /****** ini untuk receiver ****/
        super.onResume();
    }

    @Override
    protected void onPause() {

        /**** ini untuk receiver ****/
        unregisterReceiver(receiver);
        /****** ini untuk receiver ****/
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        /*
        if(editText.getText().toString().equals(""))
        {
            finish();
        }else{
            editText.setText("");
        }

         */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            /*** menghapus session ***/
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferences.edit().clear().commit();
            /*** menghapus session ***/

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
        hidePDialogAwal();

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    private void hidePDialogAwal() {
        if (pDialog_awal != null) {
            pDialog_awal.dismiss();
            pDialog_awal = null;
        }
    }





}
