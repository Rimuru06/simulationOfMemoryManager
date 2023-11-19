package excecoes;

public class ProcessoInexistente extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2202552226703261637L;

	public ProcessoInexistente(){
		super();
	}
	
	public ProcessoInexistente(String msg){
		super(msg);
	}
}
