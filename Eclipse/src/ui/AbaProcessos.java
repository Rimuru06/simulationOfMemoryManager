package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import essenciais.Pagina;
import essenciais.Processo;
import SistemaOperacional.Nucleo;

public class AbaProcessos extends Tab {
	
	private StackPane base;
	
	private Nucleo ker;
	
	private ControladorAbaProcessos controlador;
	
	private HashMap<Integer, TitledPane> mapeamento;
	
	private List<Processo> existentes;
	
	public AbaProcessos(String text, Nucleo k) {
		this.setText(text);
		this.ker = k;
		this.existentes = k.todosProcessos();
		this.mapeamento = new HashMap<>();
		
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
		List<Processo> aux = new ArrayList<>();
		
		int j = 0;
		for(Processo p : ker.todosProcessos()) {
			aux.add(p);
			for(Pagina pag: p.getTabela().getHash().values()) {
				
				if(pag.getProcesso().getId() != p.getId()) {
					j++;
				}
			}
			
			if(j == p.getTabela().getHash().size()) {
				aux.remove(p);
				mapeamento.remove(p.getId());
				
			}
			j = 0;
		}
		
		for(int i = 0; i < aux.size(); i++) {
			if(!existentes.contains(aux.get(i))) {
				existentes.add(aux.get(i));
				mapeamento.put(aux.get(i).getId(), controlador.criarPainelProcesso(aux.get(i)));
			}
			else {
				TitledPane nova = controlador.criarPainelProcesso(aux.get(i));
				for(Entry<Integer, TitledPane> entry:mapeamento.entrySet()) {
					if(entry.getKey().intValue() == aux.get(i).getId()) {
						mapeamento.replace(aux.get(i).getId(), nova);
					}
				}
				
			}
		}
		
		
		processos = Collections.list(Collections.enumeration(mapeamento.values()));
		controlador.getBase().getPanes().setAll(processos);
	}
	
	public void atualizarRemocao() {
		
		for(Processo p: existentes) {
			if(!ker.todosProcessos().contains(p)) {
				existentes.remove(p);
				mapeamento.remove(p.getId());
				controlador.atualizarRemocao(p);
				break;
			}
		}
	}
	
	
	
}
