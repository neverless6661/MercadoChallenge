package com.lubani.mercadochallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    EditText etSearchField;
    String textobusqueda;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearchField = (EditText) findViewById(R.id.etSearchField);
        etSearchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i){
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        if(etSearchField.getText().toString().equals("") || etSearchField.getText().toString().isEmpty()){
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("Alerta");
                            alertDialog.setMessage("No debe haber campos vacíos");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else {
                            progressDialog = new ProgressDialog(MainActivity.this);
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );
                            textobusqueda = etSearchField.getText().toString();
                            textobusqueda = textobusqueda.replace(" ","%20");
                            Buscar(textobusqueda);
                        }
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    public void Buscar(String texto){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest sr = new StringRequest(Request.Method.GET, Rutas.busqueda+texto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data = new JSONObject(response);

                    Log.d("RESPUESTAML", data.toString());

                    String busqueda = data.getString("query");
                    Log.d("BUSQUEDAML", busqueda);
                    JSONObject paging = data.getJSONObject("paging");
                    int resultados = paging.getInt("total");

                    progressDialog.dismiss();
                   Intent intent = new Intent(MainActivity.this, ListaBusqueda.class);
                   intent.putExtra("busqueda", busqueda);
                   intent.putExtra("totalresult", resultados);
                   startActivity(intent);
                   finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;

                if(networkResponse != null && networkResponse.data != null)
                {
                    String code = Integer.toString(error.networkResponse.statusCode); /*el codigo de error*/
                    String error_message = "", action_string = "";
                    try {

                        String responseBody = new String(error.networkResponse.data, "utf-8");

                        JSONObject data = new JSONObject(responseBody); /*la informacion convertida a JSON */
                        JSONObject errors = data.getJSONObject("errors");

                        error_message = errors.getString("msg"); /*el mensaje de error*/
                        Log.d("Mensaje error", error_message.toString());

                    } catch (JSONException e) {
                        Log.d("Mensaje error", e.toString());
                    } catch (UnsupportedEncodingException errorr) {
                        Log.d("Mensaje error", errorr.toString());
                    }

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alerta");
                    alertDialog.setMessage(error_message);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    progressDialog.dismiss();
                    alertDialog.show();
                      Toast.makeText(MainActivity.this, error_message, Toast.LENGTH_SHORT).show();

                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alerta");
                    alertDialog.setMessage("No hay conexión a internet");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    progressDialog.dismiss();
                    alertDialog.show();
                     Toast.makeText(MainActivity.this, "Sin internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        queue.add(sr);
    }
}