package ui;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Base extends Application {

	@Override
	public void start(Stage fundo) throws Exception {
		
		URL fxmlUrl = this.getClass()
                .getClassLoader()
                .getResource("resources/fxml/interface.fxml");
				
		VBox estrutura;
		
		Scene cena;
		
		try{
			estrutura = FXMLLoader.<VBox>load(fxmlUrl);
			
			cena = new Scene(estrutura, 1024, 700);
			
			fundo.setScene(cena);
			fundo.setTitle("Gerenciador de Memória Virtual");
			fundo.show();
		} catch(IOException e){
			throw new IOException("Arquivo Corrompido: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
