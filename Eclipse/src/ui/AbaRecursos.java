package ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import recursos.GerenciadorRecursos;
import recursos.Pagina;
import recursos.PaginaMP;
import recursos.TabelaDePaginas;

public class AbaRecursos extends Tab {
	
	private HBox base;
	private GerenciadorRecursos gerRec;
	
	private ControladorAbaRecursos controlador;

	public AbaRecursos(String text, GerenciadorRecursos ger) {
		this.setText(text);
		this.gerRec = ger;

		init();
	}

	public void init(){
		
		FXMLLoader loader = new FXMLLoader(this.getClass()
                .getClassLoader()
                .getResource("resources/fxml/abaRecursos.fxml"));
		
		try{
			base = loader.<HBox>load();
			
			controlador = loader.<ControladorAbaRecursos>getController(); 
			controlador.initData(gerRec);		
			
			this.setContent(base);
		} catch(IOException e){
			System.out.println("Erro no carregamento da aba " + e.getMessage());
		}
	}
	
	public void atualizar(Pagina p){
		controlador.atualizar(p);
	}
	
	public void atualizar(TabelaDePaginas tp, Class<? extends Pagina> tipo){
		for(Pagina p : tp.getPaginas()){
			if(tipo.isInstance(p)){
				controlador.atualizar(p);
			}
		}
	}
	
}
