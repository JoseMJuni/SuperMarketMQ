package com.javafx.RabbitMQ.interfaz;

import java.util.ArrayList;
import java.util.Arrays;

import com.javafx.RabbitMQ.entities.Cliente;
import com.javafx.RabbitMQ.entities.Controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ControladorEstadisticas {
	
	@FXML
	private BarChart<String, Integer> grafico;
	@FXML
	private CategoryAxis xAxis;
	
    private ObservableList<String> localizaciones = FXCollections.observableArrayList();
    private Controlador controlador = Controlador.getControlador();
    
	private Stage dialogStage;
    
    @FXML
    private void initialize() {
       
    }
    
    /**
     * Sets the persons to show the statistics for.
     * 
     * @param persons
     */
    public void setCliente(boolean lista, Cliente...cls) {
        if(!lista) {
        	Cliente cl = cls[0];
 
	        if(controlador.getLocalizacionesHistorico(cl).size() > 0) {
	        	 localizaciones.addAll(controlador.getLocalizacionesHistorico(cl));
	             xAxis.setCategories(localizaciones);
	             XYChart.Series<String, Integer> series = new XYChart.Series<>();
	            
	            for (int i = 0; i < Controlador.getControlador().getTiempoHistorico(cl).size(); i++) {
	                series.getData().add(new XYChart.Data<>(Controlador.getControlador().getLocalizacionesHistorico(cl).get(i), Controlador.getControlador().getTiempoHistorico(cl).get(i)));
	            }
	          
	           grafico.getData().add(series);
	
	        }
        }
        else if(lista && cls.length >0) {
        	int[] mediaTiempos = new int[controlador.getLocalizaciones().length];
        	String[] localizacionesCentro = controlador.getLocalizaciones();
        	for(int i = 0; i < controlador.getLocalizaciones().length; i++) {
        		for(Cliente cl: cls) {
        			mediaTiempos[i] += controlador.getTiempoLocalizacionCliente(cl, localizacionesCentro[i]); 
        		}
        		mediaTiempos[i] = mediaTiempos[i]/cls.length;
        	}
        	 localizaciones.addAll(Arrays.asList(controlador.getLocalizaciones()));
             xAxis.setCategories(localizaciones);
             XYChart.Series<String, Integer> series = new XYChart.Series<>();
	            
	            for (int i = 0; i < localizacionesCentro.length; i++) {
	                series.getData().add(new XYChart.Data<>(localizaciones.get(i), mediaTiempos[i]));
	            }
	          
	           grafico.getData().add(series);
	

        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
