package essenciais;

import java.util.ArrayList;
import java.util.List;

import excecoes.TamanhoInsuficiente;

public class GerenciadorDisco extends GerenciadorRecursos {
	public GerenciadorDisco() {
		super(confs.getTamanhoTotalMS());
		
		Pagina atual;
		
		for(int i = 0; i < confs.getQuantidadePaginas(this.tamanhoTotal); i++){
			atual = new PaginaMS(i);
			
			this.quadros.add(atual);
			this.livres.add(atual);
		}
	}
	
	public List<Pagina> alocarMemoria(Processo p, int qtdPaginas) throws TamanhoInsuficiente {
		
		List<Pagina> pgs;
		
		if(qtdPaginas*confs.getTamanhoPagina() > tamanhoDisponivel) throw new TamanhoInsuficiente();

		
		pgs = new ArrayList<>(qtdPaginas);
		
		for(int i = 0; i < qtdPaginas ; i++){
			pgs.add(super.getQuadroLivre(p));
		}
		
		this.tamanhoDisponivel -= qtdPaginas*confs.getTamanhoPagina(); 
		
		
		return pgs;
	}
	
	public void liberaPagina(Pagina p){
		super.liberaQuadro(p);
	}

	public void liberarMemoria(Processo p) {
		TabelaDePaginas tp = p.getTabela();
		
		this.tamanhoDisponivel += tp.getTamanho();
		livres.addAll(tp.getPaginas());
		livres.sort(null);
		p.setTabela(null);
	}
}
