package com.lubani.mercadochallenge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterBusquedas extends RecyclerView.Adapter<AdapterBusquedas.ViewHolder> {
    private List<ListBusqueda> nData;
    private LayoutInflater nInflater;
    private Context context;

    public AdapterBusquedas(List<ListBusqueda> itemList, Context context){
        this.nInflater = LayoutInflater.from(context);
        this.context = context;
        this.nData = itemList;
    }

    @NonNull
    @Override
    public AdapterBusquedas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = nInflater.inflate(R.layout.item_articulo, parent, false);
        return new AdapterBusquedas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBusquedas.ViewHolder holder, int position) {
        holder.bindData(nData.get(position));
    }

    @Override
    public int getItemCount() { return nData.size(); }

    public void setItems(List<ListBusqueda> items) { nData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        String urlthumbnail, titulo, precio, envio, condicion;
        TextView title, price, shipping, condition;
        ImageView imgThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitArticulo);
            price = itemView.findViewById(R.id.txtPriceArticulo);
            shipping = itemView.findViewById(R.id.txtTipoEnvio);
            condition = itemView.findViewById(R.id.txtCondicion);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetalleBusqueda.class);
                    i.putExtra("titulo", titulo);
                    i.putExtra("precio", precio);
                    i.putExtra("envio", envio);
                    i.putExtra("condicion", condicion);
                    i.putExtra("urlimg", urlthumbnail);
                    context.startActivity(i);
                }
            });
        }

        void bindData(final ListBusqueda item){
            urlthumbnail = item.getThumbnail();
            title.setText(item.getTitulo());
            titulo = item.getTitulo();
            envio = item.getShipping();
            condicion = item.getCondition();
            precio = String.valueOf(item.getPrice());
            String precio_final = String.valueOf(item.getPrice());
            price.setText("$"+precio_final);
            shipping.setText(item.getShipping());
            condition.setText(item.getCondition());
            Glide.with(context).load(urlthumbnail).into(imgThumbnail);
        }
    }


}
