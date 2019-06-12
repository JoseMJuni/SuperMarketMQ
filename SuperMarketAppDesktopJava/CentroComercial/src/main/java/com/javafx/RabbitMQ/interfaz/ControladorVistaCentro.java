package com.javafx.RabbitMQ.interfaz;

import java.awt.TextField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.javafx.RabbitMQ.entities.Cliente;
import com.javafx.RabbitMQ.entities.Controlador;
import com.javafx.RabbitMQ.entities.HiloRefresh;
import com.javafx.RabbitMQ.utilidades.Utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public  class ControladorVistaCentro {
	private static ControladorVistaCentro controladorVistaCentro;
	
	private HashMap<String, Cliente> listaClientes =  new HashMap<String, Cliente>();
	private boolean onClick = false;
	

	@FXML
	public TableView<Cliente> clienteTabla;
	@FXML
	private TableColumn<Cliente, String> column1; //Id
	@FXML
	private TableColumn<Cliente, String> column2; //localizacion
	@FXML
	private TableColumn<Cliente, String> column3; //intereses
	@FXML
	private TableColumn<Cliente, String> column4; //tiempo
	
	@FXML
	private Label idNombre;
	@FXML
	private Label idLocalizacion;
	@FXML
	private Label idSus;
	@FXML
	private Label idHora;
	@FXML
	private Label idDetallesCliente;
	
	@FXML
	private Text nombre;
	@FXML
	private Text localizacion;
	@FXML
	private Text fechaEntrada;
	@FXML
	private TextArea listaIntereses;
	@FXML
	private Button btnEstadisticas;
	@FXML
	private Button btnSend;


	@FXML
	private AnchorPane panel;
	


	
	// Reference to the main application.
    private static MainApp mainApp;
	
	
	public static ControladorVistaCentro getControlador() {
		
		if(controladorVistaCentro == null) {
			
			controladorVistaCentro = new ControladorVistaCentro();
		}
		return controladorVistaCentro;
	}
	

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	Controlador.getControlador().inicializarConsumidorServidor();
    	Controlador.getControlador().inicializarPublicadorServidor();

     // Initialize the client table with the two columns.
        column1.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        column2.setCellValueFactory(cellData -> cellData.getValue().getLocalizacion());
        column3.setCellValueFactory(cellData -> cellData.getValue().getInteresesStringProperty());
        column4.setCellValueFactory(cellData -> cellData.getValue().getTiempoTranscurrido());
     // Listen for selection changes and show the person details when changed.
        
     // Clear person details.
        showClientDetails(null);

     // Listen for selection changes and show the person details when changed.
        Thread refreshTabla = new HiloRefresh();
        refreshTabla.start();     
    
        
     
        
        
    }
    

    

    public void showClientDetails(Cliente cl) {
        if (cl != null) {
        	btnEstadisticas.setVisible(true);
        	btnSend.setVisible(true);
            listaIntereses.setVisible(true);
            listaIntereses.setText(Controlador.getControlador().getInteresesCliente(cl));
            nombre.setText(cl.getNombre().getValue());
            localizacion.setText(cl.getLocalizacion().getValue());
            fechaEntrada.setText(Controlador.getControlador().getFechaEntrada(cl));

            
        } else {
        	btnEstadisticas.setVisible(false);
        	btnSend.setVisible(false);
        	listaIntereses.setText("");
            nombre.setText("");
            localizacion.setText("");
            fechaEntrada.setText("");

        }
    }
    
    @FXML
    public void showSendOverview(MouseEvent event)
    {
    	if (event.getClickCount() == 1) //Comprobamos click
        {
        	if(clienteTabla.getSelectionModel().getSelectedItem() != null) {
        		
        		showClientDetails(clienteTabla.getSelectionModel().selectedItemProperty().get());
        	}
        }
    	if (event.getClickCount() == 2) //Comprobamos doble click
        {
        	if(clienteTabla.getSelectionModel().getSelectedItem() != null) {
                mainApp.showSendOverview(clienteTabla.getSelectionModel().selectedItemProperty().get());
        	}
        }
    }
    
    @FXML
    public void handleBtnSend()
    {
    	
    	if(clienteTabla.getSelectionModel().getSelectedItem() != null) {
            mainApp.showSendOverview(clienteTabla.getSelectionModel().selectedItemProperty().get());
    	}
        
    }
    
    @FXML
    public void handleBtnEstadisticas()
    {
    	if(clienteTabla.getSelectionModel().getSelectedItem() != null) {
            mainApp.showEstadisticas(false, clienteTabla.getSelectionModel().selectedItemProperty().get());
    	}
    }
     

   public void refreshTableView() {
	   synchronized (mainApp.getPersonData()) {
		   if(mainApp.getPersonData() != null) {
			   for (Cliente cl : mainApp.getPersonData()) {
				  int index = mainApp.getPersonData().indexOf(cl);
				  cl.setTiempoTranscurrido();
				  mainApp.getPersonData().set(index, cl);
				}
		   }
		}
   }
   

   public void refreshPanelViewDerecho() {
	   int selectedIndex = MainApp.controller.clienteTabla.getSelectionModel().getSelectedIndex();
       Cliente clSeleccionado = null;
       if(selectedIndex >= 0) {
    	   clSeleccionado = MainApp.controller.clienteTabla.getItems().get(selectedIndex);
       }
	   
	   if(clSeleccionado!=null) { 
		   MainApp.controller.showClientDetails(clSeleccionado);
	   }
          
		
   }
   
   public void removeCliente(Cliente cl) {
       int selectedIndex = MainApp.controller.clienteTabla.getSelectionModel().getSelectedIndex();
       Cliente clSeleccionado = null;
       if(selectedIndex >= 0) {
    	   clSeleccionado = MainApp.controller.clienteTabla.getItems().get(selectedIndex);
       }
	   synchronized (mainApp.getPersonData()) {
		   if(clSeleccionado!=null && clSeleccionado.equals(cl)) { 
    		   MainApp.controller.showClientDetails(null);
    	   }
		  if(cl != null) {
				cl.setLocalizacion("Fuera"); //Actualizamos la localizacion
				int index = mainApp.getPersonData().indexOf(cl);
				mainApp.getPersonData().remove(index);

				
			}
		
		}
       
	

	   
   }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        clienteTabla.setItems(mainApp.getPersonData());
    }
    
    public MainApp getMainApp() {
    	return mainApp;
    }
	
	
	
	

}
