package main;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Entrada{
	public static void main(String[] args){
		/*
		 *	TODO: 
		 *		- Ler arquivo
		 *
		 * */
		try{
			Scanner leitor = new Scanner(new File("src/resources/entrada.txt"));
			String linha;
			String[] partes;
			List<Processo> procs = new ArrayList<Processo>();
			Processo atual;

			while(leitor.hasNextLine()){
				linha = leitor.nextLine();
				partes = linha.split(" ");

				//Obter processo
				atual = obterProcessos(partes[0], procs);

				//Obter ação
				switch(partes[1].charAt(0)){
					case 'C':
						break;
					case 'R':
						break;
					case 'P':
						break;
					case 'W':
						break;
					case 'I':
						break;
					default:
						break;
				}
				
				//Obter parâmetro (definir aqui tamanhos de página, quadro, etc)
				System.out.println(linha);
			}
		} catch (FileNotFoundException e) {
			
		}
	}
}
