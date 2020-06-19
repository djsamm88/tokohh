package medantechno.com.tokohh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import medantechno.com.tokohh.config.Config;

/**
 * Created by dinaskominfokab.pakpakbharat on 27/09/18.
 */

public class Login extends Activity {

    EditText mNohp,mPassword;
    Button mBtnGo,btnDaftar;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mNohp = (EditText) findViewById(R.id.no_hp);
        mPassword = (EditText) findViewById(R.id.password);
        mBtnGo = (Button) findViewById(R.id.btnGo);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goDaftar = new Intent(getApplicationContext(),Daftar.class);
                startActivity(goDaftar);
            }
        });

        /***** mengambil session ***/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String UserName = preferences.getString("UserID", "");
        String NoTelp = preferences.getString("NoTelp", "");
        System.out.println("isi_session:"+UserName+"-"+NoTelp);
        /***** mengambil session ***/

        if(!NoTelp.equals(""))
        {
            Intent directHome = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(directHome);
            finish();
        }

        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String NIP = mNohp.getText().toString();
                String password = mPassword.getText().toString();

                //Log.d("Login.javaa","login:"+NIP+"&"+password);
                if(NIP.equals("") || password.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"No HP dan Password harus diisi",Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("Login.java","login:"+NIP+"&"+password);
                panggilJson(NIP,password);
            }
        });


    }



    private void panggilJson(String NIP, String Password)
    {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final String pass = Password;

        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.StringUrl.cek_login+"?NoTelp="+NIP+"&Password="+Password;

        System.out.println(url);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // display response
                System.out.println("jumlah:"+response.length());
                if(response.length()>0)
                {
                    Toast.makeText(getApplicationContext(),"Berhasil Login. Mohon tunggu...",Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject hasil = response.getJSONObject(0);
                        System.out.println(hasil.getString("UserID"));

                        /*** memasukkan ke session ***/
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("UserID",hasil.getString("UserID"));
                        editor.putString("Password",hasil.getString("Password"));
                        editor.putString("RoleID",hasil.getString("RoleID"));
                        editor.putString("LasLogin",hasil.getString("LasLogin"));
                        editor.putString("IsSuspend",hasil.getString("IsSuspend"));
                        editor.putString("Email",hasil.getString("Email"));
                        editor.putString("Nama",hasil.getString("Nama"));
                        editor.putString("NIK",hasil.getString("NIK"));
                        editor.putString("NoTelp",hasil.getString("NoTelp"));
                        editor.putString("Alamat",hasil.getString("Alamat"));
                        editor.putString("TanggalLahir",hasil.getString("TanggalLahir"));
                        editor.putString("Foto",hasil.getString("Foto"));
                        editor.putString("FotoKTP",hasil.getString("FotoKTP"));
                        editor.putString("FotoKTP2",hasil.getString("FotoKTP2"));
                        editor.putString("TanggalRegistrasi",hasil.getString("TanggalRegistrasi"));
                        editor.putString("IsEmailVerified",hasil.getString("IsEmailVerified"));

                        editor.apply();
                        /*** memasukkan ke session ***/

                        finish();
                        Intent ii = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(ii);

                    }catch (Exception x)
                    {
                        System.out.println("error_preferenced:"+x);
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Gagal login, No HP Atau password salah...",Toast.LENGTH_SHORT).show();
                }
                System.out.println("hasil:"+response);
                hidePDialog();


            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response",error.toString());

                        hidePDialog();

                        Toast.makeText(getApplicationContext(),"Gagal login. Periksa jaringan internet anda. "+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                }
        );

        // add it to the RequestQueue
        queue.add(jsonObjReq);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
