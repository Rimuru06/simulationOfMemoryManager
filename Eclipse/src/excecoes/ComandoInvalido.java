package excecoes;

public class ComandoInvalido extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6283623537491709718L;

	public ComandoInvalido(){
		super();
	}
	
	public ComandoInvalido(String msg){
		super(msg);
	}
	
}
