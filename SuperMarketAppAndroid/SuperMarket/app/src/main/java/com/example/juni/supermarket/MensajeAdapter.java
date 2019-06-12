package com.example.juni.supermarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MensajeAdapter extends ArrayAdapter<Mensaje> {
    List<Mensaje> listaMensaje = new ArrayList<Mensaje>();
    Context context;

    public MensajeAdapter(Context context, int layoutResourceId, List<Mensaje> events) {
        super(context, layoutResourceId, events);
        this.context = context;
        this.listaMensaje = events;
    }

    public void add(Mensaje message) {
        this.listaMensaje.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    public boolean refreshEvents(List<Mensaje> events) {
        if(events.size() > this.listaMensaje.size()){
            this.listaMensaje.clear();
            this.listaMensaje.addAll(events);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return listaMensaje.size();
    }

    @Override
    public Mensaje getItem(int i) {
        return listaMensaje.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VisualizarMensaje holder = new VisualizarMensaje();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Mensaje mensaje= listaMensaje.get(position);

        convertView = messageInflater.inflate(R.layout.mensajes_canal, null);
        holder.avatar = (View) convertView.findViewById(R.id.avatar);
        holder.nombre = (TextView) convertView.findViewById(R.id.name);
        holder.cuerpoMensaje = (TextView) convertView.findViewById(R.id.message_body);
        holder.hora = (TextView) convertView.findViewById(R.id.hora);
        convertView.setTag(holder);

        holder.nombre.setText(mensaje.getNombre());
        holder.cuerpoMensaje.setText(mensaje.getText());
        holder.hora.setText(mensaje.getHora());
        GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
        drawable.setColor(Color.parseColor(mensaje.getColor()));


        return convertView;
    }
}

class VisualizarMensaje {
    public View avatar;
    public TextView nombre;
    public TextView cuerpoMensaje;
    public TextView hora;
}


