package com.android.appmadrid.ui.cuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.appmadrid.Modelo;
import com.android.appmadrid.R;
import com.android.appmadrid.Usuario;

public class CuentaFragment extends Fragment {

    TextView cambioUsuario;
    TextView cambioPassword;
    TextView cambioMail;
    TextView nombreUsuario;
    TextView mailUsuario;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);
        Modelo modelo=Modelo.getModelo(getActivity());
        Usuario user= Usuario.construirUsuario();
        String userId=user.getIdUsuario();

        nombreUsuario=(TextView) view.findViewById(R.id.textView_nombreUsuarioCuenta);
        nombreUsuario.setText(modelo.getNombreUsuario(userId));

        mailUsuario=(TextView) view.findViewById(R.id.textView_tuMailCuenta);
        mailUsuario.setText(modelo.getMailUsuario(userId));

        cambioUsuario= (TextView) view.findViewById(R.id.textView_cambioUsuario);
        cambioPassword= (TextView) view.findViewById(R.id.textView_cambioPassword);
        cambioMail= (TextView) view.findViewById(R.id.textView_cambioMail);

        cambioUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoCambioUsuario cambioUsuario= new dialogoCambioUsuario();
                cambioUsuario.show(getActivity().getSupportFragmentManager(),"Cambio de usuario");
            }
        });

        cambioPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoCambioPassword cambioPassword= new dialogoCambioPassword();
                cambioPassword.show(getActivity().getSupportFragmentManager(),"Cambio de constraseña");
            }
        });

        cambioMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoCambioMail cambioMail= new dialogoCambioMail();
                cambioMail.show(getActivity().getSupportFragmentManager(),"Cambio de mail");
            }
        });

        return view;
    }
}
