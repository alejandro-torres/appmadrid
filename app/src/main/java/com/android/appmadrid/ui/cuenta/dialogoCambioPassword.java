package com.android.appmadrid.ui.cuenta;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.appmadrid.Modelo;
import com.android.appmadrid.R;
import com.android.appmadrid.Usuario;


public class dialogoCambioPassword extends DialogFragment {

    AlertDialog.Builder builder;
    Modelo modelo = Modelo.getModelo(getActivity());
    Usuario usuario=Usuario.construirUsuario();
    String idUsuario=usuario.getIdUsuario();


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = requireActivity().getLayoutInflater();


        final View dialogview=inflater.inflate(R.layout.dialogo_cambio_password, null);
        builder.setView(dialogview);

        builder.setTitle("Cambio de contraseña")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Cogemos los TextView de la vista y sacamos lo que ha introducido el usuario
                        TextView usuario=(TextView) dialogview.findViewById(R.id.editText_usuarioCambioPass);
                        TextView pass=(TextView) dialogview.findViewById(R.id.editText_passActual);
                        TextView passNueva=(TextView) dialogview.findViewById(R.id.editText_nuevaPass);
                        String inputUsuario=usuario.getText().toString();
                        String inputPass=pass.getText().toString();
                        String inputNuevaPass=passNueva.getText().toString();

                        if (modelo.consultarUsuario(inputUsuario,inputPass)){
                            modelo.modificarPassUsuario(inputNuevaPass,idUsuario);
                            Toast.makeText(getActivity(), "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Usuario o contraseña equivocada", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });


        return builder.create();
    }

}
