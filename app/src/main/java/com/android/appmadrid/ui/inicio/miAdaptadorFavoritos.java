package com.android.appmadrid.ui.inicio;

import android.content.Context;

import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.appmadrid.Modelo;
import com.android.appmadrid.R;
import com.android.appmadrid.Usuario;
import com.android.appmadrid.ui.buscar.Evento;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class miAdaptadorFavoritos extends ArrayAdapter<Evento> {
    Context ctx;
    int layoutTemplate;
    List<Evento> favoritosList;

    public miAdaptadorFavoritos(@NonNull Context context, int resource, @NonNull List<Evento> objects) {
        super(context, resource, objects);
        this.ctx=context;
        this.layoutTemplate=resource;
        this.favoritosList=objects;
    }

    @NonNull
    @Override
    //Se lanza automáticamente como si fuera un bucle por cada elemento que se reciba en objects
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v= LayoutInflater.from(ctx).inflate(layoutTemplate,parent,false);
        final Modelo modelo= Modelo.getModelo(ctx);
        Usuario user=Usuario.construirUsuario();
        final String idUser=user.getIdUsuario();

        //Obtener la información del elemento de la lista que estoy iteranto en el momento
        Evento elementoActual=favoritosList.get(position);

        //Rescatar los elementos de la IU (interfaz usuario) de la plantilla
        TextView textViewTitulo=v.findViewById(R.id.textView_tituloFavorito);
        TextView textViewDistrito=v.findViewById(R.id.textView_distritoFavorito);
        TextView textViewFecha=v.findViewById(R.id.textView_fechaFavorito);
        TextView textViewPrecio=v.findViewById(R.id.textView_precioFavorito);
        TextView textViewFechaFin=v.findViewById(R.id.textView_fechaFinFavorito);
        ImageView imageViewCalendario=v.findViewById(R.id.imageView_calendarioFavorito);
        ImageView imageViewEliminar=v.findViewById(R.id.imageView_eliminarFavorito);

        //Hacer un set de la info del elemento Actual en los elementos de la IU
        textViewTitulo.setText(elementoActual.getTitulo());
        textViewDistrito.setText(elementoActual.getCalle());

        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Date fecha=elementoActual.getFechaInicio();
        String fechaInicio=dateFormat.format(fecha);
        textViewFecha.setText(fechaInicio);

        Date fechaFin=elementoActual.getFechaFin();
        String fechaFinal=dateFormat.format(fechaFin);
        textViewFechaFin.setText(fechaFinal);

        if(elementoActual.isGratuito()){
            textViewPrecio.setText("Gratuito");
        }else{
            textViewPrecio.setText("De pago");
        }

        imageViewCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCalendario = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE,favoritosList.get(position).getTitulo())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION,favoritosList.get(position).getCalle())
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,favoritosList.get(position).getFechaInicio().getTime())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,favoritosList.get(position).getFechaFin().getTime());
                ctx.startActivity(intentCalendario);
            }
        });

        imageViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelo.eliminarFav(idUser,favoritosList.get(position).getIdEvento());
                favoritosList.remove(position);
                notifyDataSetChanged();
            }
        });


        return v;
    }


}
