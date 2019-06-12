package com.example.juni.supermarket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CanalVisual {
    private String text;
    private List<Mensaje> listaMensajes = new LinkedList<Mensaje>();

    public CanalVisual(String text) {
        this.text = text;

    }



    public String getText() {
        return text;
    }

    public String getUltimoMensaje() {
        if(listaMensajes.size() > 0){
            return listaMensajes.get(listaMensajes.size()-1).getText();
        }
        return "";
    }


    public String getHora(){
        if(listaMensajes.size() > 0) {
            return listaMensajes.get(listaMensajes.size() - 1).getHora();
        }
        return "";
    }

    public void addMensaje(Mensaje m){
        listaMensajes.add(m);

    }

    public List<Mensaje> getMensajes(){
        return new ArrayList<Mensaje>(listaMensajes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CanalVisual that = (CanalVisual) o;
        return this.text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Integer.getInteger(text);
    }
}
