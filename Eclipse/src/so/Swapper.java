package so;

import java.util.LinkedList;
import java.util.List;

import controle.Configuracao;
import excecoes.TamanhoInsuficiente;
import recursos.GerenciadorDisco;
import recursos.GerenciadorMemoria;
import recursos.Pagina;
import recursos.Processo;

public abstract class Swapper {
	/*
	 *	1. Find the location of the desired page on the disk.
	 *	2. Find a free frame:
	 *	a. If there is a free frame, use it.
	 *	b. If there is no free frame, use a page-replacement algorithm to select
	 *	a victim frame.
	 *	c. Write the victim frame to the disk; change the page and frame tables
	 *	accordingly.
	 *	3. Read the desired page into the newly freed frame; change the page and
	 *	frame tables.
	 *	4. Continue the user process from where the page fault occurred.
	 */
	
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
	
	/*
	 * 	swap-in-processo: Trazer da memória processo inteiro
	 * */
	public void swapIn(Processo p) throws TamanhoInsuficiente{
		for(Pagina pag: p.getTabela().getPaginas()){
			this.swapIn(pag);
		}
		p.pronto();
	}
	
	/*
	 * 	swap-out-processo: Guardar na memória processo inteiro
	 * */
	public void swapOut(Processo p) throws TamanhoInsuficiente{
		for(Pagina pag: p.getTabela().getPaginas()){
			this.removePagMP(pag);
		}
		p.suspender();
	}
	
	/*
	 *	Swap-in: Traz página da memória secundária para a memória principal
	 * */
	public void swapIn(Pagina pag) throws TamanhoInsuficiente{
		Configuracao confs = Configuracao.obterInstancia();
		//Tenta alocar memoria
		Pagina pagMP = gm.alocarMemoria(pag.getProcesso(), confs.getTamanhoPagina()).get(0);
		// Se estava em swapp esta modificada
		pagMP.modificar();
		//Tira pagina da MS e coloca na MP
		int nPagina = pag.getProcesso().getTabela().getKey(pag);
		pag.getProcesso().getTabela().substituiPagina(nPagina, pagMP);
		gd.liberaPagina(pag);
	}
	
	/*
	 * 	Swap-out: Guarda pagina na memoria secundaria
	 * */
	public abstract void swapOut(int tamanho) throws TamanhoInsuficiente;
	
	// Retira efetivamente uma pagina p da MP
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
		// Procurar processo que possui a pagina
		Processo alvo = p.getProcesso();
		int nPagina = alvo.getTabela().getKey(p);
		// Se esta modificado salva no disco
		if(p.isModificado()){
			Pagina pagDisco = gd.alocarMemoria(alvo, 1).get(0);
			alvo.getTabela().substituiPagina(nPagina, pagDisco);;
		} else {
			// Dissocia pagina de processo
			alvo.getTabela().removePagina(nPagina);
		}
		gm.liberarMemoria(p);
	}
	
	

}
