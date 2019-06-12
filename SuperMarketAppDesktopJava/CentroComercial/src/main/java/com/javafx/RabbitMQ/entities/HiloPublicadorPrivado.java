package com.javafx.RabbitMQ.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.javafx.RabbitMQ.interfaz.ControladorVistaCentro;



public class HiloPublicadorPrivado extends Thread {
	Controlador controlador = Controlador.getControlador();
	List<Cliente> listaClientes = new ArrayList<Cliente>();
	HashMap<String, List<Cliente>> mensajesEnviados = new HashMap<String, List<Cliente>>(); //HashMap para tener un control de los mensajes
	
	
	
	
	@Override
	public void run() {
		//Mensajes predefinidos
		mensajesEnviados.put("SinIntereses",new ArrayList<Cliente>());
		mensajesEnviados.put("CineGratis", new ArrayList<Cliente>()); 
		mensajesEnviados.put("CarneGratis", new ArrayList<Cliente>()); 
		mensajesEnviados.put("OfertaReposteria", new ArrayList<Cliente>()); 
		mensajesEnviados.put("OfertasPescaderia", new ArrayList<Cliente>()); 
		mensajesEnviados.put("OfertasHogar", new ArrayList<Cliente>());
		mensajesEnviados.put("OfertasFruteria", new ArrayList<Cliente>());
		List<Cliente> clientesCine = mensajesEnviados.get("CineGratis");
		List<Cliente> clientesCarne = mensajesEnviados.get("CarneGratis");
		List<Cliente> clientesFruteria = mensajesEnviados.get("OfertasFruteria");
		List<Cliente> clientesSinIntereses = mensajesEnviados.get("SinIntereses");
		List<Cliente> clientesReposteria = mensajesEnviados.get("OfertaReposteria");
		List<Cliente> clientesPescaderia = mensajesEnviados.get("OfertaPescaderia");
		List<Cliente> clientesHogar = mensajesEnviados.get("OfertasHogar");
		
		while(true) {
			listaClientes = controlador.getClientes();
			if(listaClientes.size() >= 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(Cliente cl: listaClientes) {
					//Comprobamos los usuarios que no tienen intereses seleccionados
					synchronized (cl) {
						
						
						if(!clientesSinIntereses.contains(cl)) {
							if(cl.getIntereses().size() < 3) { //El usuario siempre tiene dos canales por defecto, el del información centro y el del centro privado
								String[] mensaje = {"cliente."+cl.getNombre().getValue()+".centro","Aún no has seleccionado intereses. Puedes hacerlo en Ajustes de la aplicación"};
								new Productor(mensaje).enviar();
								clientesSinIntereses.add(cl);
							}
							
						}
						//FIN comprobación los usuarios que no tienen intereses seleccionados
						//Información sobre nuevos productos
							//Reposteria
						
						if(!clientesReposteria.contains(cl)) {
							if(cl.getHistorialLocalizaciones().containsKey("Reposteria")) {
								Integer tiempoLocalizacion = Integer.valueOf(cl.getHistorialLocalizaciones().get("Reposteria"));
								if(tiempoLocalizacion == null) tiempoLocalizacion = 0;
								
								if(tiempoLocalizacion >= 1 &&  controlador.getInteresUsuario(cl, "Reposteria")) {
									
									String[] mensaje = {"cliente."+cl.getNombre().getValue()+".ofertas.reposteria","PAN PIZZA GRATUITO. Acercate al mostrador y enseña este mensaje"};
									new Productor(mensaje).enviar();
									clientesReposteria.add(cl);
								}
							}
						}
						
						
						if(!clientesReposteria.contains(cl)) {
							if(cl.getHistorialLocalizaciones().containsKey("Pescaderia")) {
								Integer tiempoLocalizacion = Integer.valueOf(cl.getHistorialLocalizaciones().get("Pescaderia"));
								if(tiempoLocalizacion == null) tiempoLocalizacion = 0;
								if(tiempoLocalizacion >= 1 &&  controlador.getInteresUsuario(cl, "Pescaderia")) {
									String[] mensaje = {"cliente."+cl.getNombre().getValue()+".ofertas.pescaderia","2x1 en Lubina. Acercate al mostrador y enseña este mensaje"};
									new Productor(mensaje).enviar();
									clientesPescaderia.add(cl);
								}
							}
						}
						
						if(!clientesHogar.contains(cl)) {
							if(cl.getHistorialLocalizaciones().containsKey("Hogar")) {
								Integer tiempoLocalizacion = Integer.valueOf(cl.getHistorialLocalizaciones().get("Hogar"));
								if(tiempoLocalizacion == null) tiempoLocalizacion = 0;
								if(tiempoLocalizacion >= 1 &&  controlador.getInteresUsuario(cl, "Hogar")) {
									String[] mensaje = {"cliente."+cl.getNombre().getValue()+".ofertas.hogar","2x1 en PAÑALES. Acercate al mostrador y enseña este mensaje"};
									new Productor(mensaje).enviar();
									clientesHogar.add(cl);
								}
							}
						}
						
						
						if(!clientesCine.contains(cl)) {
							if(cl.getLocalizacion().getValue().equals("Cine") && controlador.getLocalizacionInteres(cl)) {
								Integer tiempoLocalizacion = cl.getTiempoLocalizacionActual();
								if(tiempoLocalizacion == null) tiempoLocalizacion = 0;
								if(tiempoLocalizacion >= 1) {
									String[] mensaje = {"cliente."+cl.getNombre().getValue()+".ofertas.cine.cartelera","UNA ENTRADA GRATIS para la pelicula que tu elijas. Acercate al mostrador y enseña este mensaje"};
									new Productor(mensaje).enviar();
									clientesCine.add(cl);
								}
							}
						}
						
						if(!clientesCarne.contains(cl)) {
							if(cl.getLocalizacion().getValue().equals("Charcuteria") && controlador.getLocalizacionInteres(cl)) {
								Integer tiempoLocalizacion = cl.getTiempoLocalizacionActual();
								if(tiempoLocalizacion == null) tiempoLocalizacion = 0;
								if(tiempoLocalizacion >= 1) {
									String[] mensaje = {"cliente."+cl.getNombre().getValue()+".ofertas.charcuteria","HAMBURGUESA DE POLLO GRATIS. Acercate al mostrador y enseña este mensaje"};
									new Productor(mensaje).enviar();
									clientesCarne.add(cl);
								}
							}
						}
						
						
						if(!clientesFruteria.contains(cl)) {
							if(cl.getLocalizacion().getValue().equals("Fruteria") && controlador.getLocalizacionInteres(cl)) {
								Integer tiempoLocalizacion = cl.getTiempoLocalizacionActual();
								if(tiempoLocalizacion == null) tiempoLocalizacion = 0;
								if(tiempoLocalizacion >= 1) {
									String[] mensaje = {"cliente."+cl.getNombre().getValue()+".ofertas.fruteria","1 Kg de naranjas a mitad de precio. Acercate al mostrador y enseña este mensaje"};
									new Productor(mensaje).enviar();
									clientesFruteria.add(cl);
								}
							}
						}
						
					}
					
				}
				
			}
			
			
			
			
			
		
			
		
			
	
		}
	}
	
	

}
