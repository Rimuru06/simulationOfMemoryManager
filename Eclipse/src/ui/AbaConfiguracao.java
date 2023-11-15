package ui;

import java.io.IOException;

import controle.Configuracao;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

public class AbaConfiguracao extends Tab {
	private GridPane base;
	static private Configuracao conf = Configuracao.obterInstancia();

	public AbaConfiguracao(String text) {
		this.setText(text);

		init();
	}

	public void init(){
		ControladorAbaConfiguracao controlador;
		
		FXMLLoader loader = new FXMLLoader(this.getClass()
                .getClassLoader()
                .getResource("resources/fxml/abaConfiguracao.fxml"));
		
		try{
			base = loader.<GridPane>load();
			
			controlador = loader.<ControladorAbaConfiguracao>getController(); 
			controlador.initData(conf);		

			this.setContent(base);
		} catch(IOException e){
			System.out.println("Erro no carregamento da aba " + e.getMessage());
		}
	}

}