package com.android.appmadrid.ui.buscar;

import java.util.Date;
import java.util.GregorianCalendar;

public class Evento{
    private String idEvento,titulo, calle;
    private Date fechaInicio,fechaFin;
    private boolean gratuito;

    public Evento(String idEvento, String titulo, String calle, int agnoInicio, int mesInicio, int diaInicio, int agnoFin, int mesFin, int diaFin, boolean gratuito) {
        this.idEvento=idEvento;
        this.titulo = titulo;
        this.calle = calle;

        GregorianCalendar fechaInicio=new GregorianCalendar(agnoInicio,mesInicio-1,diaInicio);
        this.fechaInicio=fechaInicio.getTime();

        GregorianCalendar fechaFin=new GregorianCalendar(agnoFin,mesFin-1,diaFin);
        this.fechaFin=fechaFin.getTime();

        this.gratuito = gratuito;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String distrito) {
        this.calle = distrito;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public boolean isGratuito() {
        return gratuito;
    }

    public void setGratuito(boolean gratuito) {
        this.gratuito = gratuito;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Evento(){
    }
}
