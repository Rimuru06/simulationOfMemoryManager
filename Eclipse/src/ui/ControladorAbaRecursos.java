package ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import recursos.GerenciadorRecursos;
import recursos.Pagina;
import recursos.Processo;

public class ControladorAbaRecursos {

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

	/*
	 * private List<Processo> lProcessos = new ArrayList<>(); protected
	 * ListProperty<Processo> lPropProcessos = new SimpleListProperty<>();
	 */

	public ControladorAbaRecursos() {
	}

	@FXML
	private void initialize() {
	}

	void initData(GerenciadorRecursos gerRec) {

		Label rotulo;

		tamanhoDisponivel.setText(tamanhoDisponivel.getText() + Integer.toString(gerRec.getTamanhoDisponivel()));

		tamanhoRecurso.setText(tamanhoRecurso.getText() + Integer.toString(gerRec.getTamanhoDisponivel()));

		/*
		 * lvProcessos.itemsProperty().bind(lPropProcessos);
		 * 
		 * lPropProcessos.set(FXCollections.observableArrayList(gerRec.getFila()
		 * ));
		 * 
		 * lvProcessos.setCellFactory(new Callback<ListView<Processo>,
		 * ListCell<Processo>>() {
		 * 
		 * @Override public ListCell<Processo> call(ListView<Processo> listView)
		 * { return new ListCell<Processo>() {
		 * 
		 * @Override public void updateItem(Processo item, boolean empty) { //
		 * Must call super super.updateItem(item, empty);
		 * 
		 * int index = this.getIndex(); String name = null;
		 * 
		 * // Format name if (item == null || empty) { // No action to perform }
		 * else { name = (index + 1) + ". Processo " + item.getId(); }
		 * 
		 * this.setText(name); setGraphic(null); } }; } });
		 */

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
	}
}
