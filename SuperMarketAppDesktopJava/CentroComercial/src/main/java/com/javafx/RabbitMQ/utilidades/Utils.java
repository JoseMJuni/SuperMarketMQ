package com.javafx.RabbitMQ.utilidades;

public class Utils {

	private static String[] colas = {"informacion.centro","ofertas.cine.tienda","ofertas.cine.cartelera",
			"ofertas.supermarket.charcuteria","ofertas.supermarket.limpieza","ofertas.supermarket.pescaderia",
			"ofertas.supermarket.fruteria","ofertas.supermarket.reposteria","ofertas.supermarket.hogar"};
	
	private static String[] localizaciones = {"Hall","Cine","Charcuteria","Pescaderia","Hogar","Fruteria","Reposteria"};
	
	
	public static String[] getColas() {
		return colas;
	}
	
	//Como estamos con topic el routing key es estricto.
	public static String[] getColaSendAll(){
		 String[] colasAll =  {"informacion.centro","ofertas.cine.tienda","ofertas.cine.cartelera",
			"ofertas.supermarket.charcuteria","ofertas.supermarket.pescaderia",
			"ofertas.supermarket.fruteria","ofertas.supermarket.reposteria","ofertas.supermarket.hogar"};
		 return colasAll;
	}
	
	public static String[] getLocalizaciones() {
		return localizaciones;
	}
	
	
	
}
