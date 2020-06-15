package medantechno.com.tokohh;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import medantechno.com.tokohh.adapter.DinasLuarAdapter;
import medantechno.com.tokohh.config.Config;
import medantechno.com.tokohh.database.DbUser;
import medantechno.com.tokohh.model.ModelDinasLuar;
import medantechno.com.tokohh.model.ModelUser;


/**
 * Created by dinaskominfokab.pakpakbharat on 27/09/18.
 */

public class CutiSakit extends AppCompatActivity {


    private ProgressDialog pDialog;

    private ListView listView;


    private DinasLuarAdapter dinasLuarAdapter;

    private List<ModelDinasLuar> dinasLuarList = new ArrayList<ModelDinasLuar>();

    String NIP,nama,jabatan,fid;
    int id_user;

    LayoutInflater inflater_dialog;
    AlertDialog.Builder dialog;
    View dialogView;

    TextView tanggal,tanggal_antara,keterangan;
    DatePickerDialog.OnDateSetListener dateTanggal, dateTanggal_antara;
    Calendar myCalendar;

    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuti_sakit);




        /**************** cek dulu database apakah sudah pernah login **********/
        DbUser dbUser = new DbUser(getApplicationContext());
        try{
            ModelUser modelUser = dbUser.select_by_terbesar();

            NIP = modelUser.getNIP();
            nama = modelUser.getNama();
            jabatan = modelUser.getJabatan();
            id_user = modelUser.getId_user();
            fid = modelUser.getFid();
        }catch (Exception x) {

        }
        /**************** cek dulu database apakah sudah pernah login **********/




        /*********Panggil adapter***********************/
        listView = (ListView) findViewById(R.id.list);
        dinasLuarAdapter = new DinasLuarAdapter(this, dinasLuarList,getApplicationContext());
        listView.setAdapter(dinasLuarAdapter);
        list(NIP);
        /*********Panggil adapter***********************/

        Button btn = (Button)findViewById(R.id.btnAjukan);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm();
            }
        });


        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list(NIP);
                pullToRefresh.setRefreshing(false);
            }
        });
    }



    private void list(String NIP)
    {


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        dinasLuarList.clear();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest sptRequest = new JsonArrayRequest(Config.StringUrl.data_cuti_sakit_by_nik+"?nik="+NIP,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Hasil", response.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);

                        ModelDinasLuar modelDinasLuar = new ModelDinasLuar();

                        modelDinasLuar.setId(obj.getString("id"));
                        modelDinasLuar.setTanggal(obj.getString("tanggal"));
                        modelDinasLuar.setFID(obj.getString("FID"));
                        modelDinasLuar.setKeterangan(obj.getString("keterangan"));
                        modelDinasLuar.setStatus(obj.getString("status"));


                        dinasLuarList.add(modelDinasLuar);
                        dinasLuarAdapter.notifyDataSetChanged();
                        System.out.println("jumlah_data:"+dinasLuarList.size());


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
        dinasLuarAdapter.notifyDataSetChanged();


    }



    public void DialogForm()
    {
        dialog = new AlertDialog.Builder(CutiSakit.this);
        inflater_dialog = LayoutInflater.from(CutiSakit.this);
        dialogView = inflater_dialog.inflate(R.layout.cuti_sakit_form, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setTitle("Form Cuti Sakit/Sakit");


         tanggal = (TextView) dialogView.findViewById(R.id.tanggal);
         keterangan = (TextView) dialogView.findViewById(R.id.keterangan);

        myCalendar = Calendar.getInstance();
        dateTanggal = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tanggal.setText(sdf.format(myCalendar.getTime()));
            }

        };

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CutiSakit.this, dateTanggal, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        Button btnSimpan = (Button) dialogView.findViewById(R.id.btnSimpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tanggal.getText().toString().equals("")  || keterangan.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Semua field required!.",Toast.LENGTH_LONG).show();
                }else{
                    simpanForm();
                }
            }
        });


        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void simpanForm()
    {
        String tgl = tanggal.getText().toString();
        String ket = keterangan.getText().toString();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = Config.StringUrl.set_form_cuti_sakit;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("NIK", NIP);
            jsonBody.put("FID", fid);
            jsonBody.put("tanggal", tgl);
            jsonBody.put("keterangan", ket);


            final String requestBody = jsonBody.toString();

            System.out.println(requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    hidePDialog();
                    Toast.makeText(getApplicationContext(),"Permohonan berhasil di upload.",Toast.LENGTH_LONG).show();

                    try {
                        finish();
                        Intent i = new Intent(getApplicationContext(), CutiSakit.class);
                        startActivity(i);
                    }catch (Exception e)
                    {

                    }




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
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Nampaknya ada masalah. Pastikan jaringan anda bagus.",Toast.LENGTH_LONG).show();

        }
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
