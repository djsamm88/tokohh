package medantechno.com.tokohh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import medantechno.com.tokohh.adapter.AdapterBeli;
import medantechno.com.tokohh.adapter.AdapterHistory;
import medantechno.com.tokohh.adapter.AdapterMenuResto;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.database.DbLokasi;
import medantechno.com.tokohh.database.DbPesanan;
import medantechno.com.tokohh.model.ModelHistory;
import medantechno.com.tokohh.model.ModelLokasi;
import medantechno.com.tokohh.model.ModelMenu;
import medantechno.com.tokohh.model.ModelPesanan;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class HistoryActivity extends AppCompatActivity {

    private ProgressDialog pDialog,pDialog_awal;


    SharedPreferences preferences;
    String UserID;
    String status;
    SwipeRefreshLayout swipe;
    TextView jenis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        jenis = findViewById(R.id.jenis);

        Intent intent = getIntent();
        status = intent.getStringExtra("STATUS");
        
        /*** session **/
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = preferences.getString("UserID","");
        /*** session **/

        jenis.setText(status);

        list_history(status);
        try {

            swipe = findViewById(R.id.swipe);
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list_history(status);
                    swipe.setRefreshing(false);
                }
            });

        }catch (Exception e)
        {
            System.out.println(e);
        }



    }

    private void list_history(String status)
    {


        /********* list view *****/
        ListView listHistory = (ListView)findViewById(R.id.listHistory);
        final List<ModelHistory> modelMenuList = new ArrayList<ModelHistory>();
        final AdapterHistory adapterMenu = new AdapterHistory(this,modelMenuList);
        listHistory.setAdapter(adapterMenu);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        modelMenuList.clear();


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest sptRequest = new JsonArrayRequest(Config.StringUrl.history_pesanan+"?IdPembeli="+UserID+"&Status="+status,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Hasil", response.toString());
                hidePDialog();

                if(response.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Belum ada pesanan.",Toast.LENGTH_SHORT).show();
                }
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);

                        ModelHistory modelMenu = new ModelHistory();

                        modelMenu.setIdPesanan(obj.getString("IdPesanan"));
                        modelMenu.setIdResto(obj.getString("IdResto"));
                        modelMenu.setIdDriver(obj.getString("IdDriver"));
                        modelMenu.setIdPembeli(obj.getString("IdPembeli"));
                        modelMenu.setStatus(obj.getString("Status"));
                        modelMenu.setBiayaKirim(obj.getString("BiayaKirim"));
                        modelMenu.setBiayaTotal(obj.getString("BiayaTotal"));
                        modelMenu.setTujuan(obj.getString("Tujuan"));
                        modelMenu.setLat(obj.getString("Lat"));
                        modelMenu.setLong(obj.getString("Long"));
                        modelMenu.setJarak(obj.getString("Jarak"));
                        modelMenu.setFoto(obj.getString("Foto"));
                        modelMenu.setLong(obj.getString("Long"));
                        modelMenu.setCreatedOn(obj.getString("CreatedOn"));
                        modelMenu.setNamaDriver(obj.getString("NamaDriver"));
                        modelMenu.setNoHpDriver(obj.getString("NoHpDriver"));
                        modelMenu.setFotoDriver(obj.getString("FotoDriver"));
                        modelMenu.setNoPlat(obj.getString("NoPlat"));
                        modelMenu.setNamaResto(obj.getString("NamaResto"));
                        modelMenu.setAlamatResto(obj.getString("AlamatResto"));
                        modelMenu.setLatResto(obj.getString("LatResto"));
                        modelMenu.setLongResto(obj.getString("LongResto"));
                        modelMenu.setAlasanTolak(obj.getString("AlasanTolak"));
                        modelMenu.setNoHP_Resto(obj.getString("NoHP_Resto"));



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
