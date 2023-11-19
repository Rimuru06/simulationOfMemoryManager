package excecoes;

public class FaltaDePagina extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6208482526128855032L;

	public FaltaDePagina(){
		super();
	}
	
	public FaltaDePagina(String msg){
		super(msg);
	}
	
}