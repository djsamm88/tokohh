package medantechno.com.tokohh;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import medantechno.com.tokohh.config.Config;

/**
 * Created by dinaskominfokab.pakpakbharat on 27/09/18.
 */

public class Daftar extends Activity {

    EditText v_no_hp,v_Nama,v_Email,v_NIK,v_Alamat,password,password_lagi,v_TanggalLahir,Foto,FotoKTP,FotoKTP2;
    Button btnDaftar;
    private ProgressDialog pDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener dateTanggal;
    ImageView photonya1,photonya2,photonya3;
    String mCurrentPhotoPath,id_kategori;
    private static final int STORAGE_PERMISSION_CODE = 123;
    final int REQUEST_IMAGE_CAPTURE = 10;
    final int REQUEST_IMAGE_GALERY = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);


        /***** mengambil session ***/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String NoTelp = preferences.getString("NoTelp", "");
        if(!NoTelp.equals(""))
        {
            Intent directHome = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(directHome);
            finish();
        }
        /***** mengambil session ***/

        v_no_hp = (EditText)findViewById(R.id.no_hp);
        v_Nama = (EditText)findViewById(R.id.Nama);
        v_Email = (EditText)findViewById(R.id.Email);
        v_NIK = (EditText)findViewById(R.id.NIK);
        v_Alamat = (EditText)findViewById(R.id.Alamat);

        v_TanggalLahir = (EditText) findViewById(R.id.TanggalLahir);
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
                v_TanggalLahir.setText(sdf.format(myCalendar.getTime()));
            }

        };
        v_TanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Daftar.this, dateTanggal, myCalendar
                        .get(Calendar.YEAR)-15, myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /*** password ****/
        password = (EditText) findViewById(R.id.password);
        password_lagi = (EditText) findViewById(R.id.password_lagi);
        /*** password ****/

        /*** image ****/
        Foto = (EditText)findViewById(R.id.Foto);
        Foto.setEnabled(false);
        FotoKTP = (EditText)findViewById(R.id.FotoKTP);
        FotoKTP.setEnabled(false);
        FotoKTP2 = (EditText)findViewById(R.id.FotoKTP2);
        FotoKTP2.setEnabled(false);

        photonya1 = (ImageView)findViewById(R.id.photonya1);
        photonya1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilCamera(1);
            }
        });

        photonya2 = (ImageView)findViewById(R.id.photonya2);
        photonya2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilCamera(2);
            }
        });


        photonya3 = (ImageView)findViewById(R.id.photonya3);
        photonya3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilCamera(3);
            }
        });
        /*** image ****/

        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBase64();
            }
        });


    }





    private void ambilCamera(final int xxx)
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
                galleryAddPic();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this,
                        ex.toString()+"Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "medantechno.com.tokohh.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE+xxx);
            }
        }

    }



    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void ambilGalery(final int yyy)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALERY+yyy);
    }


    public String getPath(Uri uri)
    {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        //imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    //calling the method:
    //Bitmap converetdImage = getResizedBitmap(photo, 500);


    public static Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(12);
        paint.setAntiAlias(true);

        canvas.drawText(watermark, 20, 25, paint);

        return result;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == (REQUEST_IMAGE_CAPTURE+1))
        {
            Log.d("activityResult","req kamera");
            if (resultCode == Activity.RESULT_OK)
            {

                try {


                    File imgFile = new File(mCurrentPhotoPath);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        /** mengecilkan Bitmap **/
                        Bitmap converetdImage = getResizedBitmap(myBitmap, 200);
                        /** mengecilkan Bitmap **/

                        /** membuat watermark **/
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                        Bitmap mark = mark(converetdImage, timeStamp);
                        /** membuat watermark **/

                        /** menyimpan Bitmap jadi file **/
                        File file = new File(imgFile.getAbsolutePath());
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        mark.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                        /** menyimpan Bitmap jadi file **/

                        photonya1.setImageBitmap(mark);


                        //mInputPhotonya1.setText(mCurrentPhotoPath);

                        /** image to base64 ***/
                        Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] byteArrayImage = baos.toByteArray();
                        /** image to base64 ***/

                        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        Foto.setText(encodedImage);


                    } else {
                        Log.d("xxx", "tidaak ada");
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Ada kesalahan. Ulangi lagi.",Toast.LENGTH_LONG).show();



                }




            }

        }


        if(requestCode == (REQUEST_IMAGE_CAPTURE+2))
        {
            Log.d("activityResult","req kamera");
            if (resultCode == Activity.RESULT_OK)
            {

                try {


                    File imgFile = new File(mCurrentPhotoPath);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        /** mengecilkan Bitmap **/
                        Bitmap converetdImage = getResizedBitmap(myBitmap, 200);
                        /** mengecilkan Bitmap **/

                        /** membuat watermark **/
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                        Bitmap mark = mark(converetdImage, timeStamp);
                        /** membuat watermark **/

                        /** menyimpan Bitmap jadi file **/
                        File file = new File(imgFile.getAbsolutePath());
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        mark.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                        /** menyimpan Bitmap jadi file **/

                        photonya2.setImageBitmap(mark);


                        //mInputPhotonya1.setText(mCurrentPhotoPath);

                        /** image to base64 ***/
                        Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] byteArrayImage = baos.toByteArray();
                        /** image to base64 ***/

                        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        FotoKTP.setText(encodedImage);


                    } else {
                        Log.d("xxx", "tidaak ada");
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Ada kesalahan. Ulangi lagi.",Toast.LENGTH_LONG).show();



                }




            }

        }


        if(requestCode == (REQUEST_IMAGE_CAPTURE+3))
        {
            Log.d("activityResult","req kamera");
            if (resultCode == Activity.RESULT_OK)
            {

                try {


                    File imgFile = new File(mCurrentPhotoPath);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        /** mengecilkan Bitmap **/
                        Bitmap converetdImage = getResizedBitmap(myBitmap, 200);
                        /** mengecilkan Bitmap **/

                        /** membuat watermark **/
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                        Bitmap mark = mark(converetdImage, timeStamp);
                        /** membuat watermark **/

                        /** menyimpan Bitmap jadi file **/
                        File file = new File(imgFile.getAbsolutePath());
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        mark.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                        /** menyimpan Bitmap jadi file **/

                        photonya3.setImageBitmap(mark);


                        //mInputPhotonya1.setText(mCurrentPhotoPath);

                        /** image to base64 ***/
                        Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] byteArrayImage = baos.toByteArray();
                        /** image to base64 ***/

                        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        FotoKTP2.setText(encodedImage);


                    } else {
                        Log.d("xxx", "tidaak ada");
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Ada kesalahan. Ulangi lagi.",Toast.LENGTH_LONG).show();



                }




            }

        }







    }






    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    public void uploadBase64()
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final String filePath1 = Foto.getText().toString().trim();
        String filePath2 = FotoKTP.getText().toString().trim();
        String filePath3 = FotoKTP2.getText().toString().trim();
        final String no_hp = v_no_hp.getText().toString().trim();
        final String Nama = v_Nama.getText().toString().trim();
        final String Email = v_Email.getText().toString().trim();
        final String NIK = v_NIK.getText().toString().trim();
        final String Alamat = v_Alamat.getText().toString().trim();
        final String TanggalLahir = v_TanggalLahir.getText().toString().trim();
        String pass = password.getText().toString();
        String pass_lagi = password_lagi.getText().toString();

        String imageUri=null;



        boolean lanjut = true;


        if(pass == null || pass.length()<6)
        {
            Toast.makeText(getApplicationContext(),"Password minimal 6 karakter.",Toast.LENGTH_SHORT).show();
            password.requestFocus();
            lanjut = false;
        }

        if(!pass.equals(pass_lagi))
        {
            Toast.makeText(getApplicationContext(),"Password dan Password Lagi harus sama",Toast.LENGTH_SHORT).show();
            password_lagi.requestFocus();
            lanjut = false;
        }

        if(no_hp.equals(""))
        {
            Toast.makeText(this,"No HP harus diisi.",Toast.LENGTH_LONG).show();
            lanjut = false;
            v_no_hp.requestFocus();
        }


        if(Nama.equals(""))
        {
            Toast.makeText(this,"Nama harus diisi.",Toast.LENGTH_LONG).show();
            v_Nama.requestFocus();
            lanjut = false;
        }


        if(NIK.equals(""))
        {
            Toast.makeText(this,"NIK harus diisi.",Toast.LENGTH_LONG).show();
            v_NIK.requestFocus();
            lanjut = false;
        }


        if(Email.equals(""))
        {
            Toast.makeText(this,"Email harus diisi.",Toast.LENGTH_LONG).show();
            v_Email.requestFocus();
            lanjut = false;
        }


        if(Alamat.equals(""))
        {
            Toast.makeText(this,"Alamat harus diisi.",Toast.LENGTH_LONG).show();
            v_Alamat.requestFocus();
            lanjut = false;
        }


        if(TanggalLahir.equals(""))
        {
            Toast.makeText(this,"Tanggal Lahir harus diisi.",Toast.LENGTH_LONG).show();
            lanjut = false;
        }




        if(filePath1.equals(""))
        {
            Toast.makeText(this,"Peringatan!!! Foto harus ada.",Toast.LENGTH_LONG).show();
            lanjut = false;
        }


        if(filePath2.equals(""))
        {
            Toast.makeText(this,"Peringatan!!! FotoKTP harus ada.",Toast.LENGTH_LONG).show();
            lanjut = false;
        }


        if(!lanjut)
        {
            hidePDialog();
        }

        if(lanjut)
        {

            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                String URL = Config.StringUrl.tampung_base54;
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("Foto", filePath1);
                jsonBody.put("FotoKTP", filePath2);
                jsonBody.put("FotoKTP2", filePath3);
                jsonBody.put("NoTelp", no_hp);
                jsonBody.put("Nama", Nama);
                jsonBody.put("Email", Email);
                jsonBody.put("Alamat", Alamat);
                jsonBody.put("TanggalLahir", TanggalLahir);
                jsonBody.put("NIK", NIK);
                jsonBody.put("Password", pass);


                final String requestBody = jsonBody.toString();

                System.out.println(requestBody);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY_info", response);

                        hidePDialog();
                        if(response.equals("0"))
                        {
                            Toast.makeText(getApplicationContext(),"Berhasil mendaftar. Silahkan Login.",Toast.LENGTH_LONG).show();
                            notifnya("Berhasil","Berhasil mendaftar eWarung.");


                            Intent i = new Intent(getApplicationContext(),Login.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Gagal mendaftar. No HP Sudah digunakan.",Toast.LENGTH_LONG).show();
                            v_no_hp.requestFocus();
                        }




                        //finish();
                        //Intent i = new Intent(getApplicationContext(),HistoryAbsenActivity.class);
                        //startActivity(i);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                        hidePDialog();
                        Toast.makeText(getApplicationContext(),"Nampaknya ada masalah. Pastikan jaringan anda bagus.",Toast.LENGTH_LONG).show();
                        notifnya("Gagal","Nampaknya ada masalah. Pastikan jaringan anda bagus.");
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
                notifnya("Gagal","Nampaknya ada masalah. Pastikan jaringan anda bagus.");
            }

        }



    }


    public void notifnya(String title,String konten)
    {

        /****************  notif **********/
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(uri)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(konten)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notif = mBuilder.build();


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "upload_absen",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }


        mNotificationManager.notify(1, notif);
        /**************** notif **********/
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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");


            AlertDialog.Builder xx = new AlertDialog.Builder(getApplicationContext());
            xx.setTitle("Perhatian");
            xx.setMessage("Data belum di upload. Yakin meninggalkan?");
            xx.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            xx.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            xx.show();


            return false;

        }
        return super.onKeyDown(keyCode, event);
    }





}
