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
import essenciais.Pagina;
import essenciais.Processo;
import essenciais.TabelaDePaginas;
import SistemaOperacional.Nucleo;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;


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

	public void initData(Nucleo ker) {
		List<TitledPane> processos = new ArrayList<>();

		for (Processo p : ker.todosProcessos()) {
			processos.add(criarPainelProcesso(p));
		}

		acProcessos.getPanes().addAll(processos);
	}

	@SuppressWarnings("unchecked")
	public TitledPane criarPainelProcesso(Processo p) {
			
		GridPane grid = new GridPane();
		
		TabelaDePaginas nova = new TabelaDePaginas();
		for(Entry<Integer, Pagina> entry : p.getTabela().getHash().entrySet()) {
			if(entry.getValue().getProcesso() == p) {
				nova.getHash().put(entry.getKey(), entry.getValue());
			}
			
		}

		
		
		ObservableList<Entry<Integer, Pagina>> items = FXCollections.observableArrayList(nova.getHash().entrySet());
		TableView<Entry<Integer,Pagina>> tabela = new TableView<>(items);
		
		
		Screen screen = Screen.getPrimary();
	    Rectangle2D bounds = screen.getVisualBounds();
		
	    tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    tabela.setMaxWidth(Double.MAX_VALUE);
		
		Label rotuloEstado = new Label(p.getEstado().toString());
		rotuloEstado.textProperty().bind(p.getEstadoStr());
		
		HBox hbox = new HBox(new Label("Estado: "), rotuloEstado);
		grid.addRow(0, hbox);

		rotuloEstado.setMaxWidth(Double.MAX_VALUE);
		rotuloEstado.setWrapText(true);

		grid.addRow(1, new Label("Tabela de Paginas:"));
		
		tabela.maxWidthProperty().bindBidirectional(tabela.prefWidthProperty());
	    tabela.setPrefWidth(bounds.getWidth());
		
		TableColumn<Entry<Integer, Pagina>, String> endLogCol = new TableColumn<>("Num. Pagina");
		endLogCol.setCellValueFactory(cellData -> {
			Integer ef = cellData.getValue().getKey();
			
			return new ReadOnlyStringWrapper(ef.toString());
		});

		TableColumn<Entry<Integer, Pagina>, String> endFisCol = new TableColumn<>("# Quadro");
		endFisCol.setCellValueFactory(cellData -> {
			Integer ef = cellData.getValue().getValue().getEndFisico();
			
			return new ReadOnlyStringWrapper(ef.toString());
		});
		
		TableColumn<Entry<Integer, Pagina>, String> ultModCol = new TableColumn<>("Ultima Modificacao");
		ultModCol.setCellValueFactory(cellData -> {
			Date dum = cellData.getValue().getValue().getUltimaUtilizacao();
			
			return new ReadOnlyStringWrapper(dum.toString());
		});
		
		TableColumn<Entry<Integer, Pagina>, Boolean> presenteCol = new TableColumn<>("Bit Presença");
		presenteCol.setEditable(false);
		
		presenteCol.setCellValueFactory(cellData -> {
	        Pagina pag = cellData.getValue().getValue();
			Boolean v =  pag.isPresente();
			return new ReadOnlyBooleanWrapper(v);
		});
		
		TableColumn<Entry<Integer, Pagina>, Boolean> modificadoCol = new TableColumn<>("Bit Modificaçao");
		modificadoCol.setEditable(false);
		
		modificadoCol.setCellValueFactory(cellData -> {
	        Pagina pag = cellData.getValue().getValue();
			Boolean v =  pag.isModificado();
			return new ReadOnlyBooleanWrapper(v);
		});
		
		TableColumn<Entry<Integer, Pagina>, Boolean> utilizadoCol = new TableColumn<>("Utilizacao");
		utilizadoCol.setEditable(false);
		
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
	
	public void atualizarRemocao(Processo p) {
		int idProcesso = p.getId();
		TitledPane atual;
		
		for(int i = 0; i < acProcessos.getPanes().size(); i++) {
			atual = acProcessos.getPanes().get(i);
			if((int)(atual.getUserData()) == idProcesso) {
				acProcessos.getPanes().remove(i);
			}
			
		}
	}

}
