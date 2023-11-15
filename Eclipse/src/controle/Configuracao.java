package controle;

public class Configuracao {

	private static volatile Configuracao instancia;
	private int quantidadeInicialPaginas = 4;
	private int tamanhoPagina = 32;
	private int enderecoLogico;
	private int tamanhoTotalMP = 1024;
	private int tamanhoTotalMS = 2048;
	private int tamanhoMaximoProcesso;
	private int swp = 0;
	
	private int quantidadeInicialPaginasProx = 4;
	private int tamanhoPaginaProx = 32;
	private int enderecoLogicoProx;
	private int tamanhoTotalMPProx = 1024;
	private int tamanhoTotalMSProx = 2048;
	private int tamanhoMaximoProcessoProx;
	private int swpProx = 0;

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

	public void setQuantidadeInicialPaginas(int quantidadeInicialPaginas) {
		this.quantidadeInicialPaginasProx = quantidadeInicialPaginas;
	}

	public int getQuantidadeInicialPaginas() {
		return quantidadeInicialPaginas;
	}

	public void setTamanhoPagina(int tamanhoPagina) {
		this.tamanhoPaginaProx = tamanhoPagina;
	}

	public int getTamanhoPagina() {
		return this.tamanhoPagina;
	}
	
	public int getTamanhoPagina(int qtd) {
		return this.tamanhoPagina*qtd;
	}

	public void setEnderecoLogico(int enderecoLogico) {
		this.enderecoLogicoProx = enderecoLogico;
	}

	public int getEnderecoLogico() {
		return this.enderecoLogico;
	}

	public void setTamanhoTotalMP(int tamanhoTotalMP) {
		this.tamanhoTotalMPProx = tamanhoTotalMP;
	}

	public int getTamanhoTotalMP() {
		return this.tamanhoTotalMP;
	}

	public void setTamanhoTotalMS(int tamanhoTotalMS) {
		this.tamanhoTotalMSProx = tamanhoTotalMS;
	}

	public int getTamanhoTotalMS() {
		return this.tamanhoTotalMS;
	}

	public void setTamanhoMaximoProcesso(int tamanhoMaximoProcesso) {
		this.tamanhoMaximoProcessoProx = tamanhoMaximoProcesso;
	}

	public int getTamanhoMaximoProcesso() {
		return this.tamanhoMaximoProcesso;
	}
	
	public int getQuantidadePaginas(int tam){
		int qtdPaginas = tam / this.getTamanhoPagina();
		if(tam % this.getTamanhoPagina() > 0) qtdPaginas++;
		return qtdPaginas;
	}
	
	public void setSwapper(int n){
		this.swpProx = n;
	}
	
	public int getSwapper(){
		return this.swp;
	}
	
	public void aplicarConfiguracoes(){
		this.enderecoLogico = enderecoLogicoProx;
		this.quantidadeInicialPaginas = quantidadeInicialPaginasProx;
		this.swp = swpProx;
		this.tamanhoMaximoProcesso = tamanhoMaximoProcessoProx;
		this.tamanhoPagina = tamanhoPaginaProx;
		this.tamanhoTotalMP = tamanhoTotalMPProx;
		this.tamanhoTotalMS = tamanhoTotalMSProx;
	}
}
