package recursos;

import java.util.Date;

public class PaginaMP extends Pagina {

	public PaginaMP(int endFisico) {
		super(endFisico);		
		super.presente = true;
		super.modificado = false;
		super.utilizado = false;
	}
	
	public void desalocar(){
		super.desalocar();
		super.modificado = false;
		super.ultimaUtilizacao = null;
	}
	
	public String toString(){
        return  "Processo: \t" + ((this.dono != null) ? this.dono.getId() : " ") + "\n"
                + "Modificado: \t\t" + ((this.modificado) ? "v" : "f") + "\n"
                + "Utilizado: \t\t\t" + ((this.utilizado) ? "v" : "f") + "\n"
                + "Ultima Util.: \t\t" + ((this.ultimaUtilizacao != null)? this.ultimaUtilizacao.toString().substring(10, 19): 'x') + "\n"
                + "Quadro: \t\t\t" + Long.toString(endFisico) + "\n";
    }
}
