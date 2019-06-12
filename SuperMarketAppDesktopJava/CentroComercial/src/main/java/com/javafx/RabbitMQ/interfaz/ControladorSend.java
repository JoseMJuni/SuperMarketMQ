package com.javafx.RabbitMQ.interfaz;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.javafx.RabbitMQ.entities.Cliente;
import com.javafx.RabbitMQ.entities.Productor;

public class ControladorSend {

	@FXML
	ComboBox<String> menu;
	@FXML
	TextArea textoMensaje;
	
	String[] mensajeRouting = new String[2];
	
	private Stage dialogStage;
	private Cliente cliente;
	private boolean okClicked = false;
	
	private Productor productoEnviar;
	
	
	
	//Variable canal
	
	
	  /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Establecemos los canales que el usuario tiene disponibles (esta suscrito) para enviarle un mensaje donde queramos
     * Siempre y cuando sea por la parte privada del canal, es decir, donde el prefijo sea cliente.*.*
     * 
     * @param person
     */
    public void setSplitMenuItem(Cliente c) {
        this.cliente = c;
        if(!cliente.getLocalizacion().getValue().equals("Everywhere")) {
        	for(String s: c.getIntereses()) {
            	String[] splitInteres = s.split("\\.");
            	if(splitInteres.length >0 && splitInteres[0].equals("cliente")) {
            		menu.getItems().add(s);
            	}
            }
        }
        else 
        {
        	menu.getItems().addAll(cliente.getIntereses());
        }
        
        
    }
    
    

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    @FXML
    public void handleMenu() {
    	System.out.println(menu.getValue());
    	mensajeRouting[0] = menu.getValue();
    	
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleSend() {
        if (isInputValid()) {
        	mensajeRouting[1] = textoMensaje.getText();
        	System.out.println(textoMensaje.getText());
        	productoEnviar = new Productor(mensajeRouting);
            productoEnviar.enviar();

        	
        	
        }
    }
    
    /**
     * Comprobamos que el mensaje a enviar no es vacío
     * @return
     */
    private boolean isInputValid() {
    	if(mensajeRouting[0] == null || mensajeRouting[0].length() == 0) {
    		Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
    		alert.setTitle("Error enviar el mensaje");
    		alert.setHeaderText("No se pudo enviar el mensaje.");
    		alert.setContentText("No ha seleccionado ningun canal!");

    		alert.showAndWait();
    		return false;
    		
    	}
    	else if(textoMensaje.getText() == null || textoMensaje.getText().length()==0) {
    		Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    		alert.setTitle("Error enviar el mensaje");
    		alert.setHeaderText("No se pudo enviar el mensaje.");
    		alert.setContentText("El mensaje esta vacío !");

    		alert.showAndWait();
    		return false;
    	}
    	return true;
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handlClose() {
        dialogStage.close();
    }
	
}
