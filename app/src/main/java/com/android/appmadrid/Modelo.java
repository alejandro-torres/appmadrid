package com.android.appmadrid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.appmadrid.ui.buscar.Evento;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Modelo extends SQLiteOpenHelper {

    private static Modelo modelo = null;

    private static final String NOMBRE_DB="app_db";
    private static final int VERSION_DB=2;

    private static final String NOMBRE_TABLA_USUARIOS="users";
    private static final String NOMBRE_TABLA_EVENTOS="events";
    private static final String NOMBRE_TABLA_FAVORITOS="fav";

    private static final String CREACION_TABLA_USUARIOS="CREATE TABLE "+NOMBRE_TABLA_USUARIOS+" (id_user INTEGER  PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT);";
    private static final String CREACION_TABLA_EVENTOS="CREATE TABLE "+NOMBRE_TABLA_EVENTOS+" (id_event INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, place TEXT, Dstart TEXT, Dend TEXT, price TEXT);";
    private static final String CREACION_TABLA_FAVORITOS="CREATE TABLE "+NOMBRE_TABLA_FAVORITOS+" (" +
            "id_user_fk INTEGER, id_event_fk INTEGER," +
            " PRIMARY KEY (id_user_fk, id_event_fk)," +
            " FOREIGN KEY (id_user_fk) REFERENCES users (id_user)," +
            " FOREIGN KEY (id_event_fk) REFERENCES events (id_event)" +
            ");";


    private Modelo(@Nullable Context context) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }

    public static Modelo getModelo(Context contexto){

        if (modelo == null){
            modelo = new Modelo(contexto);
        }
        return modelo;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREACION_TABLA_USUARIOS);
        db.execSQL(CREACION_TABLA_EVENTOS);
        db.execSQL(CREACION_TABLA_FAVORITOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //de momento no lo voy a usar esto es para actualizar la base de datos
        //a una nueva version
    }

    //===============Consultar Todos los usuarios============================
    public void consultarTodosLosUsuarios()
    {

        SQLiteDatabase db = modelo.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLA_USUARIOS+"",null);
        if (c.moveToFirst()){
            do {

                String column_0= c.getString(0);
                String column_1= c.getString(1);
                String column_2= c.getString(2);
                String column_3= c.getString(3);
                Log.d("registro_1",column_0+" "+column_1+" "+column_2+" "+column_3);

            }while (c.moveToNext());
        }
        db.close();
    }

    //===============Insertar Usuarios para lo de registrarse============================
    public void insertarUsuario(String nombre, String correo, String pass)
    {
        SQLiteDatabase db = modelo.getWritableDatabase();
        db.execSQL("INSERT INTO "+NOMBRE_TABLA_USUARIOS+" (name,email,password) VALUES ('"+nombre+"','"+correo+"','"+pass+"')");
        Log.d("==>","Usuario insertado");
        db.close();
    }

    //===============Consultar Usuario para el login============================
    public Boolean consultarUsuario(String nombre, String pass)
    {
        Boolean existe = null;

        SQLiteDatabase db = modelo.getReadableDatabase();
        //Cursor sirve para navegar por la base de datos
        Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLA_USUARIOS+" WHERE name='"+nombre+"' AND password='"+pass+"'",null);

        if (c.moveToFirst()){
            existe = true;
            String column_0= c.getString(0);
            String column_1= c.getString(1);
            String column_2= c.getString(2);
            String column_3= c.getString(3);
            Log.d("registro_1",column_0+" "+column_1+" "+column_2+" "+column_3);
        }else{
            existe = false;
            Log.d("==>","No existe");
        }


        db.close();
        return existe;
    }
    //===============obtener id usuario para crear objeto de tipo usuario=======================
    public String getIdUsuario(String nombre, String pass){
        String id="";
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id_user FROM "+NOMBRE_TABLA_USUARIOS+" WHERE name = '"+nombre+"' AND password = '"+pass+"' ",null);
        if (c.moveToFirst()){
            do {
                id= c.getString(0);
                Log.d("==>"," ID: "+id);
            }while (c.moveToNext());
        }
        db.close();
        return id;
    }

    //===============obtener mail del usuario=======================
    public String getMailUsuario(String id){
        String email="";
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT email FROM "+NOMBRE_TABLA_USUARIOS+" WHERE id_user = '"+id+"'",null);
        if (c.moveToFirst()){
            do {
                email= c.getString(0);
                Log.d("==>"," Mail: "+email);
            }while (c.moveToNext());
        }
        db.close();
        return email;
    }
    public String getNombreUsuario(String id){
        String user="";
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM "+NOMBRE_TABLA_USUARIOS+" WHERE id_user = '"+id+"'",null);
        if (c.moveToFirst()){
            do {
                user= c.getString(0);
                Log.d("==>"," User: "+user);
            }while (c.moveToNext());
        }
        db.close();
        return user;
    }
    //===============Comprobar si hay eventos en la base de datos y si no pues a rellenar del api Eventos============================
    public void comprobarSiHayEventos(){
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLA_EVENTOS+"",null);
        if (c.moveToFirst()==false) {
            Log.d("==>","No hay eventos asi que a rellenar");
            modelo.insertarEventos();
        }
        db.close();
    }


    //===============Insertar Eventos============================
    private void insertarEventos(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL url = null;

                try {

                    url =  new URL("https://datos.madrid.es/egob/catalogo/206974-0-agenda-eventos-culturales-100.json");

                    HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

                    if (conexion.getResponseCode() == 200){

                        Log.d("==>","conexion correcta");
                        InputStream stream = conexion.getInputStream();
                        InputStreamReader reader = new InputStreamReader(stream,"UTF-8");

                        String elString = IOUtils.toString(stream,"UTF-8");

                        //Numero de eventos EN EL API
                        int numeroEventos = (new JSONObject(elString).getJSONArray("@graph").length())-1;

                        //inserción en base de datos:
                        SQLiteDatabase db = modelo.getWritableDatabase();

                        String title = "-";
                        String place = "-";
                        String Dstart = "-";
                        String Dend = "-";
                        String price = "-";

                        if (numeroEventos >= 0){
                            String codigo_postal = "";
                            String localizacion = "";
                            for (int i = 0; i < numeroEventos; i++) {

                                JSONObject evento = new JSONObject(elString).getJSONArray("@graph").getJSONObject(i);

                                title = evento.getString("title");
                                Dstart = evento.getString("dtstart");
                                Dend = evento.getString("dtend");
                                price = String.valueOf(evento.getInt("free"));

                                try {
                                    //codigo_postal = evento.getJSONObject("address").getJSONObject("area").getString("postal-code");
                                    localizacion = evento.getJSONObject("address").getJSONObject("area").getString("street-address");
                                    //place = localizacion+" , "+codigo_postal;
                                    place = localizacion;

                                }catch (Exception e){
                                    place = "-";
                                }


                                try{
                                    db.execSQL("INSERT INTO "+NOMBRE_TABLA_EVENTOS+" (title,place,Dstart,Dend,price) VALUES ('"+title+"','"+place+"','"+Dstart+"', '"+Dend+"', '"+price+"')");
                                    Log.d("==>","Evento numero: "+i+" insertado :D");
                                }catch (Exception e){
                                    Log.d("==>","Algun listo habrá metido unas comillas en el api");
                                }

                            }
                        }else{
                            Log.d("==>","No hay nada para insertar");
                        }
                        db.close();
                        //============================

                    }else {
                        Log.d("==>","El codigo de estado no es 200 y eso es malo");
                        Log.d("==>","es malo porque el 200 es el codigo http de que todo esta bien");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //===============Consultar Eventos toda la tabla de eventos============================
    public void consultarEventos()
    {
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLA_EVENTOS+"",null);
        if (c.moveToFirst()){
            do {

                String column_0= c.getString(0);
                String column_1= c.getString(1);
                String column_2= c.getString(2);
                String column_3= c.getString(3);
                String column_4= c.getString(4);
                String column_5= c.getString(5);
                Log.d("registro_1",column_0+" "+column_1+" "+column_2+" "+column_3+" "+column_4+" "+column_5);
            }while (c.moveToNext());
        }
        db.close();
    }

    //===============Obtener Eventos en un array de objetos de tipo evento============================
    //=============Casi terminado===================================
    // aqui estaria bien hacer una sobrecarga de métodos para buscar con parametros o sin ellos
    public ArrayList<Evento> buscarEventos(){
        ArrayList<Evento> listaDeEventos = new ArrayList<>();
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLA_EVENTOS+"",null);
        if (c.moveToFirst()){
            do {
                String eventos_id= c.getString(0);
                String eventos_title= c.getString(1);
                String eventos_place= c.getString(2);
                String eventos_Dstart= c.getString(3);
                String eventos_Dend= c.getString(4);
                String eventos_price= c.getString(5);



                String[] fecha_inicio = eventos_Dstart.split("-");
                int yearO = 0;
                int monthO = 0;
                int dayO = 0;
                for (int i = 0; i < fecha_inicio.length; i++) {
                            yearO = Integer.parseInt(fecha_inicio[0]);
                            monthO = Integer.parseInt(fecha_inicio[1]);
                            String[] day = fecha_inicio[2].split(" ");
                            dayO=Integer.parseInt(day[0]);
                }

                String[] fecha_final = eventos_Dend.split("-");
                int yearF = 0;
                int monthF = 0;
                int dayF = 0;
                for (int i = 0; i < fecha_final.length; i++) {
                    yearF = Integer.parseInt(fecha_final[0]);
                    monthF = Integer.parseInt(fecha_final[1]);
                    String[] day = fecha_final[2].split(" ");
                    dayF=Integer.parseInt(day[0]);
                }

                Boolean gratuito = false;
                if (eventos_price.equals("0")){
                    gratuito = false;
                }else if( eventos_price.equals("1")){
                    gratuito =  true;
                }

                Evento objetoEvento = new Evento(eventos_id,eventos_title,eventos_place,yearO,monthO,dayO,yearF,monthF,dayF,gratuito);

                listaDeEventos.add(objetoEvento);

            }while (c.moveToNext());
        }

        db.close();
        return listaDeEventos;
    }

    //===============Obtener Eventos Favoritos en un array de objetos de tipo evento============================
    public ArrayList<Evento> buscarEventosFavoritos(String userId){
        ArrayList<Evento> listaDeEventos = new ArrayList<>();
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM fav INNER JOIN events ON id_event_fk=id_event WHERE id_user_fk='"+userId+"'",null);
        if (c.moveToFirst()){
            do {
                String eventos_id= c.getString(2);
                String eventos_title= c.getString(3);
                String eventos_place= c.getString(4);
                String eventos_Dstart= c.getString(5);
                String eventos_Dend= c.getString(6);
                String eventos_price= c.getString(7);



                String[] fecha_inicio = eventos_Dstart.split("-");
                int yearO = 0;
                int monthO = 0;
                int dayO = 0;
                for (int i = 0; i < fecha_inicio.length; i++) {
                    yearO = Integer.parseInt(fecha_inicio[0]);
                    monthO = Integer.parseInt(fecha_inicio[1]);
                    String[] day = fecha_inicio[2].split(" ");
                    dayO=Integer.parseInt(day[0]);
                }

                String[] fecha_final = eventos_Dend.split("-");
                int yearF = 0;
                int monthF = 0;
                int dayF = 0;
                for (int i = 0; i < fecha_final.length; i++) {
                    yearF = Integer.parseInt(fecha_final[0]);
                    monthF = Integer.parseInt(fecha_final[1]);
                    String[] day = fecha_final[2].split(" ");
                    dayF=Integer.parseInt(day[0]);
                }

                Boolean gratuito = false;
                if (eventos_price.equals("0")){
                    gratuito = false;
                }else if( eventos_price.equals("1")){
                    gratuito =  true;
                }

                Evento objetoEvento = new Evento(eventos_id,eventos_title,eventos_place,yearO,monthO,dayO,yearF,monthF,dayF,gratuito);

                listaDeEventos.add(objetoEvento);

            }while (c.moveToNext());
        }

        db.close();
        return listaDeEventos;
    }

    //=====Buscar con un parámetro====================
    public ArrayList<Evento> buscarEventos(String titulo){
        ArrayList<Evento> listaDeEventos = new ArrayList<>();
        SQLiteDatabase db = modelo.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+NOMBRE_TABLA_EVENTOS+" WHERE title = '"+titulo+"'",null);
        if (c.moveToFirst()){
            do {
                String eventos_id= c.getString(2);
                String eventos_title= c.getString(3);
                String eventos_place= c.getString(4);
                String eventos_Dstart= c.getString(5);
                String eventos_Dend= c.getString(6);
                String eventos_price= c.getString(7);



                String[] fecha_inicio = eventos_Dstart.split("-");
                int yearO = 0;
                int monthO = 0;
                int dayO = 0;
                for (int i = 0; i < fecha_inicio.length; i++) {
                    yearO = Integer.parseInt(fecha_inicio[0]);
                    monthO = Integer.parseInt(fecha_inicio[1]);
                    String[] day = fecha_inicio[2].split(" ");
                    dayO=Integer.parseInt(day[0]);
                }

                String[] fecha_final = eventos_Dend.split("-");
                int yearF = 0;
                int monthF = 0;
                int dayF = 0;
                for (int i = 0; i < fecha_final.length; i++) {
                    yearF = Integer.parseInt(fecha_final[0]);
                    monthF = Integer.parseInt(fecha_final[1]);
                    String[] day = fecha_final[2].split(" ");
                    dayF=Integer.parseInt(day[0]);
                }

                Boolean gratuito = false;
                if (eventos_price.equals("0")){
                    gratuito = false;
                }else if( eventos_price.equals("1")){
                    gratuito =  true;
                }

                Evento objetoEvento = new Evento(eventos_id,eventos_title,eventos_place,yearO,monthO,dayO,yearF,monthF,dayF,gratuito);

                listaDeEventos.add(objetoEvento);

            }while (c.moveToNext());
        }

        db.close();
        return listaDeEventos;
    }
    //===========================================================
    //===========================================================

    //=============Modificar usuario=============================
    //============LOS MÉTODOS DE LA OPCIÓN DE CUENTA!!!==========
    public void modificarNombreUsuario(String user, String id){
        SQLiteDatabase db = modelo.getReadableDatabase();
        db.execSQL("UPDATE "+NOMBRE_TABLA_USUARIOS+" SET name = '"+user+"' WHERE id_user = '"+id+"'");
        db.close();
        Log.d("==>","Nombre modificado");
    }
    public void modificarEmailUsuario(String email, String id){
        SQLiteDatabase db = modelo.getReadableDatabase();
        db.execSQL("UPDATE "+NOMBRE_TABLA_USUARIOS+" SET email = '"+email+"' WHERE id_user = '"+id+"'");
        db.close();
        Log.d("==>","Email modificado");
    }
    public void modificarPassUsuario(String pass, String id){
        SQLiteDatabase db = modelo.getReadableDatabase();
        db.execSQL("UPDATE "+NOMBRE_TABLA_USUARIOS+" SET password = '"+pass+"' WHERE id_user = '"+id+"'");
        db.close();
        Log.d("==>","Pass modificado");
    }

    //==================insertar Favoritos======================
    public void insertarFav(String userID, String eventID){
        SQLiteDatabase db = modelo.getWritableDatabase();
        db.execSQL("INSERT INTO "+NOMBRE_TABLA_FAVORITOS+" (id_user_fk, id_event_fk) VALUES ('"+userID+"','"+eventID+"')");
        Log.d("==>","Evento insertado como favorito");
        db.close();
    }
    //================eliminar Favorito=========================
    public void eliminarFav(String userID, String eventID){
        SQLiteDatabase db = modelo.getWritableDatabase();
        db.execSQL("DELETE FROM "+NOMBRE_TABLA_FAVORITOS+" WHERE id_user_fk = '"+userID+"' AND id_event_fk = '"+eventID+"' ");
        Log.d("==>","Evento eliminado de favorito");
        db.close();
    }

    //==========Método para saber si un evento es favorito=======
    public Boolean comprobarFavorito(String userID, String eventID){
        Boolean favorito;
        SQLiteDatabase db = modelo.getReadableDatabase();
        String query = "SELECT id_event_fk, id_user_fk FROM fav WHERE id_user_fk = '"+userID+"' AND id_event_fk = '"+eventID+"' ";
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()){
            favorito = true;
        }else{
            favorito = false;
        }
        return favorito;
    }
}
