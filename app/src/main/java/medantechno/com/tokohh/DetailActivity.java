package medantechno.com.tokohh;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import medantechno.com.tokohh.config.Config;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/11/18.
 */

public class DetailActivity extends AppCompatActivity {

    private ProgressDialog pDialog,pDialog_awal;

    TextView Menu,Harga,Nama,IsBuka,Deskripsi;
    ImageView imageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Menu = findViewById(R.id.Menu);
        Harga = findViewById(R.id.Harga);
        Nama = findViewById(R.id.Nama);
        IsBuka = findViewById(R.id.IsBuka);
        Deskripsi = findViewById(R.id.Deskripsi);
        imageMenu = findViewById(R.id.imageMenu);

        Intent i= getIntent();
        Bundle j = i.getExtras();

        try {
            Picasso.with(getApplicationContext()).load(Config.StringUrl.base_gambar_menu +j.getString("FileGambar")).into(imageMenu);
        }catch (Exception e)
        {
            Log.d("load gambar",Config.StringUrl.base_gambar_menu +j.getString("FileGambar"));
            System.out.println(e);
        }

        Menu.setText(j.getString("Menu"));
        Harga.setText("Rp."+j.getString("Harga"));
        Nama.setText(j.getString("Nama"));
        Deskripsi.setText(j.getString("Deskripsi"));

        if(j.getString("IsTersedia").equals("1"))
        {
            IsBuka.setText("Tersedia");
            IsBuka.setTextColor(Color.parseColor("#009688"));
        }else{
            IsBuka.setText("Habis");
            IsBuka.setTextColor(Color.parseColor("#ff0000"));
        }




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
