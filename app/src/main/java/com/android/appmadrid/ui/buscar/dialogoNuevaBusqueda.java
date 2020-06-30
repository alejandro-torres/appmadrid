package com.android.appmadrid.ui.buscar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;

import com.android.appmadrid.R;


public class dialogoNuevaBusqueda extends DialogFragment {
    AlertDialog.Builder builder;
    /*Modelo modelo=Modelo.getModelo(getActivity());
    private EditText busquedaTitulo;*/


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View dialogview=inflater.inflate(R.layout.dialogo_nueva_busqueda, null);
        builder.setView(dialogview);

        builder.setTitle("Búsqueda")
                .setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                       /* Paso de información de Dialog a Fragment sin acabar
                        busquedaTitulo= (EditText) dialogview.findViewById(R.id.editText_busquedaTitulo);
                        String titulo= busquedaTitulo.getText().toString();
                        List<Evento> hola= new ArrayList<>();
                        hola=modelo.buscarEventos(titulo);
                        Intent i= new Intent();
                        i.putExtra("sdf",hola);
                        Toast.makeText(getActivity(), "Texto "+ hola.size(), Toast.LENGTH_SHORT).show();*/

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });


        return builder.create();
    }
}
