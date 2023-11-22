package config;

public class Configuracao {

	private static volatile Configuracao instancia;
	private int quantidadeInicialPaginas = 1;
	private int tamanhoPagina = 16;
	private int enderecoLogico;
	private int tamanhoTotalMP = 1024;
	private int tamanhoTotalMS = 2048;
	private int tamanhoMaximoProcesso = 1024;
	private int swp = 0;

	private Configuracao() {
	}

	public static Configuracao obterInstancia() {
		if (instancia == null) {
			synchronized (Configuracao.class) {
				if (instancia == null) {
					instancia = new Configuracao();
				}
			}
		}
		return instancia;
	}


	public int getQuantidadeInicialPaginas() {
		return quantidadeInicialPaginas;
	}


	public int getTamanhoPagina() {
		return this.tamanhoPagina;
	}
	
	public int getTamanhoPagina(int qtd) {
		return this.tamanhoPagina*qtd;
	}


	public int getEnderecoLogico() {
		return this.enderecoLogico;
	}


	public int getTamanhoTotalMP() {
		return this.tamanhoTotalMP;
	}


	public int getTamanhoTotalMS() {
		return this.tamanhoTotalMS;
	}

	

	public int getTamanhoMaximoProcesso() {
		return this.tamanhoMaximoProcesso;
	}
	
	public int getQuantidadePaginas(int tam){
		int qtdPaginas = tam / this.getTamanhoPagina();
		if(tam % this.getTamanhoPagina() > 0) qtdPaginas++;
		return qtdPaginas;
	}
	
	
	public int getSwapper(){
		return this.swp;
	}
	
	
}
