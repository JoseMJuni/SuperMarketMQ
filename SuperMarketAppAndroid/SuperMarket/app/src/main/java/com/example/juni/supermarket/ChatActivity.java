package com.example.juni.supermarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    ImageButton buttonSetting;
    ImageButton buttonMessage;
    ImageButton buttonMain;

    MensajeAdapter mensajeAdapter;
    ListView messagesView;
    CanalVisual c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        overridePendingTransition(0,0);


        buttonSetting = (ImageButton) findViewById(R.id.buttonSetting);
        buttonMessage = (ImageButton) findViewById(R.id.buttonMail);
        buttonMain    = (ImageButton) findViewById(R.id.buttonMain);
        buttonMessage.setImageResource(R.drawable.iconomensajeseleccion);


        c = Controlador.getControlador().getCurrentCanal();
        ((TextView)findViewById(R.id.tituloCanal)).setText(" < "+ c.getText());

        messagesView = (ListView)findViewById(R.id.listCanal);

        mensajeAdapter = new MensajeAdapter(this, R.id.listCanal, c.getMensajes());
        messagesView.setAdapter(mensajeAdapter);
        messagesView.setSelection(mensajeAdapter.getCount() - 1);
        refreshChat();
    }


    public void onClickedMain(View v){
        buttonMessage.setImageResource(R.drawable.iconomensaje);
        buttonSetting.setImageResource(R.drawable.iconosetting2);
        buttonMain.setImageResource(R.drawable.iconomainseleccion);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void onClickedMessage(View v){
        //Intent intent = new Intent(this, CanalesActivity.class);
        //startActivity(intent);
        finish();
    }

    public void onClickedSetting(View v){
        buttonMessage.setImageResource(R.drawable.iconomensaje);
        buttonSetting.setImageResource(R.drawable.iconosetting2seleccion);
        buttonMain.setImageResource(R.drawable.iconomain);

        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        finish();

    }


    public void refreshChat(){
        Updategui up = new Updategui(this, 1);

    }

    public class Updategui extends TimerTask {
        Activity context;
        Timer timer;

        public Updategui(Activity context, int seconds) {
            this.context = context;
            timer = new Timer();
            timer.schedule(this,
                    seconds * 1000,  // initial delay
                    seconds * 1000); // subsequent rate
        }

        @Override
        public void run() {
            if(context == null || context.isFinishing()) {
                this.cancel();
                return;
            }

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        int posiciones = messagesView.getFirstVisiblePosition()-messagesView.getFirstVisiblePosition();
                        for(int i=messagesView.getFirstVisiblePosition();i<=messagesView.getLastVisiblePosition();i++){
                            mensajeAdapter.getItem(i).marcarLeido();
                        }

                        if(mensajeAdapter.refreshEvents(c.getMensajes())) {

                            messagesView.setSelection(mensajeAdapter.getCount() - 1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }}




}