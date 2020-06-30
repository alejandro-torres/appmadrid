package com.android.appmadrid;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PrincipalInicioRegistro extends Fragment {
    private Button fragment_inicio;
    private Button fragment_registro;
    private ConexionFragmentsInicio mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal_inicio_registro, container, false);
        fragment_inicio = (Button) view.findViewById(R.id.button_ir_inicio);
        fragment_registro = (Button) view.findViewById(R.id.button_ir_registro);
        fragment_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentInicioSesion = new InicioSesionFragment();
                mListener.moveToFragment(fragmentInicioSesion);
            }
        });;
        fragment_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentRegistro = new RegistroFragment();
                mListener.moveToFragment(fragmentRegistro);
            }
        });;
        Modelo modelo=Modelo.getModelo(getActivity());
        modelo.comprobarSiHayEventos();
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
