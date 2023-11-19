package so;

import java.util.Date;

import controle.Configuracao;
import excecoes.TamanhoInsuficiente;
import recursos.GerenciadorDisco;
import recursos.GerenciadorMemoria;
import recursos.Pagina;

public class SwapperLRU extends Swapper {

	public SwapperLRU(GerenciadorMemoria gm, GerenciadorDisco gd) {
		super(gm, gd);
	}

	@Override
	public void swapOut(int tamanho) throws TamanhoInsuficiente {
		Configuracao confs = Configuracao.obterInstancia();
		int qtdPag = confs.getQuantidadePaginas(tamanho);
		while(qtdPag > 0){
			_swapOut(leastRecentlyUsed());
		}
	}
	
	/*
	 * 	Retorna endereco da pagina eleita para substituicao
	 * */
	private Pagina leastRecentlyUsed(){
		
		/*
		 * Primeira implementacao.
		 * Possibilidades, caso haja tempo:
		 * 	- Usar heaps, atualizar arvore a cada utilizacao das paginas
		 * */
		
		Pagina eleita = null;
		
		long agora = new Date().getTime(),
				tempoEleito = Integer.MAX_VALUE;
		
		for(Pagina pg : gm.getQuadros()){
			if(pg.getUltimaUtilizacao() == null){
				eleita = pg;
				break;
			} else 	if(agora - pg.getUltimaUtilizacao().getTime() < tempoEleito){
				tempoEleito = agora - pg.getUltimaUtilizacao().getTime();
				eleita = pg;
			}
		}
		
		return eleita;
	}

}
