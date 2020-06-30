package com.android.appmadrid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegistroFragment extends Fragment {
    private TextView fragment_principal;
    private ConexionFragmentsInicio mListener;
    private EditText campo_nombre;
    private EditText campo_pass;
    private EditText campo_correo;
    private Button boton_registro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        //Boton cancelar
        fragment_principal = (TextView) view.findViewById(R.id.textView_cancelar_registro);
        fragment_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentPrincipal = new PrincipalInicioRegistro();
                mListener.moveToFragment(fragmentPrincipal);
            }
        });;

        //boton registrar usuario
        campo_nombre = (EditText) view.findViewById(R.id.editText_usuario_registro);
        campo_pass = (EditText) view.findViewById(R.id.editText_contrasena_registro);
        campo_correo = (EditText) view.findViewById(R.id.editText_mail_registro);

        boton_registro = (Button) view.findViewById(R.id.button_registro);
        boton_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = campo_nombre.getText().toString();
                String pass = campo_pass.getText().toString();
                String correo = campo_correo.getText().toString();

                Modelo modelo = Modelo.getModelo(getActivity());
                modelo.insertarUsuario(nombre,correo,pass);

                //Vuelta a la activity inicial
               Fragment fragmentPrincipal = new PrincipalInicioRegistro();
               mListener.moveToFragment(fragmentPrincipal);
               Toast.makeText(getActivity(), "Registro realizado", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConexionFragmentsInicio) {
            mListener = (ConexionFragmentsInicio) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " hay que implementar el ConexionFragmentsInicio");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
