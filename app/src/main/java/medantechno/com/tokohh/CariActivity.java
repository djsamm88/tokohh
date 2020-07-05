package medantechno.com.tokohh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import medantechno.com.tokohh.adapter.AdapterHistory;
import medantechno.com.tokohh.adapter.AdapterMenu;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.model.ModelHistory;
import medantechno.com.tokohh.model.ModelMenu;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class CariActivity extends AppCompatActivity {

    private ProgressDialog pDialog,pDialog_awal;


    SharedPreferences preferences;
    String UserID;
    String key;
    SwipeRefreshLayout swipe;
    EditText pencarian;
    Button btn_go_cari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        pencarian = findViewById(R.id.pencarian);
        btn_go_cari = findViewById(R.id.btn_go_cari);

        /*** session **/
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = preferences.getString("UserID","");
        /*** session **/
        try {
            Intent intent = getIntent();
            key = intent.getStringExtra("key");
            pencarian.setText(key);
            list_history(key);
        }catch (Exception e)
        {

        }


        btn_go_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_history(pencarian.getText().toString());
            }
        });

    }

    private void list_history(final String key)
    {


        /********* list view *****/
        ListView listHistory = (ListView)findViewById(R.id.listCari);
        final List<ModelMenu> modelMenuList = new ArrayList<ModelMenu>();
        final AdapterMenu adapterMenu = new AdapterMenu(this,modelMenuList);
        listHistory.setAdapter(adapterMenu);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        modelMenuList.clear();


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest sptRequest = new JsonArrayRequest(Config.StringUrl.cari_menu+key,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Hasil", response.toString());
                hidePDialog();

                if(response.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Belum ada menu "+key,Toast.LENGTH_SHORT).show();
                }
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
