package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import recursos.Processo;
import so.Kernel;

public class AbaProcessos extends Tab {
	
	private StackPane base;
	
	private Kernel ker;
	
	private ControladorAbaProcessos controlador;
	
	private List<Processo> existentes;
	
	public AbaProcessos(String text, Kernel k) {
		this.setText(text);
		this.ker = k;
		this.existentes = k.todosProcessos();
		
		init();
	}

	public void init() {
		
		
		FXMLLoader loader = new FXMLLoader(this.getClass()
                .getClassLoader()
                .getResource("resources/fxml/abaProcessos.fxml"));
		
		try{
			base = loader.<StackPane>load();
			
			controlador = loader.<ControladorAbaProcessos>getController(); 
			controlador.initData(this.ker);
			
			this.setContent(base);

		} catch(IOException e){
			System.out.println("Erro no carregamento da aba " + e.getMessage());
		}
	}
	
	public void atualizar(){
		List<TitledPane> processos = new ArrayList<>();		
		
		for(Processo p : ker.todosProcessos()){
			if(!existentes.contains(p)){
				existentes.add(p);
				processos.add(controlador.criarPainelProcesso(p));
			}
		}
		
		controlador.getBase().getPanes().addAll(processos);
	}
	
	public void atualizarTabela(Processo p){
		
	}
}
