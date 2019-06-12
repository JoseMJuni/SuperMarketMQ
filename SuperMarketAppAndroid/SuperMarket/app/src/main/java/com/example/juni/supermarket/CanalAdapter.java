package com.example.juni.supermarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CanalAdapter extends ArrayAdapter<CanalVisual> {
    List<CanalVisual> listaCanales = new ArrayList<CanalVisual>();
    List<VisualizarCanal> listaHolders = new ArrayList<VisualizarCanal>();
    Context context;


    public CanalAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.context = context;


    }




    public CanalAdapter(Context context, int layoutResourceId, List<CanalVisual> events) {
        super(context, layoutResourceId, events);
        this.context = context;
        this.listaCanales = events;
        ordenarCanales();


    }

    private void ordenarCanales(){
        int i = 0;
        while(listaCanales.get(i).getText() != "Información Centro") { i=i+1;}
        CanalVisual c = listaCanales.get(i);
        listaCanales.remove(i);

        Comparator<CanalVisual> canalComparator = new Comparator<CanalVisual>() {
            @Override
            public int compare(CanalVisual c1, CanalVisual c2) {
                return c1.getText().compareTo(c2.getText());
            }
        };
        Collections.sort(listaCanales, canalComparator);
        listaCanales.add(0, c);
    }

    public void add(CanalVisual canalVisual) {
        this.listaCanales.add(canalVisual);
        notifyDataSetChanged(); // to render the list we need to notify
    }





    @Override
    public int getCount() {
        return listaCanales.size();
    }

    @Override
    public CanalVisual getItem(int i) {
        return listaCanales.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void refreshEvents(List<CanalVisual> events) {
        notifyDataSetChanged();
        ordenarCanales();
    }

    public void removeAll() {
        this.listaCanales  = new ArrayList<CanalVisual>();
        notifyDataSetChanged();

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VisualizarCanal holder = new VisualizarCanal();

        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        CanalVisual canalVisual = listaCanales.get(position);

        convertView = messageInflater.inflate(R.layout.fila_canal, null);
        holder.avatar = (View) convertView.findViewById(R.id.icono_canal);
        holder.titulo = (TextView) convertView.findViewById(R.id.nombre_canal);
        holder.ultimoMensaje = (TextView) convertView.findViewById(R.id.ultimo_mensaje);
        holder.hora = (TextView) convertView.findViewById(R.id.hora_ultimo_mensaje);
        holder.numMensajesNoLeidos = (TextView) convertView.findViewById(R.id.idNumNoLeido);
        convertView.setTag(holder);

        holder.titulo.setText(canalVisual.getText());
        holder.ultimoMensaje.setText(canalVisual.getUltimoMensaje());
        holder.hora.setText(canalVisual.getHora());

        //Compruebo si tengo mensajes sin leer y en el caso de que si los muestro y digo cuantos
        int numeroMensajes = 0;
        for(Mensaje m: canalVisual.getMensajes()){
            if(!m.isLeido()){
                numeroMensajes = numeroMensajes +1;
            }
        }

        if(numeroMensajes > 0){
            holder.numMensajesNoLeidos.setVisibility(View.VISIBLE);
            holder.numMensajesNoLeidos.setText(String.valueOf(numeroMensajes));
        }

        GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();

        return convertView;
    }




}


class VisualizarCanal {
    public View avatar;
    public TextView titulo;
    public TextView ultimoMensaje;
    public TextView hora;
    public TextView numMensajesNoLeidos;




}





