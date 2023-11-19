package so;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import controle.Configuracao;
import excecoes.FaltaDePagina;
import excecoes.ProcessoInexistente;
import excecoes.TamanhoInsuficiente;
import recursos.Estado;
import recursos.GerenciadorDisco;
import recursos.GerenciadorDispositivo;
import recursos.GerenciadorMemoria;
import recursos.GerenciadorMemoriaVirtual;
import recursos.Pagina;
import recursos.Processo;
import recursos.TabelaDePaginas;

public class Kernel {
	
	private HashMap<Integer, Processo> listaProcessos; 
	private GerenciadorMemoria gm;
	private GerenciadorDisco gd;
	private GerenciadorDispositivo gp;
	private GerenciadorMemoriaVirtual gmv;
	private Swapper swp;
	
	public Kernel(GerenciadorMemoria gm, GerenciadorDisco gd, Swapper swp){
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
		// swapp out
		swp.swapOut(tamanho);
	}
	
	private Pagina tratarSwappIn(int nPagina, Processo pros) throws TamanhoInsuficiente{
		Pagina pagMP = null;
		try {
			Pagina pSwapp = pros.getTabela().getPagina(nPagina);
			swp.swapIn(pSwapp);
		} catch (TamanhoInsuficiente e) {
			Configuracao confs = Configuracao.obterInstancia();
			tratarTamanhoInsuficiente(confs.getTamanhoPagina());
			Pagina pSwapp = pros.getTabela().getPagina(nPagina);
			swp.swapIn(pSwapp);
		}
		return pagMP;
	}
	
	// Pagina nao esta em MP nem em Swapp
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
		
		/*	TODO: Modificar para ajuste de locais das páginas*/
		list = gmv.alocarMemoria(p, tamanho);
		
		for(int i = 0; i < list.size(); i++){
			p.getTabela().insertPagina(list.get(i), i);
		}
		
		// Colocar na lista do escalonador
		listaProcessos.put(id, p);
		
		return p;
	}
	
	/*	TODO: criar função alocarProcesso, que leva Páginas da MS para a MP*/

	public void le(int id, int pos) throws TamanhoInsuficiente{
		
		Processo p = this.listaProcessos.get(id);
		Pagina pag;
		int endFisico = this.descobreEnderecoFisico(p, pos);
		
		// Executa
		gm.ler(p, endFisico);
		
	}
	
	public void escreve(int id, int pos) throws TamanhoInsuficiente{
		Processo p = this.listaProcessos.get(id);
		int endFisico = this.descobreEnderecoFisico(p, pos);
		// Executa
		gm.escrever(p, endFisico);
		// Retorna
		int t = 0;
	}
	
	public void processa(int id, int pos) throws TamanhoInsuficiente{
		Configuracao confs = Configuracao.obterInstancia();
		// Resolve endereco: n da pagina + offset
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
	
	// TODO usa dispositivo
	public void usaDispositivo(int id, int dispositivo){
		
	}
	
	public int descobreEnderecoFisico(Processo p, int pos) throws TamanhoInsuficiente{
		Configuracao confs = Configuracao.obterInstancia();
		
		// Resolve endereco: n da pagina + offset
		int nPagina = pos / confs.getTamanhoPagina();
		int offset = pos % confs.getTamanhoPagina();
		
		// Pega o endereco da pagina
		TabelaDePaginas tp = p.getTabela();
		int endFisico = -1;
		
		try {
			endFisico = tp.getEndPagina(nPagina);
		} catch (FaltaDePagina e) {
			// Se ocorreu falta de pagina, logo ou a pagina nao esta em swap
			// ou em ms
			Pagina pagina = tp.getPagina(nPagina);
			// Nao esta presente, pois ocorreu falta de paginas
			if(pagina != null)
				endFisico = tratarSwappIn(nPagina, p).getEndFisico();
			else
				endFisico = tratarPaginaMS(nPagina, p).getEndFisico();
			
		}
		
		return endFisico;
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
