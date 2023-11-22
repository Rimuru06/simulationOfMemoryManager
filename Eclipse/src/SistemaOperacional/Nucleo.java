package SistemaOperacional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import config.Configuracao;
import essenciais.Estado;
import essenciais.GerenciadorDisco;
import essenciais.GerenciadorDispositivo;
import essenciais.GerenciadorMemoria;
import essenciais.GerenciadorMemoriaVirtual;
import essenciais.Pagina;
import essenciais.Processo;
import essenciais.TabelaDePaginas;
import excecoes.FaltaDePagina;
import excecoes.ProcessoInexistente;
import excecoes.TamanhoInsuficiente;

public class Nucleo {
	
	private HashMap<Integer, Processo> listaProcessos; 
	private GerenciadorMemoria gm;
	private GerenciadorDisco gd;
	private GerenciadorDispositivo gp;
	private GerenciadorMemoriaVirtual gmv;
	private Swapper swp;
	
	public Nucleo(GerenciadorMemoria gm, GerenciadorDisco gd, Swapper swp){
		this.gm = gm;
		this.gd = gd;
		this.swp = swp;
		this.gp = new GerenciadorDispositivo();
		this.gmv = new GerenciadorMemoriaVirtual(gm, gd, swp);
		this.listaProcessos = new HashMap<>();
	}
	
	public void resetarEstados(){
		this.swp.resetaProcessosModificados();
		for(Processo p: listaProcessos.values()){
			if(p.getEstado() != Estado.SUSPENSO)
				p.pronto();
		}
	}
	
	private void tratarTamanhoInsuficiente(int tamanho) throws TamanhoInsuficiente {
		swp.swapOut(tamanho);
	}
	
	private Pagina tratarSwappIn(int nPagina, Processo pros) throws TamanhoInsuficiente{
		Pagina pagMP = null;
		try {
			Pagina pSwapp = pros.getTabela().getPagina(nPagina);
			swp.swapIn(pSwapp);
			pagMP = pSwapp;
		} catch (TamanhoInsuficiente e) {
			Configuracao confs = Configuracao.obterInstancia();
			tratarTamanhoInsuficiente(confs.getTamanhoPagina());
			Pagina pSwapp = pros.getTabela().getPagina(nPagina);
			swp.swapIn(pSwapp);
			pagMP = pSwapp;
		}
		return pagMP;
		
	}
	
	private Pagina tratarPaginaMS(int nPagina, Processo p) throws TamanhoInsuficiente{
		Configuracao confs = Configuracao.obterInstancia();
		Pagina pagMP = gmv.alocarMemoria(p, confs.getTamanhoPagina()).get(0);
		p.getTabela().insertPagina(pagMP, nPagina);
		return pagMP;
	}

	public Processo obterProcesso(int id) throws ProcessoInexistente {
		Processo p = listaProcessos.get(id);
		
		if(p == null) throw new ProcessoInexistente("Processo nao existe.");
		
		return p;
	}
	
	public Processo criarProcesso(int id, int tamanho) 
			throws TamanhoInsuficiente{
		
		Processo p = null;
		Configuracao confs = Configuracao.obterInstancia();
		List<Pagina> list;
		int tamanhoInicial = confs.getQuantidadeInicialPaginas() * confs.getTamanhoPagina();
		
		if(tamanho < tamanhoInicial)
			tamanho = tamanhoInicial;
		
		p = new Processo(id, tamanho, new TabelaDePaginas(0, null));
		
		list = gmv.alocarMemoria(p, tamanho);
		
		for(int i = 0; i < list.size(); i++){
			p.getTabela().insertPagina(list.get(i), i);
		}
		
		listaProcessos.put(id, p);
		
		return p;
	}

	public void le(int id, int pos) throws TamanhoInsuficiente{
		
		Processo p = this.listaProcessos.get(id);
		Pagina pag;
		int endFisico = this.descobreEnderecoFisico(p, pos);
		
		gm.ler(p, endFisico);
		
	}
	
	public void escreve(int id, int pos) throws TamanhoInsuficiente{
		Processo p = this.listaProcessos.get(id);
		int endFisico = this.descobreEnderecoFisico(p, pos);
		gm.escrever(p, endFisico);
		int t = 0;
	}
	
	public void processa(int id, int pos) throws TamanhoInsuficiente{
		Configuracao confs = Configuracao.obterInstancia();
		int nPagina = pos/confs.getTamanhoPagina();

		Processo p = this.listaProcessos.get(id);
		Pagina pagina = p.getTabela().getPagina(nPagina);
		
		if(pagina == null)
			tratarPaginaMS(nPagina, p).utilizado();
		else if(pagina.isPresente())
			pagina.utilizado();
		else 
			tratarSwappIn(nPagina, p).utilizado();	
	}
	
	public void usaDispositivo(int id, int dispositivo){
		
	}
	
	public int descobreEnderecoFisico(Processo p, int pos) throws TamanhoInsuficiente{
		Configuracao confs = Configuracao.obterInstancia();
		
		int nPagina = pos / confs.getTamanhoPagina();
		int offset = pos % confs.getTamanhoPagina();
		
		TabelaDePaginas tp = p.getTabela();
		int endFisico = -1;
		
		try {
			endFisico = tp.getEndPagina(nPagina);
		} catch (FaltaDePagina e) {
			Pagina pagina = tp.getPagina(nPagina);
			if(pagina != null)
				endFisico = tratarSwappIn(nPagina, p).getEndFisico();
			else
				endFisico = tratarPaginaMS(nPagina, p).getEndFisico();
			
		}
		
		return endFisico;
	}
	
	public void terminaProcesso(int id) throws TamanhoInsuficiente{
		
		
		for(Processo proc : listaProcessos.values()) {
			if(proc.getId() == id) {
				gm.liberarMemoria(proc);
				listaProcessos.remove(id);
				break;
			}
		}	
	}
	
	public GerenciadorMemoria obterGerenciadorMP(){
		return this.gm;
	}
	
	public GerenciadorDispositivo obterGerenciadorDP(){
		return this.gp;
	}
	
	public GerenciadorDisco obterGerenciadorMS(){
		return this.gd;
	}
	
	public List<Processo> todosProcessos(){
		return Collections.list(Collections.enumeration(this.listaProcessos.values()));
	}
	
	public List<Processo> processosModificados(){
		return swp.processosModificados;
	}
}
