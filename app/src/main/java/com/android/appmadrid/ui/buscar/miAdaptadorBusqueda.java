package com.android.appmadrid.ui.buscar;

import android.content.Context;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class miAdaptadorBusqueda extends ArrayAdapter<Evento> {
    Context ctx;
    int layoutTemplate;
    List<Evento> eventoList;

    //Constructor del adaptador
    public miAdaptadorBusqueda(@NonNull Context context, int resource, @NonNull List<Evento> objects) {
        super(context, resource, objects);
        this.ctx=context;
        this.layoutTemplate=resource;
        this.eventoList =objects;
    }


    @NonNull
    @Override
    //Se lanza automáticamente como si fuera un bucle por cada elemento que se reciba en objects
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v= LayoutInflater.from(ctx).inflate(layoutTemplate,parent,false);

        //Obtener la información del elemento de la lista que estoy iteranto en el momento
        Evento elementoActual= eventoList.get(position);

        //Rescatar los elementos de la IU (interfaz usuario) de la plantilla
        TextView textViewTitulo=v.findViewById(R.id.textView_tituloBusqueda);
        TextView textViewDistrito=v.findViewById(R.id.textView_distritoBusqueda);
        TextView textViewFecha=v.findViewById(R.id.textView_fechaBusqueda);
        TextView textViewPrecio=v.findViewById(R.id.textView_precioBusqueda);
        TextView textViewFechaFin=v.findViewById(R.id.textView_fechaFinBusqueda);
        ImageView imageViewFavoritoOff=v.findViewById(R.id.imageView_favoritoOff);
        ImageView imageViewFavoritoOn=v.findViewById(R.id.imageView_favoritoOn);

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

        final Modelo modelo= Modelo.getModelo(ctx);
        Usuario usuario=Usuario.construirUsuario();
        final String idUsuario=usuario.getIdUsuario();
        final String idEvento= elementoActual.getIdEvento();


        if(modelo.comprobarFavorito(idUsuario,idEvento)){
            imageViewFavoritoOn.setVisibility(View.VISIBLE);
            imageViewFavoritoOff.setVisibility(View.INVISIBLE);
        }else{
            imageViewFavoritoOff.setVisibility(View.VISIBLE);
            imageViewFavoritoOn.setVisibility(View.INVISIBLE);
        }

        imageViewFavoritoOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    modelo.eliminarFav(idUsuario,idEvento);
                    notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("==>","No insistas, no va ");
                }
            }
        });

        imageViewFavoritoOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    modelo.insertarFav(idUsuario,idEvento);
                    notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("==>","No va");
                }
            }
        });

        return v;

    }
}
