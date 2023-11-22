package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.util.Callback;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import essenciais.GerenciadorRecursos;
import essenciais.Pagina;
import essenciais.Processo;

public class ControladorAbaRecursos {
	
	private GerenciadorRecursos gerRec;
	
	private String previo;

	@FXML
	private ListView<Processo> lvProcessos;

	@FXML
	private FlowPane quadros;

	@FXML
	private HBox base;

	@FXML
	private VBox lista;

	@FXML
	private Label tamanhoRecurso, tamanhoDisponivel;

	@FXML
	private URL location;

	@FXML
	private ResourceBundle resources;
	
	private List<Processo> lProcessos = new ArrayList<>(); protected
	ListProperty<Processo> lPropProcessos = new SimpleListProperty<>();
	
	
	public ControladorAbaRecursos() {
	}

	@FXML
	private void initialize() {
	}

	void initData(GerenciadorRecursos gerRec) {
		
		this.gerRec = gerRec;

		Label rotulo;
		
		this.previo = tamanhoDisponivel.getText();
		
		tamanhoDisponivel.setText(tamanhoDisponivel.getText() + Integer.toString(gerRec.getTamanhoDisponivel()));

		tamanhoRecurso.setText(tamanhoRecurso.getText() + Integer.toString(gerRec.getTamanhoDisponivel()));
		
		for (Pagina p : gerRec.getQuadros()) {
			rotulo = new Label(p.toString());
			rotulo.getStyleClass().add("qds");
			quadros.getChildren().add(rotulo);
		}
	}

	public void atualizar(Pagina p) {

		Label rotulo = new Label(p.toString());
		rotulo.getStyleClass().add("qds");
		
		quadros.getChildren().set(p.getEndFisico(), rotulo);
		tamanhoDisponivel.setText(previo + Integer.toString(this.gerRec.getTamanhoDisponivel()));
	}
}
