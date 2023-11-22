package essenciais;

import java.util.ArrayList;
import java.util.List;

import SistemaOperacional.Swapper;
import config.Configuracao;
import excecoes.TamanhoInsuficiente;

public class GerenciadorMemoriaVirtual {
	
	private GerenciadorMemoria gm;
	private GerenciadorDisco gd;
	private Swapper swp;

	public GerenciadorMemoriaVirtual(GerenciadorMemoria gm, GerenciadorDisco gd, Swapper swp) {
		this.gm = gm;
		this.gd = gd;
		this.swp = swp;
	}
	
	public List<Pagina> alocarMemoria(Processo p, int tamanho) throws TamanhoInsuficiente{
		
		Configuracao confs = Configuracao.obterInstancia();
		
		int qtdPaginas = confs.getQuantidadePaginas(tamanho),
			i = 0;
		
		List<Pagina> pgs = new ArrayList<>(qtdPaginas);
		
		try{
			for(; i<qtdPaginas; i++)
				pgs.add(gm.getQuadroLivre(p));
		} catch (TamanhoInsuficiente e) {
			swp.swapOut(confs.getTamanhoPagina(qtdPaginas - i));
			for(; i < qtdPaginas; i++)
				pgs.add(gm.getQuadroLivre(p));
		}
		
		return pgs;
	}
	
	

}
