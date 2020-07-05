package medantechno.com.tokohh;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import medantechno.com.tokohh.adapter.AdapterBeli;
import medantechno.com.tokohh.adapter.AdapterDriver;
import medantechno.com.tokohh.adapter.AdapterMenuResto;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.database.DbLokasi;
import medantechno.com.tokohh.database.DbPesanan;
import medantechno.com.tokohh.model.ModelDriver;
import medantechno.com.tokohh.model.ModelLokasi;
import medantechno.com.tokohh.model.ModelMenu;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class BeliActivity extends AppCompatActivity {

    private ProgressDialog pDialog,pDialog_awal;

    Bundle bundle;
    TextView alamat_warung,nama_warung,t4ongkir,t4Subtotal,t4total,t4Driver;

    Double ongkir=0.0;
    Double subtotal=0.0;
    Double total=0.0;
    Button btnDriver,btnProses;

    String  NamaDriver,IdDriver,IdPengguna,NoSIM,NoPlat,no_hp,Foto;
    String jarak,lat,lng,harga_ongkir,IdResto,Nama,Alamat;
    SharedPreferences preferences;
    String UserID;
    EditText t4Alamat_antar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli);
        /*** session **/
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        /*** session **/

        t4Driver = findViewById(R.id.t4Driver);
        btnProses = findViewById(R.id.btnProses);

        Intent i = getIntent();
        bundle = i.getExtras();

        jarak = bundle.getString("Jarak");
        lat = bundle.getString("Lat");
        lng = bundle.getString("Long");
        harga_ongkir = bundle.getString("harga_ongkir");
        IdResto = bundle.getString("IdResto");
        Nama = bundle.getString("Nama");
        Alamat = bundle.getString("Alamat");

        try {
            ongkir = Double.parseDouble(harga_ongkir) * Double.parseDouble(jarak);
        }catch (Exception e)
        {
            System.out.println(e+"-ongkir-"+harga_ongkir+"-"+jarak);
        }

        try {
            DbPesanan dbPesanan = new DbPesanan(getApplicationContext());
            List<ModelPesanan> modelPesananList = dbPesanan.getAll(bundle.getString("IdResto"));

            for (ModelPesanan mp : modelPesananList) {
                if (mp.getJumlah().equals("0") || mp.getJumlah().equals(null) || mp.getJumlah().equals("")) {
                    System.out.println("Kosong");
                } else {
                    System.out.println(mp.getIdMenu() + "-" + mp.getJumlah() + "-" + mp.getSubtotal() + "-" + mp.getCatatan());
                    subtotal += Double.parseDouble(mp.getSubtotal());

                }

            }

            total = subtotal + ongkir;
        }catch (Exception e){
            System.out.println("apa_ni:"+e);
        }

        alamat_warung = findViewById(R.id.alamat_warung);
        nama_warung = findViewById(R.id.nama_warung);
        t4ongkir=findViewById(R.id.t4ongkir);
        t4Subtotal=findViewById(R.id.t4Subtotal);
        t4total=findViewById(R.id.t4total);

        alamat_warung.setText(bundle.getString("Alamat") +" ("+jarak+" Km dari anda.)");
        nama_warung.setText(bundle.getString("Nama"));

        t4ongkir.setText(Rupiah(ongkir));
        t4Subtotal.setText(Rupiah(subtotal));
        t4total.setText(Rupiah(total));






        /********* list view *****/
        ListView listRekomendasi = (ListView)findViewById(R.id.listBeli);
        final List<ModelPesanan> modelMenuList = new ArrayList<ModelPesanan>();
        final AdapterBeli adapterMenu = new AdapterBeli(this,modelMenuList);
        listRekomendasi.setAdapter(adapterMenu);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        modelMenuList.clear();
        DbPesanan dbPesanan = new DbPesanan(getApplicationContext());
        List<ModelPesanan> modelPesananList = dbPesanan.getAll(bundle.getString("IdResto"));

        for (ModelPesanan mp : modelPesananList) {
            if (mp.getJumlah().equals("0") || mp.getJumlah().equals(null) || mp.getJumlah().equals("")) {
                System.out.println("Kosong");
            } else {
                System.out.println(mp.getIdMenu() + "-" + mp.getJumlah() + "-" + mp.getSubtotal() + "-" + mp.getCatatan());
                ModelPesanan modelPesanan = new ModelPesanan();
                modelPesanan.setIdResto(mp.getIdResto());
                modelPesanan.setIdMenu(mp.getIdMenu());
                modelPesanan.setHarga(mp.getHarga());
                modelPesanan.setSubtotal(mp.getSubtotal());
                modelPesanan.setJumlah(mp.getJumlah());
                modelPesanan.setCatatan(mp.getCatatan());
                modelPesanan.setFileGambar(mp.getFileGambar());
                modelPesanan.setMenu(mp.getMenu());
                modelMenuList.add(modelPesanan);
            }

            hidePDialog();

        }

        adapterMenu.notifyDataSetChanged();

        /**** list view ****/

        btnDriver = findViewById(R.id.btnDriver);
        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modalDriver();
            }
        });

        t4Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalDriver();
            }
        });


        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("DAPAT_DRIVER");
        registerReceiver(receiver, filter);
        /****** ini untuk receiver ****/


        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(NamaDriver+"-"+IdDriver+"-"+IdPengguna+"-"+NoSIM+"-"+NoPlat+"-"+no_hp+"-"+Foto);
                System.out.println(jarak+"-"+lat+"-"+lng+"-"+ongkir+"-"+IdResto+"-"+Nama+"-"+Alamat);
                UserID = preferences.getString("UserID","");

                t4Alamat_antar = findViewById(R.id.t4Alamat_antar);

                String IdPembeli = UserID;
                String IdDriver = IdPengguna;
                String BiayaKirim = String.valueOf(ongkir);
                String BiayaTotal = String.valueOf(total);
                String Tujuan = t4Alamat_antar.getText().toString();

                String idMenuMulti="";
                String HargaMulti="";
                String JumlahMulti="";
                String SubtotalMulti="";
                String CatatanMulti="";


                DbPesanan dbPesanan = new DbPesanan(getApplicationContext());
                List<ModelPesanan> modelPesananList = dbPesanan.getAll(IdResto);
                for (ModelPesanan mp : modelPesananList) {
                    if (mp.getJumlah().equals("0") || mp.getJumlah()==null || mp.getJumlah().equals("")) {
                        System.out.println("Kosong");
                    } else {
                        System.out.println(mp.getIdMenu() + "-" + mp.getJumlah() + "-" + mp.getSubtotal() + "-" + mp.getCatatan());
                        idMenuMulti+=mp.getIdMenu()+"|";
                        HargaMulti+=mp.getHarga()+"|";
                        JumlahMulti+=mp.getJumlah()+"|";
                        SubtotalMulti+=mp.getSubtotal()+"|";
                        CatatanMulti+=mp.getCatatan()+"|";

                    }


                }


                if(Tujuan.equals(""))
                {
                    t4Alamat_antar.requestFocus();
                    Toast.makeText(getApplicationContext(),"Alamat pengantaran harus diisi.",Toast.LENGTH_SHORT).show();
                }else{

                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String URL = Config.StringUrl.proses_pembelian;
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("IdResto", IdResto);
                        jsonBody.put("IdDriver", IdDriver);
                        jsonBody.put("IdPembeli", IdPembeli);
                        jsonBody.put("BiayaKirim", BiayaKirim);
                        jsonBody.put("BiayaTotal", BiayaTotal);
                        jsonBody.put("Tujuan", Tujuan);
                        jsonBody.put("Lat", lat);
                        jsonBody.put("Long", lng);
                        jsonBody.put("Jarak", jarak);

                        jsonBody.put("idMenuMulti", idMenuMulti);
                        jsonBody.put("HargaMulti", HargaMulti);
                        jsonBody.put("JumlahMulti", JumlahMulti);
                        jsonBody.put("SubtotalMulti", SubtotalMulti);
                        jsonBody.put("CatatanMulti", CatatanMulti);



                        final String requestBody = jsonBody.toString();

                        System.out.println(requestBody);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("VOLLEY", response);
                                hidePDialog();
                                Toast.makeText(getApplicationContext(),"Berhasil diproses, Mohon cek status secara berkala.",Toast.LENGTH_LONG).show();
                                /**** disini action setelah dipesan ****/
                                new DbPesanan(getApplicationContext()).hapusSemua();
                                Intent i = new Intent(getApplicationContext(),HistoryActivity.class);
                                i.putExtra("STATUS","MENUNGGU");
                                startActivity(i);
                                finish();
                                /**** disini action setelah dipesan ****/


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.toString());
                                hidePDialog();
                                Toast.makeText(getApplicationContext(),"Nampaknya ada masalah. Pastikan jaringan anda bagus.",Toast.LENGTH_LONG).show();

                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }

                        };

                        requestQueue.add(stringRequest);

                        /** mengatur time out volley***/
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                50000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        /** mengatur time out volley***/


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Nampaknya ada masalah. Pastikan jaringan anda bagus."+e.toString(),Toast.LENGTH_LONG).show();

                    }

                }





            }
        });
    }


    private void modalDriver()
    {
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(BeliActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            final View converView = (View) inflater.inflate(R.layout.layout_alert,null);
            alert.setView(converView);
            alert.setTitle("Pilih Driver:");

            // alert.setMessage("Message");

            ListView lv = converView.findViewById(R.id.listDriver);
            final List<ModelDriver> modelDriverList = new ArrayList<ModelDriver>();
            final AdapterDriver adapterDriver = new AdapterDriver(BeliActivity.this,modelDriverList);
            //converView.setAdapter(adapterMenu);
            lv.setAdapter(adapterDriver);



            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest sptRequest = new JsonArrayRequest(Config.StringUrl.list_driver,new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("Hasil", response.toString());

                    hidePDialog();

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject obj = response.getJSONObject(i);
                            ModelDriver modelDriver = new ModelDriver();
                            modelDriver.setIdDriver(obj.getString("IdDriver"));
                            modelDriver.setIdPengguna(obj.getString("IdPengguna"));
                            modelDriver.setNama(obj.getString("Nama"));
                            modelDriver.setNoSIM(obj.getString("NoSIM"));
                            modelDriver.setNoPlat(obj.getString("NoPlat"));
                            modelDriver.setNo_hp(obj.getString("no_hp"));
                            modelDriver.setFoto(obj.getString("Foto"));

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


                    hidePDialog();

                    Toast.makeText(getApplicationContext(),"Offline Mode. No inet.."+error.toString(),Toast.LENGTH_SHORT).show();
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

    /**** receiver dari adapterDriver ***/
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT).show();
            NamaDriver = intent.getStringExtra("Nama");
            IdDriver = intent.getStringExtra("IdDriver");
            IdPengguna = intent.getStringExtra("IdPengguna");
            NoSIM = intent.getStringExtra("NoSIM");
            NoPlat = intent.getStringExtra("NoPlat");
            no_hp = intent.getStringExtra("no_hp");
            Foto = intent.getStringExtra("Foto");


            Toast.makeText(getApplicationContext(), "Anda memilih: "+NamaDriver, Toast.LENGTH_SHORT).show();

            t4Driver.setVisibility(View.VISIBLE);
            t4Driver.setText(NamaDriver +" - "+no_hp);
            btnDriver.setVisibility(View.INVISIBLE);


        }
    };



    @Override
    protected void onResume() {

        /****** ini untuk receiver ****/
        IntentFilter filter = new IntentFilter();
        filter.addAction("DAPAT_DRIVER");
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


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pesanan belum selesai. Ingin keluar?");
        // alert.setMessage("Message");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.show();

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
