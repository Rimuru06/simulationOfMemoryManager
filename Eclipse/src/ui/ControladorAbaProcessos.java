package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import recursos.Pagina;
import recursos.Processo;
import so.Kernel;

public class ControladorAbaProcessos {

	@FXML
	private Accordion acProcessos;

	@FXML
	private URL location;

	@FXML
	private ResourceBundle resources;
	
	public ControladorAbaProcessos() {
	}

	@FXML
	private void initialize() {
	}

	public void initData(Kernel ker) {
		List<TitledPane> processos = new ArrayList<>();

		for (Processo p : ker.todosProcessos()) {
			processos.add(criarPainelProcesso(p));
		}

		acProcessos.getPanes().addAll(processos);
	}

	@SuppressWarnings("unchecked")
	public TitledPane criarPainelProcesso(Processo p) {
			
		GridPane grid = new GridPane();


		ObservableList<Entry<Integer, Pagina>> items = FXCollections.observableArrayList(p.getTabela().getHash().entrySet());
		TableView<Entry<Integer,Pagina>> tabela = new TableView<>(items);
		
		Label rotuloEstado = new Label(p.getEstado().toString());
		rotuloEstado.textProperty().bind(p.getEstadoStr());
		
		grid.addRow(0, new Label("Estado:"), rotuloEstado);
		grid.addRow(1, new Label("Tabela de Paginas:"));

		tabela.setPrefWidth(800);
		
		TableColumn<Entry<Integer, Pagina>, String> endLogCol = new TableColumn<>("Endereco Logico");
		endLogCol.setCellValueFactory(cellData -> {
			Integer ef = cellData.getValue().getKey();
			
			return new ReadOnlyStringWrapper(ef.toString());
		});

		TableColumn<Entry<Integer, Pagina>, String> endFisCol = new TableColumn<>("Endereco Fisico");
		endFisCol.setCellValueFactory(cellData -> {
			Integer ef = cellData.getValue().getValue().getEndFisico();
			
			return new ReadOnlyStringWrapper(ef.toString());
		});
		
		TableColumn<Entry<Integer, Pagina>, String> ultModCol = new TableColumn<>("Ultima Modificacao");
		ultModCol.setCellValueFactory(cellData -> {
			Date dum = cellData.getValue().getValue().getUltimaUtilizacao();
			
			return new ReadOnlyStringWrapper(dum.toString());
		});
		
		TableColumn<Entry<Integer, Pagina>, Boolean> presenteCol = new TableColumn<>("Presente?");
		presenteCol.setEditable(false);
		
		// Set a cell value factory
		presenteCol.setCellValueFactory(cellData -> {
	        Pagina pag = cellData.getValue().getValue();
			Boolean v =  pag.isPresente();
			return new ReadOnlyBooleanWrapper(v);
		});
		
		TableColumn<Entry<Integer, Pagina>, Boolean> modificadoCol = new TableColumn<>("Modificado?");
		modificadoCol.setEditable(false);
		
		// Set a cell value factory
		modificadoCol.setCellValueFactory(cellData -> {
	        Pagina pag = cellData.getValue().getValue();
			Boolean v =  pag.isModificado();
			return new ReadOnlyBooleanWrapper(v);
		});
		
		TableColumn<Entry<Integer, Pagina>, Boolean> utilizadoCol = new TableColumn<>("Utilizado?");
		utilizadoCol.setEditable(false);
		
		// Set a cell value factory
		utilizadoCol.setCellValueFactory(cellData -> {
	        Pagina pag = cellData.getValue().getValue();
			Boolean v =  pag.isModificado();
			return new ReadOnlyBooleanWrapper(v);
		});
		
		tabela.getColumns().addAll(endLogCol,
								   endFisCol, 
								   ultModCol,
								   presenteCol,
								   modificadoCol,
								   utilizadoCol);

		grid.addRow(2, tabela);
		
		TitledPane painel = new TitledPane("Processo " + p.getId(), grid);
		
		painel.setUserData(p.getId());
		
		return painel;

	}

	Accordion getBase() {
		return this.acProcessos;
	}
	
	public void atualizar(Processo p){
		int idProcesso = p.getId();
		TitledPane atual;
		
		for(int i = 0; i < acProcessos.getPanes().size(); i++){
			atual = acProcessos.getPanes().get(i);
			
			if((int)(atual.getUserData()) == idProcesso){
				acProcessos.getPanes().set(i, criarPainelProcesso(p));
			}
		}
	}

}
