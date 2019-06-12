package com.example.juni.supermarket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublishService extends Service {
    private static final String TAG = PublishService.class.getSimpleName();
    public  static final String CANONICAL_NAME = PublishService.class.getCanonicalName();
    public  static final String MESSAGE_TARGET = CANONICAL_NAME + ".MESSAGE";

    private PublisherHandler mServiceHandler;


    public PublishService() {
    }

    public static void startPublishMessage(String message, Context clientContext) {
        Intent requestIntent = new Intent(clientContext, PublishService.class);
        requestIntent.putExtra(MESSAGE_TARGET, message);
        clientContext.startService(requestIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Creating...");

        HandlerThread backgroundThread = new HandlerThread(
                "CounterThread",
                Process.THREAD_PRIORITY_BACKGROUND
        );
        backgroundThread.start();

        mServiceHandler = new PublisherHandler(backgroundThread.getLooper());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int idProcess) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = idProcess;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroying...");
    }


    //Creamos un hilo a parte ya que podemos sufrir ANR  (Aplication Not Responding) si se hace en el hilo principal
    private final class PublisherHandler extends Handler {
        Controlador controlador = Controlador.getControlador();
        private Connection connection;
        public Channel channel;
        private ConnectionFactory factory = new ConnectionFactory();
        private static final String EXCHANGE_NAME = "Exchange_SuperMercado";

        PublisherHandler(Looper looper) {
            super(looper);
            factory.setHost("IP");
            factory.setUsername("master");
            factory.setPassword("master");
            factory.setPort(5672);
        }


        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Current thread is " + Thread.currentThread().getName());
            Intent request = (Intent) msg.obj;

            String message = request.getStringExtra(MESSAGE_TARGET);
            Log.d("Enviamos:",message);
            sendMessage(message);
            // Stop the service using the startId
            stopSelf(msg.arg1);
        }

        private void sendMessage(final String mensaje){
            try {
                String routingKey = "cliente."+controlador.getCurrentUser().getIdUsuario()+".informacion.centro";
                connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.confirmSelect();
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, mensaje.getBytes("UTF-8"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }




    }






}
