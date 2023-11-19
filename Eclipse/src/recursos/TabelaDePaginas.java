package recursos;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import controle.Configuracao;
import excecoes.FaltaDePagina;

public class TabelaDePaginas {

	private static Configuracao confs = Configuracao.obterInstancia();
	private int tamanho;
	private HashMap<Integer, Pagina> paginas;

	public TabelaDePaginas(int linhas, List<Pagina> pgs) {
		paginas = new HashMap<>();
		for(int i=0; i<linhas; i++){
			this.insertPagina(pgs.get(i), i);
		}
	}

	public int getTamanho() {
		return confs.getTamanhoPagina()*paginas.size();
	}
	
	public int getQuantidadeEntradas(){
		return paginas.size();
	}
	
	public void insertPagina(Pagina p, int nPagina){
		paginas.put(nPagina, p);
	}
	
	public void removePagina(int nPagina){
		this.paginas.remove(nPagina);
	}

	public List<Pagina> getPaginas() {
		return Collections.list(Collections.enumeration(paginas.values()));
	}
	
	public HashMap<Integer, Pagina> getHash(){
		return this.paginas;
	}
	
	public int getEndPagina(int nPagina) throws FaltaDePagina {
		if(paginas.containsKey(nPagina) && paginas.get(nPagina).isPresente())		
			return paginas.get(nPagina).getEndFisico();
		else
			throw new FaltaDePagina();		
	}
	
	public Pagina getPagina(int nPagina){
		if(paginas.containsKey(nPagina))
			return paginas.get(nPagina);
		else
			return null;
	}
	
	public void substituiPagina(int nPagina,Pagina p){
		this.paginas.replace(nPagina, p);
	}
	
	public int getKey(Pagina p){
		for(Entry<Integer, Pagina> e: this.paginas.entrySet()){
			if(e.getValue().equals(p)) return e.getKey().intValue();
		}
		return -1;
	}
}