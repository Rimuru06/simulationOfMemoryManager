package so;

import recursos.Estado;
import recursos.GerenciadorRecursos;
import recursos.Processo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Escalonador{

	
	protected Queue<Processo> filaProntos = new LinkedList<Processo>();
	protected List<Processo> suspensos = new ArrayList<Processo>();
	protected HashMap<String, GerenciadorRecursos> bloqueados = new HashMap<>();
	
	public Escalonador(){
		
	}

	public Processo selecionaProximoProcesso(){
		return filaProntos.remove();
	}
	
	public void terminarProcesso(Processo p){
		Estado e = p.getEstado();
		if(e == Estado.PRONTO){
			filaProntos.remove(p);
		} else if(e == Estado.SUSPENSO){
			suspensos.remove(p);
		} else if(e == Estado.BLOQUEADO){
			
		}
		
	}
	
	public void aprontarProcesso(Processo p){
		filaProntos.add(p);
		// Nome estranho, trocar?
		p.alocar();
	}
	
	// realmente necessario?
	public void bloquearProcesso(Processo p, GerenciadorRecursos gr){
		p.bloquear();
		filaProntos.remove(p);		
	}
	
	public void suspenderProcesso(Processo p){
		p.suspender();
		filaProntos.remove(p);
		suspensos.add(p);
	}

}