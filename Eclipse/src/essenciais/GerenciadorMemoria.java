package essenciais;

import java.util.ArrayList;
import java.util.List;

import excecoes.TamanhoInsuficiente;

public class GerenciadorMemoria extends GerenciadorRecursos {
	
	public GerenciadorMemoria() {
		super(confs.getTamanhoTotalMP());
		
		Pagina atual;
		
		for(int i = 0; i < confs.getQuantidadePaginas(this.tamanhoTotal); i++){
			atual = new PaginaMP(i);
			
			this.quadros.add(atual);
			this.livres.add(atual);
		}
	}

	public List<Pagina> alocarMemoria(Processo p, int tamanho) throws TamanhoInsuficiente {
		
		int qtdPaginas;
		List<Pagina> pgs;
		
		if(tamanho > tamanhoDisponivel) throw new TamanhoInsuficiente();
		
		qtdPaginas = confs.getQuantidadePaginas(tamanho);
		
		pgs = new ArrayList<>(qtdPaginas);
		
		for(int i = 0; i < qtdPaginas ; i++){
			pgs.add(super.getQuadroLivre(p));
		}
		
		this.tamanhoDisponivel -= tamanho; 
		
		return pgs;
	}

	public void liberarMemoria(Processo p) {
		TabelaDePaginas tp = p.getTabela();
		
		
		for(Pagina pag: p.getTabela().getHash().values()) {
			if(pag.getProcesso() == p) {
				super.setTamanhoDisponivel(super.getTamanhoDisponivel() + confs.getTamanhoPagina()); 
			}
			this.liberarMemoria(pag);	
		}
		
		

	}
	
	public void liberarMemoria(Pagina p) {
		super.liberaQuadro(p);
	}


}
