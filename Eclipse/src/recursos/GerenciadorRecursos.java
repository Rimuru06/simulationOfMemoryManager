package recursos;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import controle.Configuracao;
import excecoes.TamanhoInsuficiente;

public abstract class GerenciadorRecursos {

	protected static Configuracao confs = Configuracao.obterInstancia();
	
	protected int tamanhoTotal;
	protected int tamanhoDisponivel;
	protected int tempoLeitura;
	protected List<Pagina> quadros;
	protected List<Pagina> livres;

	protected Queue<Processo> aguardando;
	
	public GerenciadorRecursos(int tamanhoTotal){
		this.tamanhoTotal = tamanhoTotal;
		this.tamanhoDisponivel = tamanhoTotal;
		
		int tamanho = tamanhoTotal / confs.getTamanhoPagina();
		
		this.quadros = new ArrayList<>(tamanho);
		//TODO otimizar
		this.livres = new ArrayList<>(tamanho);
		
		this.aguardando = new LinkedBlockingDeque<Processo>();
	}
	
	public Pagina getQuadroLivre(Processo p) throws TamanhoInsuficiente{
		if(livres.isEmpty()) throw new TamanhoInsuficiente();
		tamanhoDisponivel -= confs.getTamanhoPagina();
		Pagina pag = livres.remove(0);
		pag.alocar(p);
		return pag;
	}
	
	public void liberaQuadro(Pagina pag){
		pag.desalocar();
		this.tamanhoDisponivel += confs.getTamanhoPagina();
		this.livres.add(pag);
		this.livres.sort(null);
	}
	
	public int getTamanhoTotal() {
		return this.tamanhoTotal;
	}

	public int getTamanhoDisponivel() {
		return this.tamanhoDisponivel;
	}
	
	/*	Tendo em mãos o endereço físico gerado pelo processador,
	 * 	usamo-lo para ler/escrever na memoria 
	 *  
	 *  Note que a verificação de limites da tabela de página do processo não fica aqui
	 * */
	public void ler(Processo p, int enderecoFisico) {
		Pagina pag;
		
		/*Marcado para deleção*/
		aguardando.add(p);
		
		pag = quadros.get(enderecoFisico);
		pag.ler();
	}
	
	public void escrever(Processo p, int enderecoFisico) {
		aguardando.add(p);
		quadros.get(enderecoFisico).modificar();
	}
	
	public Queue<Processo> getFila(){
		return this.aguardando;
	} 
	
	public List<Pagina> getQuadros(){
		return this.quadros;
	}
}