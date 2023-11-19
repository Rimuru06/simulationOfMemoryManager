package excecoes;

public class TamanhoInsuficiente extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3863543978302898841L;
	
	public TamanhoInsuficiente(){
		super();
	}
	
	public TamanhoInsuficiente(String msg){
		super(msg);
	}
}
