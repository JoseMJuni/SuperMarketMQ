package com.javafx.RabbitMQ.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.javafx.RabbitMQ.interfaz.ControladorVistaCentro;
import com.javafx.RabbitMQ.interfaz.MainApp;
import com.javafx.RabbitMQ.utilidades.Utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class Controlador {
	private static Controlador controlador;
	private static HashMap<String, Cliente> listaClientes =  new HashMap<String, Cliente>();
	private int tiempoNecesario = 1;
	
	private Cliente currentCliente = null;
	
	
	public static Controlador getControlador() {
		
		if(controlador == null) {
			
			controlador = new Controlador();
		}
		return controlador;
	}
	
	public void inicializarConsumidorServidor() {
		Thread consumidorServidor = new Consumidor();
		consumidorServidor.start();
	}
	
	public void inicializarPublicadorServidor() {
		Thread hiloPublicador = new HiloPublicadorPrivado();
		Thread hiloPublicadorPublico = new HiloPublicadorPublico();
		hiloPublicadorPublico.start();
		hiloPublicador.start();
	}
	
	public Cliente getCurrentCliente() {
		return currentCliente;
	}
	
	public void setCurrentCliente(Cliente cl) {
		this.currentCliente = cl;
	}
	
	public Cliente getCliente(String id) {
		return listaClientes.get(id);
	}
	
	public List<Cliente> getClientes() {
		return new ArrayList<Cliente>(listaClientes.values());
	}
	

	public void addCliente(String id, String localizacion, String interes) {
		synchronized (ControladorVistaCentro.getControlador().getMainApp().getPersonData()) {
			if(interes == null) interes = ""; //Controlamos que el interes no sea nulo
			List<String> listaIntereses = Arrays.asList(interes.split(","));
			Cliente cl = new Cliente(id, localizacion, listaIntereses);
			if(!listaClientes.containsValue(cl)) {
				listaClientes.put(id, cl);
				ControladorVistaCentro.getControlador().getMainApp().getPersonData().add(cl);
//				String[] mensaje = {"cliente."+cl.getNombre().getValue()+".centro","Bienvenido. =)"};
//				new Productor(mensaje).enviar();
				

			}
		}
	}
	
	
	public void removeCliente(String id) {
		Cliente cl = listaClientes.get(id);
		ControladorVistaCentro.getControlador().removeCliente(cl);
		listaClientes.remove(id);
	}


	public void setInteresCliente(String id, String interes) {
		Cliente cl = listaClientes.get(id);
		if(cl!=null) {
			int index = ControladorVistaCentro.getControlador().getMainApp().getPersonData().indexOf(cl);
			cl.setIntereses(Arrays.asList(interes.split(",")));
			ControladorVistaCentro.getControlador().getMainApp().getPersonData().set(index, cl);
		}
		
	}
	
	public void setLocalizacion(String id, String nuevaLocalizacion) {
		synchronized (ControladorVistaCentro.getControlador().getMainApp().getPersonData()) {
			Cliente cl = listaClientes.get(id);
			if(!cl.getLocalizacion().getValue().equals(nuevaLocalizacion)) {
				cl.setLocalizacion(nuevaLocalizacion);
				int index = ControladorVistaCentro.getControlador().getMainApp().getPersonData().indexOf(cl);
				ControladorVistaCentro.getControlador().getMainApp().getPersonData().set(index, cl);
			}
		}
		
	}
	
	public String[] getClientesMayorTiempo() {
		synchronized (listaClientes) {
			ArrayList<String> clientes = new ArrayList<String>();
			Calendar fechaActual = Calendar.getInstance();
			int tiempoTranscurrido;
			for(String i: listaClientes.keySet()) {
				tiempoTranscurrido = fechaActual.get(Calendar.HOUR_OF_DAY) - listaClientes.get(i).getFechaInicio().get(Calendar.HOUR_OF_DAY);
				if(tiempoTranscurrido >= tiempoNecesario) clientes.add(listaClientes.get(i).getNombre().get());
			}
			 
			return clientes.toArray(new String[clientes.size()]);
		}
	}
	
	public String getInteresesCliente(Cliente cl) {
		String str = "";
		for(String s: cl.getIntereses()) {
			str += s + "\n";
		}
		return str;
	}
	
	//Comprueba que donde esta el cliente esta en su lista de intereses
	public boolean getLocalizacionInteres(Cliente cl) {
		String interesesJuntos = String.join(".", cl.getIntereses());
		List<String> interesesLocalizacion = Arrays.asList(interesesJuntos.split("[.]")); 
		return interesesLocalizacion.contains(cl.getLocalizacion().getValue().toLowerCase());
	}
	
	public String getNombreCliente(Cliente cl) {
		return cl.getNombre().get();
	}
	
	public String getLocalizacionCliente(Cliente cl) {
		return cl.getLocalizacion().get();
	}
	
	public String getFechaEntrada(Cliente cl) {
		return cl.getFechaEntrada().get();
	}
	
	public ArrayList<String> getLocalizacionesHistorico(Cliente cl) {
		return cl.getLocalizacionesHistorico();
	}
	
	public ArrayList<String> getLocalizacionesHistorico(Cliente...cl) {
		ArrayList<String> localizaciones = new ArrayList<String>();
		for(Cliente c: cl) {
			localizaciones.addAll(c.getLocalizacionesHistorico());
		}
		return localizaciones;
	}
	
	public ArrayList<Integer> getTiempoHistorico(Cliente cl) {
		return cl.getTiempoHistorico();
	}
	
	public ArrayList<Integer> getTiempoHistorico(Cliente...cl) {
		ArrayList<Integer> tiempo = new ArrayList<Integer>();
		for(Cliente c: cl) {
			tiempo.addAll(c.getTiempoHistorico());
		}
		return tiempo;
	}
	
	public Boolean isLocalizacionCliente(Cliente cl, String localizacion) {
		if(cl.getLocalizacionesHistorico().contains(localizacion))
			return true;
		return false;
				
	}
	
	public Integer getTiempoLocalizacionCliente(Cliente cl, String localizacion) {
		if(cl.getHistorialLocalizaciones().containsKey(localizacion))
			return cl.getHistorialLocalizaciones().get(localizacion);
		return 0;
	}
	
	public String[] getLocalizaciones() {
		return Utils.getLocalizaciones();
	}

	//Comprueba dado un usuario si le interesa X tema
	public boolean getInteresUsuario(Cliente cl, String string) {
		String interesesJuntos = String.join(".", cl.getIntereses());
		List<String> interesesLocalizacion = Arrays.asList(interesesJuntos.split("[.]")); 
		return interesesLocalizacion.contains(string.toLowerCase());
		
	}
	
}
