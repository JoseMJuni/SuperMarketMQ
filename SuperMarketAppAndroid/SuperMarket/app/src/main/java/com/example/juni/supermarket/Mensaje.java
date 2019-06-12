package com.example.juni.supermarket;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Mensaje {
    private String text; // cuerpo mensaje
    private String nombre;
    private String color;
    private String hora;
    private String canal;
    private boolean leido = false;

    public Mensaje(String text, String asunto ) {
        this.text = text;
        this.nombre = asunto;
        this.hora = String.format("%02d:%02d", (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY), (Calendar.getInstance()).get(Calendar.MINUTE));
        this.color = "#48b3ff";

    }


    public Mensaje(String text, String nombre, String color ) {
        this.text = text;
        this.nombre = nombre;
        this.color = color;
        this.hora = String.format("%02d:%02d", (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY), (Calendar.getInstance()).get(Calendar.MINUTE));
    }

    public String getText() {
        return text;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public String getHora(){
        return hora;
    }

    public String getCanal(){
        return canal;
    }

    public boolean isLeido(){ return leido;}

    public void marcarLeido() { this.leido = true;}

    public void marcarNoLeido() { this.leido = false;}






}
