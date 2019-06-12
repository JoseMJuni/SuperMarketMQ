package com.example.juni.supermarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Usuario {
    private String idUsuario;
    private ArrayList<String> listaIntereses = new ArrayList<String>();
    private HashMap<String, CanalVisual> canales = new HashMap<String, CanalVisual>();
    private String localizacion;
    private boolean isDentroCentro;

    public Usuario(String UNIQUE_ID) {
        this.idUsuario = UNIQUE_ID;
        this.isDentroCentro = false;
        //Por defecto siempre tendremos información centro
        CanalVisual canalCentro = new CanalVisual("Información Centro");
        this.localizacion = "Hall";
        canales.put("Informacion Centro", canalCentro);
        listaIntereses.add("informacion.centro");
        listaIntereses.add("cliente."+idUsuario+".centro");
    }


    public String getIdUsuario(){
        return idUsuario;
    }

    public boolean isDentroCentro(){
        return isDentroCentro;
    }

    public void setDentroCentro(boolean dentroCentro){
        this.isDentroCentro = dentroCentro;
    }

    public ArrayList<String> getListaIntereses(){
        return new ArrayList<String>(listaIntereses);
    }

    public ArrayList<CanalVisual> getListaCanales(){
        return new ArrayList<CanalVisual>(canales.values());    }

    public void addInteres(String interes){
        if(!listaIntereses.contains(interes))
            listaIntereses.add(interes);
    }

    public void addCanal(String canal){
        CanalVisual canalVisual = new CanalVisual(canal);
        if(!canales.containsKey(canal))
            canales.put(canal,canalVisual);

    }

    public void setLocalizacion(String localizacion){
        this.localizacion = localizacion;
    }

    public String getLocalizacion(){
        return localizacion;
    }

    public void removeInteres(String interes){
        listaIntereses.remove(interes);
    }

    public void removeCanal(String canal){
        canales.remove(canal);
    }


    /**
     *  Puede darse el caso que no sea para un canal en concreto y sea para varios.
     *  Hay que controlar que si es para varios, ver si es para la parte del supermercado, en ese caso iría para todos los que pertenezcan al supermercado
     */
    public void addMensaje(String ruta, String mensaje){
        List<CanalVisual> canalesLista = getCanalPerteneciente(ruta);
        for (CanalVisual c: canalesLista) {
            Mensaje m = new Mensaje(mensaje, c.getText());
            c.addMensaje(m);
        }

    }

    private List<CanalVisual> getCanalPerteneciente(String ruta) {
        List<CanalVisual> canalesLista = new ArrayList<CanalVisual>();
        List<String> rutaSplit = (Arrays.asList(ruta.split("[.]")));

        if(rutaSplit.contains("centro")){
            canalesLista.add(canales.get("Informacion Centro"));

        }
        else if(rutaSplit.contains("cine")){
            canalesLista.add(canales.get("Cine"));

        }
        else if (rutaSplit.contains("reposteria")){
            canalesLista.add(canales.get("Reposteria"));

        }
        else if(rutaSplit.contains("fruteria")){
            canalesLista.add(canales.get("Fruteria"));

        }
        else if(rutaSplit.contains("charcuteria")){
            canalesLista.add(canales.get("Charcuteria"));

        }
        else if(rutaSplit.contains("pescaderia")){
            canalesLista.add(canales.get("Pescaderia"));

        }
        else if(rutaSplit.contains("hogar")){
            canalesLista.add(canales.get("Productos hogar"));

        }

        return canalesLista;

    }


    public int getMensajesSinLeer(){
        int mensajesNoLeidos = 0;
        for (CanalVisual c: canales.values()) {
            for(Mensaje m: c.getMensajes()){
                if(!m.isLeido()) mensajesNoLeidos += 1;
            }
        }
        return mensajesNoLeidos;

    }


}
