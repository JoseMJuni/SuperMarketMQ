package com.javafx.RabbitMQ.interfaz;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.javafx.RabbitMQ.entities.Cliente;
import com.javafx.RabbitMQ.entities.Consumidor;
import com.javafx.RabbitMQ.entities.Controlador;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MainApp extends Application {
	
    private ObservableList<Cliente> clientData = FXCollections.observableArrayList();
    public static ControladorVistaCentro controller;

	
    private static Stage primaryStage;
    private BorderPane rootLayout;
    
    public MainApp() {
//    	LinkedList<String> lista = new LinkedList<String>();
//    	lista.add("ofertas.cine.*");
//    	lista.add("ofertas.charcuteria.cliente.*");
//    	clientData.add(new Cliente("a92b3dc6-89e4-427b-83f2-26f68cea7daa", "Cine",lista ));
    }

	@Override
	public void start(Stage primaryStage) {
	
		
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SuperMarket");
		

		
		initRootLayout();
		showCentroOverview();
		
		//Cuando le demos a la X de la interfaz salimos
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent t) {
	            Platform.exit();
	            System.exit(0);
	        }
	    });
		
		
		

	}
	
	 /**
     * Initializes the root layout.
     **/
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("RootLayaout.fxml"));
            rootLayout = (BorderPane) loader.load();
	          
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            ControladorRootLayaout controller = loader.getController();
	        controller.setMainApp(this);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Shows the person overview inside the root layout.
     */
    public void showCentroOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("VistaCentroOverview.fxml"));
            AnchorPane centroOverview = (AnchorPane) loader.load();

            // Set clients overview into the center of root layout.
            rootLayout.setCenter(centroOverview);
           
            // Give the controller access to the main app.
            controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  
    
    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     * 
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public void showSendOverview(Cliente c) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("SendOverview.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enviar mensaje a "+c.getNombre().get());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ControladorSend controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSplitMenuItem(c);

            // Show the dialog and wait until the user closes it
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showEstadisticas(boolean lista, Cliente...cl) {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("EstadisticasOverview.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Estadísticas Localización");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            ControladorEstadisticas controller = loader.getController();
            controller.setCliente(lista, cl);
           


            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Returns the data as an observable list of Persons. 
     * @return
     */
    public ObservableList<Cliente> getPersonData() {
        return clientData;
    }
    
    

	public static void main(String[] args) {
		
		launch(args);
		
		
		
    	
	}
	
	
	
 
}
