package essenciais;

import java.util.Date;

public abstract class Pagina implements Comparable<Pagina> {

	protected Date ultimaUtilizacao;
	protected boolean modificado;
	protected boolean utilizado;
	protected boolean presente;
	protected int endFisico;
	protected Processo dono;

	public Pagina(int endFisico){
		this.ultimaUtilizacao = null;
		this.endFisico = endFisico;
	}
	
	public Processo getProcesso(){
		return dono; 
	}
	
	public void setDono(Processo p) {
		this.dono = p;
	}
	
	public int getEndFisico(){
		return endFisico;
	}
	
	public Date getUltimaUtilizacao() {
		return ultimaUtilizacao;
	}

	public boolean isModificado() {
		return this.modificado;
	}
	
	public boolean isBloqueado() {
		
		if(this.dono.getEstado() == Estado.BLOQUEADO) {
			return true;
		}
		return false;
	}

	public void modificar(){
		this.modificado = true;
		this.ultimaUtilizacao = new Date();		
	}
	
	public void ler(){
		this.ultimaUtilizacao = new Date();	
		this.utilizado = true;
	}
	
	public void alocar(Processo p){
		this.dono = p;
		this.ultimaUtilizacao = new Date();
		this.utilizado = true;
	}
	
	public void desalocar(){
		this.dono = null;
		this.utilizado = false;
	}
	
	public void inutilizado(){
		this.utilizado = false;
	}
	
	public void utilizado(){
		this.utilizado = true;
	}
	
	public boolean isUtilizado() {
		return utilizado;
	}

	public boolean isPresente() {
		return presente;
	}
	
	
	public void limpar(){
		this.modificado = false;
		this.utilizado = false;
	}
	
	public int compareTo(Pagina p){
		return this.getEndFisico() - p.endFisico;
	}
	
	public void setUtilizacao() {
		this.ultimaUtilizacao = new Date();
	}
	
	@Override
	public abstract String toString();
}