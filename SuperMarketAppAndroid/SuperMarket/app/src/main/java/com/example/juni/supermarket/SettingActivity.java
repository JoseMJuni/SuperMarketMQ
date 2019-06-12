package com.example.juni.supermarket;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private ImageButton buttonMain, buttonMessage, buttonSetting;
    private CheckBox susOfertaCine, susOfertaCarnes, susOfertaRepos, susOfertaHogar, susOfertaPescaderia, susOfertaFruteria, susOfertaLimpieza;
    private LinkedList<CheckBox> suscripcionesBox = new LinkedList<CheckBox>();
    private Controlador controlador = Controlador.getControlador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        buttonSetting = (ImageButton) findViewById(R.id.buttonSetting);
        buttonMessage = (ImageButton) findViewById(R.id.buttonMail);
        buttonMain    = (ImageButton) findViewById(R.id.buttonMain);
        buttonSetting.setImageResource(R.drawable.iconosetting2seleccion);

        susOfertaCine   = (CheckBox) findViewById(R.id.susOfertaCine);
        susOfertaCarnes = (CheckBox) findViewById(R.id.susOfertaCarnes);
        susOfertaRepos  = (CheckBox) findViewById(R.id.susOfertaReposteria);
        susOfertaPescaderia = (CheckBox) findViewById(R.id.susOfertaPesc);
        susOfertaHogar = (CheckBox) findViewById(R.id.susOfertaProductos);
        susOfertaFruteria = (CheckBox) findViewById(R.id.susOfertaFruteria);
        overridePendingTransition(0,0);
        restaurarEstado();

    }

    @Override
    protected void onResume() {
        //buttonSetting.setImageResource(R.drawable.iconosetting2seleccion);
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    private void restaurarEstado(){
        boolean checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("susOfertaCine", false);
        susOfertaCine.setChecked(checked);


        checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("susOfertaCarnes", false);
        susOfertaCarnes.setChecked(checked);


        checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("susOfertaRepos", false);
        susOfertaRepos.setChecked(checked);


        checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("susOfertaPescaderia", false);
        susOfertaPescaderia.setChecked(checked);


        checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("susOfertaHogar", false);
        susOfertaHogar.setChecked(checked);

        checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("susOfertaFruteria", false);
        susOfertaFruteria.setChecked(checked);

        suscripcionesBox.add(susOfertaCine);
        suscripcionesBox.add(susOfertaCarnes);
        suscripcionesBox.add(susOfertaRepos);
        suscripcionesBox.add(susOfertaHogar);
        suscripcionesBox.add(susOfertaPescaderia);
        suscripcionesBox.add(susOfertaFruteria);


        for (CheckBox c: suscripcionesBox) {
            comprobarCheckBox(c);
        }
        if(controlador.getCurrentUser().isDentroCentro()){
            controlador.enviarMensaje("intereses "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",",controlador.getCurrentUser().getListaIntereses()),this);
        }

    }

    private void comprobarCheckBox(CheckBox v) {
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()) {
            case R.id.susOfertaCine:
                if (checked) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaCine", checked).commit();
                    susOfertaCine.setTypeface(susOfertaCine.getTypeface(), Typeface.BOLD_ITALIC);
                    controlador.addInteres("ofertas.cine.*");//Parte pública
                    controlador.addInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.cine.*");//Parte privada
                    controlador.addCanal("Cine");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaCine", checked).commit();
                    susOfertaCine.setTypeface(null, Typeface.NORMAL);
                    controlador.removeInteres("ofertas.cine.*");//Parte pública
                    controlador.removeInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.cine.*");//Parte privada
                    controlador.removeCanal("Cine");
                }
                break;

            case R.id.susOfertaCarnes:
                if (checked) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaCarnes", checked).commit();
                    susOfertaCarnes.setTypeface(susOfertaCarnes.getTypeface(), Typeface.BOLD_ITALIC);
                    controlador.addInteres("ofertas.supermarket.charcuteria");//Parte pública
                    controlador.addInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.charcuteria");//Parte privada
                    controlador.addCanal("Charcuteria");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaCarnes", checked).commit();
                    susOfertaCarnes.setTypeface(null, Typeface.NORMAL);
                    controlador.removeInteres("ofertas.supermarket.charcuteria");//Parte pública
                    controlador.removeInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.charcuteria");//Parte privada
                    controlador.removeCanal("Charcuteria");

                }
                break;

            case R.id.susOfertaReposteria:
                if (checked) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaRepos", checked).commit();
                    susOfertaRepos.setTypeface(susOfertaRepos.getTypeface(), Typeface.BOLD_ITALIC);
                    controlador.addInteres("ofertas.supermarket.reposteria");//Parte pública
                    controlador.addInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.reposteria");//Parte privada
                    controlador.addCanal("Reposteria");

                } else {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaRepos", checked).commit();
                    susOfertaRepos.setTypeface(null, Typeface.NORMAL);
                    controlador.removeInteres("ofertas.supermarket.reposteria");//Parte pública
                    controlador.removeInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.reposteria");//Parte privada
                    controlador.removeCanal("Reposteria");

                }
                break;

            case R.id.susOfertaPesc:
                if (checked) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaPescaderia", checked).commit();
                    susOfertaPescaderia.setTypeface(susOfertaPescaderia.getTypeface(), Typeface.BOLD_ITALIC);
                    controlador.addInteres("ofertas.supermarket.pescaderia");//Parte pública
                    controlador.addInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.pescaderia"); //Parte privada
                    controlador.addCanal("Pescaderia");

                } else {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaPescaderia", checked).commit();
                    susOfertaPescaderia.setTypeface(null, Typeface.NORMAL);
                    controlador.removeInteres("ofertas.supermarket.pescaderia");//Parte pública
                    controlador.removeInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.pescaderia");//Parte privada
                    controlador.removeCanal("Pescaderia");

                }
                break;

            case R.id.susOfertaProductos:
                if (checked) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaHogar", checked).commit();
                    susOfertaHogar.setTypeface(susOfertaHogar.getTypeface(), Typeface.BOLD_ITALIC);
                    controlador.addInteres("ofertas.supermarket.hogar");//Parte pública
                    controlador.addInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.hogar");//Parte privada
                    controlador.addCanal("Productos hogar");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaHogar", checked).commit();
                    susOfertaHogar.setTypeface(null, Typeface.NORMAL);
                    controlador.removeInteres("ofertas.supermarket.hogar");//Parte pública
                    controlador.removeInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.hogar");//Parte privada
                    controlador.removeCanal("Productos hogar");
                }
                break;

            case R.id.susOfertaFruteria:
                if (checked) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaFruteria", checked).commit();
                    susOfertaFruteria.setTypeface(susOfertaFruteria.getTypeface(), Typeface.BOLD_ITALIC);
                    controlador.addInteres("ofertas.supermarket.fruteria");//Parte pública
                    controlador.addInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.fruteria");//Parte privada
                    controlador.addCanal("Fruteria");
                } else {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("susOfertaFruteria", checked).commit();
                    susOfertaFruteria.setTypeface(null, Typeface.NORMAL);
                    controlador.removeInteres("ofertas.supermarket.fruteria");//Parte pública
                    controlador.removeInteres("cliente."+controlador.getCurrentUser().getIdUsuario()+".ofertas.fruteria");//Parte privada
                    controlador.removeCanal("Fruteria");
                }
                break;

        }

    }


    public void onCheckBoxClicked(View v) {
        comprobarCheckBox((CheckBox) v);
        if(controlador.getCurrentUser().isDentroCentro()){
            controlador.enviarMensaje("intereses "+controlador.getCurrentUser().getIdUsuario()+" "+TextUtils.join(",",controlador.getCurrentUser().getListaIntereses()),this);
        }
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
        buttonMessage.setImageResource(R.drawable.iconomensajeseleccion);
        buttonSetting.setImageResource(R.drawable.iconosetting2);
        buttonMain.setImageResource(R.drawable.iconomain);

        Intent intent = new Intent(this, CanalesActivity.class);
        startActivity(intent);
        finish();


    }

    public void onClickedSetting(View v){
        buttonMessage.setImageResource(R.drawable.iconomensaje);
        buttonSetting.setImageResource(R.drawable.iconosetting2seleccion);
        buttonMain.setImageResource(R.drawable.iconomain);

    }

}
