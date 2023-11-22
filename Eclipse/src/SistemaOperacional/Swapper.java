package SistemaOperacional;

import java.util.LinkedList;
import java.util.List;

import config.Configuracao;
import essenciais.GerenciadorDisco;
import essenciais.GerenciadorMemoria;
import essenciais.Pagina;
import essenciais.Processo;
import excecoes.TamanhoInsuficiente;

public abstract class Swapper {
	protected GerenciadorMemoria gm;
	protected GerenciadorDisco gd;
	protected List<Processo> processosModificados;
	
	public Swapper(GerenciadorMemoria gm, GerenciadorDisco gd){
		this.gm = gm;
		this.gd = gd;
	}
	
	public List<Processo> getProcessosModificados(){
		return processosModificados;
	}
	
	public void resetaProcessosModificados(){
		this.processosModificados = new LinkedList<>();
	}
	
	public void swapIn(Processo p) throws TamanhoInsuficiente{
		for(Pagina pag: p.getTabela().getPaginas()){
			this.swapIn(pag);
		}
		p.pronto();
	}

	public void swapOut(Processo p) throws TamanhoInsuficiente{
		for(Pagina pag: p.getTabela().getPaginas()){
			this.removePagMP(pag);
		}
		p.suspender();
	}

	public void swapIn(Pagina pag) throws TamanhoInsuficiente{
		Configuracao confs = Configuracao.obterInstancia();
		Pagina pagMP = gm.alocarMemoria(pag.getProcesso(), confs.getTamanhoPagina()).get(0);
		pagMP.modificar();
		int nPagina = pag.getProcesso().getTabela().getKey(pag);
		pag.getProcesso().getTabela().substituiPagina(nPagina, pagMP);
		gd.liberaPagina(pag);
	}

	public abstract void swapOut(int tamanho) throws TamanhoInsuficiente;

	protected void _swapOut(Pagina p) throws TamanhoInsuficiente{
		Processo alvo = p.getProcesso();
		this.removePagMP(p);
		Configuracao confs = Configuracao.obterInstancia();
		if(!this.processosModificados.contains(alvo))
			this.processosModificados.add(alvo);
		
		if(alvo.getTabela().getTamanho() < confs.getQuantidadeInicialPaginas()) {
			swapOut(alvo);
		}
	}
	
	private void removePagMP(Pagina p) throws TamanhoInsuficiente{
		Processo alvo = p.getProcesso();
		int nPagina = alvo.getTabela().getKey(p);
		
		if(p.isModificado()){
			Pagina pagDisco = gd.alocarMemoria(alvo, 1).get(0);
			alvo.getTabela().substituiPagina(nPagina, pagDisco);;
			
		} else {
			alvo.getTabela().getPaginas().get(nPagina).setDono(new Processo(0, 0, null));
			alvo.getTabela().getPaginas().remove(nPagina);
		}
		gm.liberarMemoria(p);
	}
}
