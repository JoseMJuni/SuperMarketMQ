package com.example.juni.supermarket;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class MainActivity extends AppCompatActivity  {
    Controlador controlador = Controlador.getControlador();
    ImageButton buttonSetting;
    ImageButton buttonMessage;
    ImageButton buttonMain;
    TextView posicionActual;

    private ListView mainListView;
    private Boolean vistaPrincipal = true;

    private static final String EXCHANGE_NAMESTRING = "Exchange_SuperMercado";



    Intent intent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        overridePendingTransition(0,0);


        buttonSetting = (ImageButton) findViewById(R.id.buttonSetting);
        buttonMessage = (ImageButton) findViewById(R.id.buttonMail);
        buttonMain    = (ImageButton) findViewById(R.id.buttonMain);
        posicionActual   = (TextView) findViewById(R.id.posicionActual);
        buttonMain.setImageResource(R.drawable.iconomainseleccion);


        //Si no se ha creado un usuario anteriormente, lo creamos.
        if(controlador.getCurrentUser() == null){
            Usuario usuario = new Usuario(UUID.randomUUID().toString());
            Controlador.getControlador().establecerUsuario(usuario);
        }
        else if(controlador.getCurrentUser().isDentroCentro()){
            posicionActual.setText("Posicion Actual: "+controlador.getCurrentUser().getLocalizacion());
        }

        if(controlador.getCurrentUser().isDentroCentro()) mostrarNavegacion();
        else ocultarNavegacion();


    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = new Intent(this , SuscriptionService.class);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickedMain(View v){

    }

    public void onClickedMessage(View v){
        buttonMessage.setImageResource(R.drawable.iconomensajeseleccion);
        buttonSetting.setImageResource(R.drawable.iconosetting2);
        buttonMain.setImageResource(R.drawable.iconomain);
        Intent intent = new Intent(MainActivity.this, CanalesActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickedSetting(View v){
        buttonMessage.setImageResource(R.drawable.iconomensaje);
        buttonSetting.setImageResource(R.drawable.iconosetting2seleccion);
        buttonMain.setImageResource(R.drawable.iconomain);
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickEntrarCentro(View v){
        controlador.usuarioDentroCentro();//Indicamos que el usuario esta dentro del centro comercial.
        controlador.iniciarLecturaRabbitMQ(MainActivity.this);
        mostrarNavegacion();
        posicionActual.setText("Posicion Actual: Hall");
        controlador.enviarMensaje("Hall "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()), MainActivity.this);
        controlador.setLocalizacion("Hall");

    }

    public void onClickCine(View v){
        posicionActual.setText("Posicion Actual: Cine");
        if(controlador.getCurrentUser().getLocalizacion() != "Cine"){
            controlador.enviarMensaje("Cine "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()),MainActivity.this);
            controlador.setLocalizacion("Cine");
        }
    }

    public void onClickReposteria(View v){
        posicionActual.setText("Posicion Actual: Reposteria");
        if(controlador.getCurrentUser().getLocalizacion() != "Reposteria"){
            controlador.enviarMensaje("Reposteria "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()),MainActivity.this);
            controlador.setLocalizacion("Reposteria");
        }
    }

    public void onClickFruteria(View v){
        posicionActual.setText("Posicion Actual: Fruteria");
        if(controlador.getCurrentUser().getLocalizacion() != "Fruteria"){
            controlador.enviarMensaje("Fruteria "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()), MainActivity.this);
            controlador.setLocalizacion("Fruteria");
        }
    }

    public void onClickCharcuteria(View v){
        posicionActual.setText("Posicion Actual: Charcuteria");
        if(controlador.getCurrentUser().getLocalizacion() != "Charcuteria"){
            controlador.enviarMensaje("Charcuteria "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()),MainActivity.this);
            controlador.setLocalizacion("Charcuteria");
        }
    }

    public void onClickHogar(View v){
        posicionActual.setText("Posicion Actual: Productos del hogar");
        if(controlador.getCurrentUser().getLocalizacion() != "Productos Hogar"){
            controlador.enviarMensaje("Hogar "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()),MainActivity.this);
            controlador.setLocalizacion("Productos Hogar");
        }
    }

    public void onClickPescaderia(View v){
        posicionActual.setText("Posicion Actual: Pescaderia");
        if(controlador.getCurrentUser().getLocalizacion() != "Pescaderia"){
            controlador.enviarMensaje("Pescaderia "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()), MainActivity.this);
            controlador.setLocalizacion("Pescaderia");
        }
    }

    public void onClickSalir(View v){
        controlador.usuarioFueraCentro();//Indicamos que el usuario esta dentro del centro comercial.
        controlador.stopLecturaRabbitMQ();
        ocultarNavegacion();
        if(controlador.getCurrentUser().getLocalizacion() != "Fuera"){
            controlador.enviarMensaje("disconnect "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",", controlador.getCurrentUser().getListaIntereses()), MainActivity.this);
            controlador.setLocalizacion("Fuera");
        }
    }

    private void mostrarNavegacion(){
        ((Button) findViewById(R.id.buttonEntrar)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.buttonCine)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonCharcuteria)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonFruteria)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonHogar)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonPescaderia)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonReposteria)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonSalir)).setVisibility(View.VISIBLE);
        posicionActual.setVisibility(View.VISIBLE);
    }

    private void ocultarNavegacion(){
        ((Button) findViewById(R.id.buttonEntrar)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonCine)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.buttonCharcuteria)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.buttonFruteria)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.buttonHogar)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.buttonPescaderia)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.buttonReposteria)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.buttonSalir)).setVisibility(View.GONE);
        posicionActual.setVisibility(View.GONE);

    }




}
