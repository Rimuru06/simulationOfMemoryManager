package recursos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Processo {

	private Estado estado;
	private TabelaDePaginas tabela;
	private int id;
	
	private StringProperty estadoStr;

	public Processo(int id, int tamanho, TabelaDePaginas tp){
		this.setId(id);		
		this.estado = Estado.NOVO;
		this.tabela = tp;
		
		estadoStr = new SimpleStringProperty(estado.toString());
	}
	
	public Estado getEstado() {
		return estado;
	}
	
	public StringProperty getEstadoStr(){
		return estadoStr;
	}

	public void alocar(){
		this.estado = Estado.PRONTO;
		estadoStr.set(this.estado.toString());
	}
	
	public void pronto(){
		this.estado = Estado.PRONTO;
		estadoStr.set(this.estado.toString());
	}

	public void suspender(){
		this.estado = Estado.SUSPENSO;
		estadoStr.set(this.estado.toString());
	}
	
	public void dispachar(){
		this.estado = Estado.EXECUTANDO;
		estadoStr.set(this.estado.toString());
	}

	public void bloquear(){
		this.estado = Estado.BLOQUEADO;
		estadoStr.set(this.estado.toString());
	}

	public void terminar(){
		this.estado = Estado.TERMINADO;
		estadoStr.set(this.estado.toString());
	}
	
	public TabelaDePaginas getTabela() {
		return tabela;
	}
	
	/*	Visibilidade para o pacote*/
	void setTabela(TabelaDePaginas tp){
		this.tabela = tp;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}
