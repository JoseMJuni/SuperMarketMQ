package com.example.juni.supermarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class CanalesActivity extends AppCompatActivity {
    Controlador controlador = Controlador.getControlador();

    ImageButton buttonSetting;
    ImageButton buttonMessage;
    ImageButton buttonMain;
    CanalAdapter canalAdapter;
    MensajeAdapter mensajeAdapter;
    ListView canalView;
    boolean primera = true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_canales);
        overridePendingTransition(0,0);


        buttonSetting = (ImageButton) findViewById(R.id.buttonSetting);
        buttonMessage = (ImageButton) findViewById(R.id.buttonMail);
        buttonMain    = (ImageButton) findViewById(R.id.buttonMain);
        buttonMessage.setImageResource(R.drawable.iconomensajeseleccion);




        mostrarListaCanales();
        refreshCanales();

    }

    private void mostrarListaCanales(){
        canalView = (ListView) findViewById(R.id.listChat);
        canalAdapter = new CanalAdapter(this,R.id.listChat,controlador.getCurrentUser().getListaCanales() );
        //canalAdapter = new CanalAdapter(this,R.id.listChat);
        canalView.setAdapter(canalAdapter);

      /* for (CanalVisual c : controlador.getCurrentUser().getListaCanales()) {
            canalAdapter.add(c);
            canalView.setSelection(canalView.getCount() - 1);
       }*/

        ListView listChat = ((ListView)findViewById(R.id.listChat));
        listChat.setSelectionAfterHeaderView();
        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,long arg3)
            {
                mostrarChatCanal(v, position);

            }
        });


        /*//Listener para el scroll del ListView
        listChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    Log.d("SCROLLING DOWN","TRUE");
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    Log.d("SCROLLING UP","TRUE");
                }
                mLastFirstVisibleItem=firstVisibleItem;

            }
        });*/
    }

    private void mostrarChatCanal(View v, int position){
        CanalVisual c = (CanalVisual) canalAdapter.getItem(position);
        controlador.setCurretCanal(c);
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        //finish();
    }

    public void onClickedMain(View v){
        buttonMessage.setImageResource(R.drawable.iconomensaje);
        buttonSetting.setImageResource(R.drawable.iconosetting2);
        buttonMain.setImageResource(R.drawable.iconomainseleccion);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

    public void onClickedMessage(View v){

    }


    public void refreshCanales(){
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
                        canalAdapter.refreshEvents(controlador.getCurrentUser().getListaCanales());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }}




}
