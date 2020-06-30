package com.android.appmadrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ConexionFragmentsInicio{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = new PrincipalInicioRegistro();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

}
