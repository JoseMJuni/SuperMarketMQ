package com.example.juni.supermarket;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.os.Process;
import android.widget.ListView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SuscriptionService extends Service {

    //https://medium.com/@anant.rao07/android-bound-services-f1cceb2f1f3e
    /*
    La idea es proveer acceso al servicio a otros componentes (clientes). Normalmente serán actividades.
    Para ello se usa la interfaz IBinder que retorna la instancia del servicio hacia el elemento que desea interactuar con él.
     */
    private static final String TAG = SuscriptionService.class.getSimpleName();




    private SuscriptionHandler mServiceHandler;



    public static void startSuscriber(Context clientContext) {
        Intent requestIntent = new Intent(clientContext, SuscriptionService.class);
        clientContext.startService(requestIntent);
    }

    @Nullable
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

        mServiceHandler = new SuscriptionHandler(backgroundThread.getLooper());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int idProcess) {
        Log.i(TAG, "Received start id " + idProcess + ": " + intent);
        Log.i(TAG, "Current thread is " + Thread.currentThread().getName());

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

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    //Creamos un hilo a parte ya que podemos sufrir ANR  (Aplication Not Responding) si se hace en el hilo principal
    private final class SuscriptionHandler extends Handler {
        Controlador controlador = Controlador.getControlador();
        private Connection connection;
        public  Channel channel;
        private ConnectionFactory factory = new ConnectionFactory();
        private static final String EXCHANGE_NAME = "Exchange_SuperMercado";
        Consumer  consumer;
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager;


        SuscriptionHandler(Looper looper) {
            super(looper);
            factory.setHost("IP");
            factory.setUsername("master");
            factory.setPassword("master");
            factory.setPort(5672);

            mBuilder =new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle("SuperMarket")
                    .setContentText("Tienes mensajes nuevos")
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        }

        private void showNotification(int target) {
            mBuilder.setContentText(
                    getString(R.string.notif_content_result, target)
            );
            mNotificationManager.notify(
                    1111,
                    mBuilder.build()
            );
        }


        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Current thread is " + Thread.currentThread().getName());
            Intent request = (Intent) msg.obj;
            readMessaage();

            stopSelfResult(msg.arg1);

        }


        private void readMessaage(){
                String tag ="";
                try {
                    connection = factory.newConnection();
                    channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
                    String queueName = channel.queueDeclare().getQueue();
                    while (controlador.getIniciadoServicio()) {
                        if (controlador.getCurrentUser().getListaIntereses().size() > 0) {

                            for (String bindingKey : controlador.getCurrentUser().getListaIntereses()) {
                                Log.d("Binding key ", bindingKey);
                                channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
                            }
                        }

                        consumer = new DefaultConsumer(channel) {

                            @Override
                            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                                String message = new String(body, "UTF-8");
                                if(!controlador.getIniciadoServicio()) consumer.handleCancel(consumerTag);
                                Log.d("Recibo", envelope.getRoutingKey() + ":" + message + "'");
                                controlador.addMensaje(envelope.getRoutingKey(), message);

                                showNotification(controlador.getCurrentUser().getMensajesSinLeer());

                            }
                        };
                        //Si no hacemos estos dos pasos no podemos parar de consumir
                        if(tag != "" && !controlador.getIniciadoServicio()){
                            channel.basicCancel(tag);
                            channel.abort();
                        }

                        else{
                            tag = channel.basicConsume(queueName, true, consumer);

                        }



                    }




                }catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }

        }




    }




}
