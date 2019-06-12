package com.javafx.RabbitMQ.interfaz;

import java.util.Arrays;
import java.util.LinkedList;

import com.javafx.RabbitMQ.entities.Cliente;
import com.javafx.RabbitMQ.utilidades.Utils;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class ControladorRootLayaout {
    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleSendAll() {
    	//Creo un cliente falso
    	Cliente cl = new Cliente("Send All", "Everywhere", Arrays.asList(Utils.getColaSendAll()));
    	mainApp.showSendOverview(cl);
        
    }
    
    @FXML
    private void handleEstadisticas() {
    	ObservableList<Cliente> cls = mainApp.getPersonData();
    	LinkedList<Cliente> listaClientes = new LinkedList<Cliente>();
    	for(Cliente c: cls) {
    		listaClientes.add(c);
    	}    
    	//Llamamos con todos los clientes
    	mainApp.showEstadisticas(true, listaClientes.toArray(new Cliente[listaClientes.size()]));
        
    }
    
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    
    
    
    
}
