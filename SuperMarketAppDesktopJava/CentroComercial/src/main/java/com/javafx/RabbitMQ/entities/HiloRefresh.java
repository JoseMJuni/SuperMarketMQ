package com.javafx.RabbitMQ.entities;

import java.util.concurrent.TimeUnit;

import com.javafx.RabbitMQ.interfaz.ControladorVistaCentro;

import javafx.fxml.FXML;

public class HiloRefresh extends Thread {
	
	@FXML
	@Override
	public void run() {
		while(true) {
			try {
				TimeUnit.SECONDS.sleep(1);
				ControladorVistaCentro.getControlador().refreshTableView();
				ControladorVistaCentro.getControlador().refreshPanelViewDerecho();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
