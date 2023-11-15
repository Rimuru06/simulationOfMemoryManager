package util.so;

import util.ambiente.*;

enum Estado{
	NOVO, PRONTO, EXECUTANDO, BLOQUEADO, SUSPENSO, TERMINADO
}

public class Processo{
	private Estado estado;
	private TabelaDePaginas tabela;
	int id;

	public Processo(int id, int tamanho){
		this.id = id;
		if(tamanho <= 0) 
			throw new IllegalArgumentException("Tamanho Inválido.\n");
		this.estado = Estado.NOVO;
		this.tabela = new TabelaDePaginas(tamanho / 4);
	}

	public void alocar(){
		this.estado = Estado.PRONTO;
	}

	public void suspender(){
		this.estado = Estado.SUSPENSO;
	}
	
	public void dispachar(){
		this.estado = Estado.EXECUTANDO;
	}

	public void bloquear(){
		this.estado = Estado.BLOQUEADO;
	}

	public void terminar(){
		this.estado = Estado.TERMINADO;
	}
}
