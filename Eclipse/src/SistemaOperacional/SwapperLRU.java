package SistemaOperacional;

import java.util.Date;
import java.util.Random;

import config.Configuracao;
import essenciais.GerenciadorDisco;
import essenciais.GerenciadorMemoria;
import essenciais.Pagina;
import excecoes.TamanhoInsuficiente;

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
			qtdPag--;
		}
	}

	private Pagina leastRecentlyUsed(){
		long agora = new Date().getTime(), 
				tempoEleito = agora - gm.getQuadros().get(0).getUltimaUtilizacao().getTime();
		Pagina eleita = gm.getQuadros().get(0);
		
		for(int i = 0; i < gm.getQuadros().size(); i++) {
			if(gm.getQuadros().get(i).getUltimaUtilizacao() == null) {
				eleita = gm.getQuadros().get(i);
				break;
			} else if (agora - gm.getQuadros().get(i).getUltimaUtilizacao().getTime() > tempoEleito) {
				tempoEleito = agora - gm.getQuadros().get(i).getUltimaUtilizacao().getTime();
				eleita = gm.getQuadros().get(i);
			}
		}
		return eleita;
	}
}
