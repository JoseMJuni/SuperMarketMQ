package com.javafx.RabbitMQ.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente {
	private StringProperty nombre; //Va a ser el ID
	private Calendar fechaInicio; //Fecha que ingreso en el CC
	private StringProperty localizacion;
	private LinkedList<String> listaIntereses;
	private StringProperty tiempoTranscurrido;
	private Calendar tiempoLocalizacion;
	private HashMap<String, Integer> historialLocalizaciones;
	
	
	public Cliente(String nombre, String localizacion, List<String> intereses) {
		this.nombre = new SimpleStringProperty(nombre);
		this.fechaInicio = Calendar.getInstance();
		this.localizacion =  new SimpleStringProperty(localizacion);
		this.listaIntereses = new LinkedList<String>(intereses);
		this.historialLocalizaciones = new HashMap<String, Integer>();
		this.tiempoTranscurrido = getFechaInicioStringProperty();
		this.tiempoLocalizacion = Calendar.getInstance();

	}

	public StringProperty getNombre() {
		return nombre;
	}

	public Calendar getFechaInicio() {
		return fechaInicio;
	}
	
	public StringProperty getFechaEntrada() {
		return new SimpleStringProperty(String.format("%02d:%02d", (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY), (Calendar.getInstance()).get(Calendar.MINUTE)));
	}
	
	public StringProperty getFechaInicioStringProperty() {
		Calendar actual = Calendar.getInstance();
		Integer formattedHora = (Math.abs(actual.get(Calendar.HOUR_OF_DAY)-fechaInicio.get(Calendar.HOUR_OF_DAY)));
		Integer formattedMinuto = (Math.abs(actual.get(Calendar.MINUTE)-fechaInicio.get(Calendar.MINUTE)));
		Integer formattedSegundo = (Math.abs(actual.get(Calendar.SECOND)-fechaInicio.get(Calendar.SECOND)));
		String formatted = String.format("%02d:%02d:%02d",formattedHora,formattedMinuto,formattedSegundo);
		return new SimpleStringProperty(formatted);
	}
	
	private Integer getTiempoTranscurridoLocalizacion() {
		Calendar actual = Calendar.getInstance();
		int horas = (Math.abs(actual.get(Calendar.HOUR_OF_DAY)-tiempoLocalizacion.get(Calendar.HOUR_OF_DAY))) * 60;
		int minutos = Math.abs(actual.get(Calendar.MINUTE)-tiempoLocalizacion.get(Calendar.MINUTE));
		return horas+minutos;
	}
	
	public StringProperty getTiempoTranscurrido() {
		return tiempoTranscurrido;
	}
	
	public void setTiempoTranscurrido() {
		this.tiempoTranscurrido = getFechaInicioStringProperty();
	}
	
	
	public StringProperty getLocalizacion() {
		return localizacion;
	}
	
	public void setLocalizacion(String nuevaLocalizacion) {
		actualizarHistorialLocalizacion(nuevaLocalizacion);
		this.localizacion =  new SimpleStringProperty(nuevaLocalizacion);
	}
	
	public void setIntereses(List<String> intereses) {
		
		this.listaIntereses = new LinkedList<String>(intereses);
	}
	
	public List<String> getIntereses() {
		return new LinkedList<String>(this.listaIntereses);
	}
	
	public StringProperty getInteresesStringProperty() {
		String interesCadena = new String("");
		for(String c : listaIntereses)
			interesCadena = c+ ", "+interesCadena;
		
		return new SimpleStringProperty(interesCadena);
	}
	
	public HashMap<String,Integer> getHistorialLocalizaciones(){
		synchronized (historialLocalizaciones) {
			return new HashMap<String, Integer>(historialLocalizaciones);
		}
	}
	
	public Integer getTiempoLocalizacionActual() {
		return getTiempoTranscurridoLocalizacion();
	}
	
	
	private void actualizarHistorialLocalizacion(String nuevaLocalizacion) {
		if(historialLocalizaciones.containsKey(this.localizacion.get())) {
			int minutos = historialLocalizaciones.get(this.localizacion.get()) +getTiempoTranscurridoLocalizacion();
			this.historialLocalizaciones.replace(this.localizacion.get(), minutos);

		}
		else {
			this.historialLocalizaciones.put(this.localizacion.get(), getTiempoTranscurridoLocalizacion());

		}
		this.tiempoLocalizacion = Calendar.getInstance();
		
	}
	
	public ArrayList<String> getLocalizacionesHistorico() {
		return new ArrayList<String>(historialLocalizaciones.keySet());
	}
	
	public ArrayList<Integer> getTiempoHistorico() {
		return new ArrayList<Integer>(historialLocalizaciones.values());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.getValue().equals(other.nombre.getValue()))
			return false;
		return true;
	}
	
	
	

}
