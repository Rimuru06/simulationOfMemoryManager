package recursos;

import java.util.ArrayList;
import java.util.List;

import controle.Configuracao;
import excecoes.TamanhoInsuficiente;
import so.Swapper;

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
			/*	TODO: Modificar para conseguir quadro do disco*/
		} catch (TamanhoInsuficiente e) {
			swp.swapOut(confs.getTamanhoPagina(qtdPaginas - i));
			for(; i < qtdPaginas; i++)
				pgs.add(gm.getQuadroLivre(p));
		}
		
		return pgs;
	}
	
	/*	TODO: Criar função alocarQuadros, refatorar funcao alocarMemoria para alocarPaginas*/

}
