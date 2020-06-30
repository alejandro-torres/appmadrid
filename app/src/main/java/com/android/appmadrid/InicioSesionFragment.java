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


public class InicioSesionFragment extends Fragment {
    private TextView fragment_principal;
    private Button fragment_app;
    private ConexionFragmentsInicio mListener;
    private EditText campo_nombre;
    private EditText campo_pass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
        fragment_principal = (TextView) view.findViewById(R.id.textView_cancelar_inicio);
        fragment_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentPrincipal = new PrincipalInicioRegistro();
                mListener.moveToFragment(fragmentPrincipal);
            }
        });;

        campo_nombre = (EditText) view.findViewById(R.id.editText_usuario_inicio);
        campo_pass = (EditText) view.findViewById(R.id.editText_contrasena_inicio);
        fragment_app= (Button) view.findViewById(R.id.button_inicio);
        fragment_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = campo_nombre.getText().toString();
                String pass = campo_pass.getText().toString();

                Modelo modelo = Modelo.getModelo(getActivity());

                if (modelo.consultarUsuario(nombre,pass)){

                    String id = modelo.getIdUsuario(nombre,pass);
                    Usuario user=Usuario.construirUsuario();
                    user.setIdUsuario(id);
                    user.setNombre(nombre);

                    Intent intent = new Intent (getActivity(), AplicacionActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getActivity(), "Campos incorrectos", Toast.LENGTH_SHORT).show();
                }


            }
        });;
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
