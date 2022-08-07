package com.lubani.mercadochallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetalleBusqueda extends AppCompatActivity {

    ImageButton imgSearch, imgAddFavorite;
    TextView txtCondicion, txtTitArticuloDetalle, txtPriceDetalle, txtTipoEnvioDetalle;
    ImageView imgArtDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_busqueda);

        imgSearch = (ImageButton) findViewById(R.id.imgSearch);
        txtCondicion = (TextView) findViewById(R.id.txtCondicion);
        txtTitArticuloDetalle = (TextView) findViewById(R.id.txtTitArticuloDetalle);
        imgArtDetalle = (ImageView) findViewById(R.id.imgArtDetalle);
        txtPriceDetalle = (TextView) findViewById(R.id.txtPriceDetalle);
        txtTipoEnvioDetalle = (TextView) findViewById(R.id.txtTipoEnvioDetalle);

        String titulo = getIntent().getStringExtra("titulo");
        String condicion = getIntent().getStringExtra("condicion");
        String precio = getIntent().getStringExtra("precio");
        String envio = getIntent().getStringExtra("envio");
        String urlimage = getIntent().getStringExtra("urlimg");

        txtTitArticuloDetalle.setText(titulo);
        txtCondicion.setText(condicion);
        txtPriceDetalle.setText("$"+precio);
        txtTipoEnvioDetalle.setText(envio);
        Glide.with(this).load(urlimage).into(imgArtDetalle);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetalleBusqueda.this, DetalleBusquedaBarra.class));
            }
        });
        imgAddFavorite = (ImageButton) findViewById(R.id.imgAddFavorite);
        imgAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgAddFavorite.isPressed()){
                    imgAddFavorite.setImageResource(R.drawable.heartfill);
                }
                else {
                    imgAddFavorite.setImageResource(R.drawable.heart);
                }
            }
        });

    }
}