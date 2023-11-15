package ui;

import java.net.URL;
import java.util.ResourceBundle;

import controle.Configuracao;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class ControladorAbaConfiguracao {

	private Configuracao conf;
	
	@FXML
	GridPane base;
	
	@FXML
	TextField tfQtdInicialPaginas,
			  tfTamanhoPagina,
			  tfEndLogico,
			  tfTamanhoMP,
			  tfTamanhoMS,
			  tfTamanhoMaximoProcesso;
	
	@FXML
	ToggleGroup metodo;
	
	@FXML
	RadioButton lru, 
				rl;
	
	@FXML
	private URL location;

	@FXML
	private ResourceBundle resources;

	public ControladorAbaConfiguracao() {
	}
	
	@FXML
	private void initialize() {
	}
	
	public void initData(Configuracao conf) {
		this.conf = conf;
		tfQtdInicialPaginas.setText(Integer.toString(conf.getQuantidadeInicialPaginas()));
		tfTamanhoMaximoProcesso.setText(Integer.toString(conf.getTamanhoMaximoProcesso()));
		tfEndLogico.setText(Integer.toString(conf.getEnderecoLogico()));
		tfTamanhoMP.setText(Integer.toString(conf.getTamanhoTotalMP()));
		tfTamanhoMS.setText(Integer.toString(conf.getTamanhoTotalMS()));
		tfTamanhoPagina.setText(Integer.toString(conf.getTamanhoPagina()));	
		
		if(conf.getSwapper() == 0)
			metodo.selectToggle(rl);
		else
			metodo.selectToggle(lru);
		
		
		tfQtdInicialPaginas.textProperty().addListener(this::qtdInicialMudou);
		tfTamanhoMaximoProcesso.textProperty().addListener(this::tamanhoMaxMudou);
		tfEndLogico.textProperty().addListener(this::endLogMudou);
		tfTamanhoMP.textProperty().addListener(this::tamMPMudou);
		tfTamanhoMS.textProperty().addListener(this::tamMSMudou);
		tfTamanhoPagina.textProperty().addListener(this::tamPagMudou);
			
		metodo.selectedToggleProperty().addListener(this::mudouMetodo);
	}
	
	public void qtdInicialMudou(ObservableValue<? extends String> prop, 
            String oldValue, 
            String newValue) {
		conf.setQuantidadeInicialPaginas(Integer.parseInt(newValue));
	}
	
	public void tamanhoMaxMudou(ObservableValue<? extends String> prop, 
            String oldValue, 
            String newValue) {
		conf.setTamanhoMaximoProcesso(Integer.parseInt(newValue));
	}
	
	public void endLogMudou(ObservableValue<? extends String> prop, 
            String oldValue, 
            String newValue) {
		conf.setEnderecoLogico(Integer.parseInt(newValue));
	}
	public void tamMPMudou(ObservableValue<? extends String> prop, 
            String oldValue, 
            String newValue) {
		conf.setTamanhoTotalMP(Integer.parseInt(newValue));
	}
	public void tamMSMudou(ObservableValue<? extends String> prop, 
            String oldValue, 
            String newValue) {
		
		conf.setTamanhoTotalMS(Integer.parseInt(newValue));
	}
	public void tamPagMudou(ObservableValue<? extends String> prop, 
            String oldValue, 
            String newValue) {
		
		conf.setTamanhoPagina(Integer.parseInt(newValue));
	}
	public void mudouMetodo(ObservableValue<? extends Toggle> prop, 
            Toggle oldValue, 
            Toggle newValue) {
		if("Least Recently Used".equals(((Labeled)newValue).getText())){
			conf.setSwapper(1);
		} else {
			conf.setSwapper(0);
		}
	}
	
	public void atualizaValoresConfiguracao(){
		conf.setQuantidadeInicialPaginas(Integer.parseInt(tfQtdInicialPaginas.getText()));
		conf.setTamanhoMaximoProcesso(Integer.parseInt(tfTamanhoPagina.getText()));
		conf.setEnderecoLogico(Integer.parseInt(tfTamanhoPagina.getText()));
		conf.setTamanhoTotalMP(Integer.parseInt(tfTamanhoMP.getText()));
		conf.setTamanhoTotalMS(Integer.parseInt(tfTamanhoMS.getText()));
		conf.setTamanhoPagina(Integer.parseInt(tfTamanhoMaximoProcesso.getText()));
	}
}
