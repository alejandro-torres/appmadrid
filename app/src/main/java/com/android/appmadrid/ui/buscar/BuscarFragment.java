package com.android.appmadrid.ui.buscar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.android.appmadrid.Modelo;
import com.android.appmadrid.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BuscarFragment extends Fragment {

    ListView listaBusqueda;
    List<Evento> eventoList;

    public View onCreateView(@NonNull LayoutInflater inflater,
           ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        final Modelo modelo= Modelo.getModelo(getActivity());

        //Botón que llama a Dialogo Evento
        FloatingActionButton botonBuscar = (FloatingActionButton) view.findViewById(R.id.botonBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoNuevaBusqueda nuevaBusqueda=new dialogoNuevaBusqueda();
                nuevaBusqueda.show(getActivity().getSupportFragmentManager(),"Nueva búsqueda");
            }
        });

        //Array de objeto Evento
        eventoList =new ArrayList<>();
        eventoList=modelo.buscarEventos();

        final miAdaptadorBusqueda adaptadorBusqueda = new miAdaptadorBusqueda(this.getActivity(),
                R.layout.busqueda_item,
                eventoList);

        listaBusqueda= (ListView) view.findViewById(R.id.listViewBusqueda1);
        listaBusqueda.setAdapter(adaptadorBusqueda);
        adaptadorBusqueda.notifyDataSetChanged();

        return view;
    }
}
