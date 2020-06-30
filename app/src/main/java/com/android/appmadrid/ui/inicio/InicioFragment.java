package com.android.appmadrid.ui.inicio;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.android.appmadrid.Modelo;
import com.android.appmadrid.R;
import com.android.appmadrid.Usuario;
import com.android.appmadrid.ui.buscar.Evento;

import java.util.ArrayList;

import java.util.List;

public class InicioFragment extends Fragment {

    ListView listaFavoritos;
    List<Evento> favoritosList;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        final Modelo modelo= Modelo.getModelo(getActivity());
        Usuario user=Usuario.construirUsuario();
        final String idUser=user.getIdUsuario();

        favoritosList=new ArrayList<>();
        favoritosList=modelo.buscarEventosFavoritos(idUser);

        final miAdaptadorFavoritos adaptadorFavoritos = new miAdaptadorFavoritos(this.getActivity(),
                R.layout.favorito_item,
                favoritosList);

        listaFavoritos= (ListView) view.findViewById(R.id.listViewFavoritos);
        listaFavoritos.setAdapter(adaptadorFavoritos);


        return view;
    }
}
