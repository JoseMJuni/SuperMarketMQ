package com.javafx.RabbitMQ.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.javafx.RabbitMQ.interfaz.ControladorVistaCentro;



public class HiloPublicadorPublico extends Thread {
	Controlador controlador = Controlador.getControlador();
	
	@Override
	public void run() {
		while(true) {
			int mediaTotal=0;
			if(controlador.getClientes().size() > 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int[] mediaTiempos = new int[controlador.getLocalizaciones().length];
	        	String[] localizacionesCentro = controlador.getLocalizaciones();
	        	for(int i = 0; i < controlador.getLocalizaciones().length; i++) {
	        		for(Cliente cl: controlador.getClientes()) {
	        			
	        			mediaTiempos[i] += controlador.getTiempoLocalizacionCliente(cl, localizacionesCentro[i]); 
	        			System.out.println("LOCALIZACIONES: "+localizacionesCentro[i]+" Tiempo: "+mediaTiempos[i]);
	        		}
	        		mediaTiempos[i] = mediaTiempos[i]/controlador.getClientes().size();
	        		if(i>0) mediaTotal = mediaTotal + mediaTiempos[i]; //No tenemos en cuenta el hall
	        	}
	        	controlador.getLocalizaciones();
	        	
	        	
	        	//Sacamos la media y mostramos mensajes de aquellos que esten por debajo de la media. Quitamos Hall de la lista
	        	mediaTotal = mediaTotal/(controlador.getLocalizaciones().length-1);
	        		for(int i = 1; i < controlador.getLocalizaciones().length; i++) {
        				try {
        					Thread.sleep(10000);
        				} catch (InterruptedException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
		        		if(mediaTiempos[i] <= mediaTotal) {
		        			if(localizacionesCentro[i].equals("Cine")) {
		        				String[] mensaje = {"ofertas.cine.*","Visita nuestras salas, y prueba las palomitas de caramelo"};
								new Productor(mensaje).enviar();
		        			}
		        			else {
			        			String[] mensaje = {"ofertas.supermarket."+localizacionesCentro[i].toLowerCase(),"Nuevos productos y ofertas en "+localizacionesCentro[i]};
								new Productor(mensaje).enviar();
			        		}
		        			
		        		}
		        		
		        		
		        	}

	
			}		
			
		
			
	
		}
	}
	
	

}
