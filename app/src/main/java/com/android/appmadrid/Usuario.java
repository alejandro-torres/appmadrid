package com.android.appmadrid;

public class Usuario {
    private static String idUsuario,nombre,pass;

    private static Usuario usuario=null;

    private Usuario(){
    }

    public static String getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(String idUsuario) {
        Usuario.idUsuario = idUsuario;
    }

    public static Usuario construirUsuario(){
     if (usuario==null){
         usuario = new Usuario();
     }
     return usuario;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Usuario.nombre = nombre;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        Usuario.pass = pass;
    }
}



