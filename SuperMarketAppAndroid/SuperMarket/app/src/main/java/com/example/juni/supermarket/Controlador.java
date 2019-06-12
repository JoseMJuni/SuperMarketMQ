package com.example.juni.supermarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Controlador {
    private Usuario currentUser;
    private static final String EXCHANGE_NAME = "Exchange_SuperMercado";
    private static Controlador controlador;
    private boolean iniciadoServicio = false;
    private SuscriptionService suscriptionService = new SuscriptionService();
    private CanalVisual currentCanal;
    private static ConnectionFactory factory = new ConnectionFactory();

    public static Controlador getControlador() {
        if(controlador == null) {
            controlador = new Controlador();
        }
        return controlador;
    }

    public Usuario getCurrentUser(){
        return currentUser;
    }

    public void establecerUsuario(Usuario usuario){
        this.currentUser = usuario;
    }

    public void setCurretCanal(CanalVisual c){
        currentCanal = c;
    }

    public CanalVisual getCurrentCanal() {
        return currentCanal;
    }


    public void addInteres(String interes){
        if(currentUser != null){
            currentUser.addInteres(interes);
        }
    }

    public void addCanal(String canal){
        if(currentUser != null){
            currentUser.addCanal(canal);
        }
    }

    public void removeInteres(String interes){
        if(currentUser != null){
            currentUser.removeInteres(interes);
        }
    }

    public void removeCanal(String canal){
        if(currentUser != null){
            currentUser.removeCanal(canal);
        }
    }

    public void addMensaje(String ruta, String mensaje){
        if(currentUser!=null){
            currentUser.addMensaje(ruta, mensaje);
        }
    }

    public boolean getIniciadoServicio(){
        return iniciadoServicio;
    }

    public void iniciarLecturaRabbitMQ(Context clientContext){
        if(!iniciadoServicio){
            iniciadoServicio = true;
            suscriptionService.startSuscriber(clientContext);
        }


    }

   public void stopLecturaRabbitMQ(){
       iniciadoServicio = false;
       //suscriptionService.onDestroy();

   }

    public void enviarMensaje(String mensaje, Context clientContext){
        PublishService.startPublishMessage(mensaje, clientContext);

    }

    public void setLocalizacion(String localizacion){
        this.currentUser.setLocalizacion(localizacion);
    }

    public void usuarioDentroCentro(){
        currentUser.setDentroCentro(true);
    }

    public void usuarioFueraCentro(){
        currentUser.setDentroCentro(false);
    }
















}
