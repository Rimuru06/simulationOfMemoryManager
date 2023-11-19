package recursos;

public class PaginaMS extends Pagina {

	public PaginaMS(int endFisico) {
		super(endFisico);
		super.presente = false;
		super.modificado = true;
	}
	
	@Override
	public String toString(){
		return  "Processo: \t" + ((this.dono != null) ? this.dono.getId() : "") + "\n";
	}

}
