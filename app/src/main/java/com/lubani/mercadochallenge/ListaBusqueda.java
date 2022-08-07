package com.lubani.mercadochallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

public class ListaBusqueda extends AppCompatActivity {

    Button btntest;
    EditText etSearchFieldList;
    TextView txtCantResults;
    ProgressDialog progressDialog;
    RecyclerView recyclerListResultados;
    List<ListBusqueda> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_busqueda);

        btntest = (Button) findViewById(R.id.btnTest);
        etSearchFieldList = (EditText)findViewById(R.id.etSearchFieldList);
        txtCantResults = (TextView) findViewById(R.id.txtCantResults);
        recyclerListResultados = (RecyclerView) findViewById(R.id.recyclerListResultados);

        String textobusqueda = getIntent().getStringExtra("busqueda");
        int totalresult = getIntent().getIntExtra("totalresult", 0);

        ListaBuscar(textobusqueda);

        etSearchFieldList.setText(textobusqueda);
        txtCantResults.setText(String.valueOf(totalresult)+" Resultados");

        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaBusqueda.this, DetalleBusqueda.class));
            }
        });
    }

    public void ListaBuscar(String texto){
        elements = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(ListaBusqueda.this);
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

                    JSONArray results = data.getJSONArray("results");

                    for(int i = 0; i < results.length(); i++){
                        JSONObject jObject = results.getJSONObject(i);
                        String id = jObject.getString("id");
                        String titulo = jObject.getString("title");
                        int precio = jObject.getInt("price");
                        String thumbnail = jObject.getString("thumbnail");

                        String condition = jObject.getString("condition");
                        String condicion = "";
                        if(condition.equals("new")){
                            condicion = "Nuevo";
                        }
                        else if(condition.equals("used")){
                            condicion = "Usado";
                        }

                        JSONObject shippingObj = jObject.getJSONObject("shipping");
                        boolean freeshipping = shippingObj.getBoolean("free_shipping");
                        String envio = "";
                        if(freeshipping){
                            envio = "Envío gratis";
                        }
                        elements.add(new ListBusqueda(id,titulo,precio,condicion,envio,thumbnail));
                    }

                    AdapterBusquedas adapterBusquedas = new AdapterBusquedas(elements, ListaBusqueda.this);
                    recyclerListResultados.setHasFixedSize(true);
                    recyclerListResultados.setLayoutManager(new LinearLayoutManager(ListaBusqueda.this));
                    recyclerListResultados.setAdapter(adapterBusquedas);


                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ListaBusqueda.this,
                            "mercadobd", null, Rutas.bd_version);
                    SQLiteDatabase bd = admin.getWritableDatabase();

                    bd.close();
                   // progressDialog.dismiss();


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

                    AlertDialog alertDialog = new AlertDialog.Builder(ListaBusqueda.this).create();
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
                    Toast.makeText(ListaBusqueda.this, error_message, Toast.LENGTH_SHORT).show();

                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(ListaBusqueda.this).create();
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
                    Toast.makeText(ListaBusqueda.this, "Sin internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        queue.add(sr);
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}