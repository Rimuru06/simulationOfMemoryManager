package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import config.Configuracao;
import excecoes.ComandoInvalido;
import excecoes.ProcessoInexistente;
import excecoes.TamanhoInsuficiente;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import essenciais.GerenciadorDisco;
import essenciais.GerenciadorMemoria;
import essenciais.PaginaMP;
import essenciais.PaginaMS;
import essenciais.Processo;
import SistemaOperacional.Nucleo;
import SistemaOperacional.SwapperLRU;

public class Controlador {

	// Interface

	private Window fundo = null;

	private FileChooser navegadorArquivos = new FileChooser();

	@FXML
	private TabPane baseAbas;

	@FXML
	private TextField tfComando;

	@FXML
	private Button bContinuar, bCarregar, bPausar, bAndar, bZerar;

	@FXML
	private URL location;

	@FXML
	private ResourceBundle resources;

	private AbaRecursos abaMemoria, abaDisco;

	private AbaProcessos abaProcessos;

	private Nucleo kernel;

	private String instrucao = null;
	private Scanner leitor = null;
	private File arquivo = null;
	private boolean emExecucao;

	public Controlador() {
	}


	@FXML
	private void initialize() {
		Configuracao confs = Configuracao.obterInstancia();
		GerenciadorMemoria gm = new GerenciadorMemoria();
		GerenciadorDisco gd = new GerenciadorDisco();
		
		if(confs.getSwapper() == 0)
			kernel = new Nucleo(gm, gd, new SwapperLRU(gm, gd));
		

		abaMemoria = new AbaRecursos("Memoria Principal", kernel.obterGerenciadorMP());
		abaDisco = new AbaRecursos("Memoria Secundária", kernel.obterGerenciadorMS());


		abaProcessos = new AbaProcessos("Processos", kernel);
		
		baseAbas.getTabs().addAll(abaDisco, abaMemoria, abaProcessos);
		emExecucao = false;
	}

	@FXML
	private void carregarArquivo() {

		fundo = bCarregar.getScene().getWindow();

		this.navegadorArquivos.setTitle("Selecione um arquivo de " 
				+ " comportamento dos processos:");

		arquivo = navegadorArquivos.showOpenDialog(fundo);

		if (arquivo == null)
			return;

		try {
			leitor = new Scanner(arquivo);

			instrucao = leitor.nextLine();

			tfComando.setText(instrucao);
		} catch (FileNotFoundException e) {
			alertar(e.getMessage(), "Arquivo não encontrado.");
		}
	}

	@FXML
	private void continuar() {
		if (emExecucao)
			return;

		if (instrucao == null || instrucao == "" || leitor == null) {

			alertar("Não há uma instrução a ser executada.", "Sem instruções");
			return;
		}

		emExecucao = true;

		try {
			processarInstrucoes();
		} catch (ComandoInvalido e) {
			alertar(e.getMessage(), "Erro na execução do comando: ");
		}

		emExecucao = false;
	}

	@FXML
	private void pausar() {
		if (emExecucao)
			emExecucao = false;
	}

	@FXML
	private void andar() {
		if (emExecucao)
			return;
		instrucao = tfComando.getText();
		
		emExecucao = true;
		try {
			processarInstrucao(instrucao);
		} catch (ComandoInvalido e) {
			alertar(e.getMessage(), "Erro na execução do comando: ");
		}
		emExecucao = false;

		instrucao = (leitor == null) ? tfComando.getText() : leitor.nextLine();
		tfComando.setText(instrucao);
	}

	@FXML
	private void zerar() {
		Configuracao confs = Configuracao.obterInstancia();
		
		
		baseAbas.getTabs().remove(0, baseAbas.getTabs().size());
		initialize();		
		
		emExecucao = false;
	}

	private void processarInstrucao(String instrucao) throws ComandoInvalido {
		String[] partes;
		Processo atual = null;
		int inicio, 
			 fim;

		if (instrucao == null || "".equals(instrucao)) {
			alertar("Comando inválido", "Comando Inválido");
			return;
		}

		partes = instrucao.split(" ");

		if (partes[1].charAt(0) != 'C') {
			try {
				atual = kernel.obterProcesso(partes[0].charAt(1) - '0');
			} catch (ProcessoInexistente e) {
				alertar(e.getMessage(), "Processo nao existe");

				return;
			}
		}
		kernel.resetarEstados();
		switch (partes[1].charAt(0)) {
		case 'C':
			try {
				atual = kernel.criarProcesso(partes[0].charAt(1) - '0', 
											 Integer.parseInt(partes[2]));
				
				abaProcessos.atualizar();
				abaMemoria.atualizar(atual.getTabela(), PaginaMP.class);
				abaDisco.atualizar(atual.getTabela(), PaginaMS.class);
				
			} catch (NumberFormatException e){
				e.printStackTrace();
			} catch (TamanhoInsuficiente e) {
				alertar("Tamanho Insuficiente de memoria", 
						"Erro na criacao do processo");
			}

			break;
		case 'R':
			try {
				inicio = partes[2].indexOf('(');
				fim = partes[2].indexOf(')');
				
				kernel.le(partes[0].charAt(1)-'0', 
						Integer.parseInt(partes[2].substring(inicio + 1, fim)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (TamanhoInsuficiente e) {
				alertar("Tamanho Insuficiente de memoria", "Nao foi possivel acessar a pagina");
			}
			
			break;
		case 'P':
			try {
				inicio = partes[2].indexOf('(');
				fim = partes[2].indexOf(')');
				kernel.processa(partes[0].charAt(1)-'0', Integer.parseInt(partes[2].substring(inicio + 1, fim)));
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (TamanhoInsuficiente e) {
				alertar("Tamanho Insuficiente de memoria", "Nao foi possivel acessar a pagina");
			}
			break;
		case 'W':
			try {
				inicio = partes[2].indexOf('(');
				fim = partes[2].indexOf(')');
				
				kernel.escreve(partes[0].charAt(1)-'0', 
						Integer.parseInt(partes[2].substring(inicio + 1, fim)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (TamanhoInsuficiente e) {
				alertar("Tamanho Insuficiente de memoria", "Nao foi possivel acessar a pagina");
			}
			break;
		case 'I':
			inicio = partes[2].indexOf('(');
			fim = partes[2].indexOf(')');
			
			kernel.obterGerenciadorDP().ler(atual, Integer.parseInt(partes[2].substring(inicio + 1, fim)));
			break;
		case 'T':
			try{
				kernel.terminaProcesso(partes[0].charAt(1) - '0');
				abaProcessos.atualizarRemocao();
				abaProcessos.atualizar();
				abaMemoria.atualizar(atual.getTabela(), PaginaMP.class);
				abaDisco.atualizar(atual.getTabela(), PaginaMS.class);
			} catch(TamanhoInsuficiente e) {
				alertar("Tamanho Insuficiente de memoria", "Nao foi possivel acessar a pagina");
			}
			break;
		default:
			throw new ComandoInvalido("Comando \"" + partes[0] + "\" nao implementado. ");
		}
		
		abaMemoria.atualizar(atual.getTabela(), PaginaMP.class);
		abaDisco.atualizar(atual.getTabela(), PaginaMS.class);
		for(Processo p : kernel.processosModificados()){
			abaMemoria.atualizar(p.getTabela(), PaginaMP.class);
			abaDisco.atualizar(p.getTabela(), PaginaMS.class);
		}
	}

	private void processarInstrucoes() throws ComandoInvalido {

		if(leitor == null) alertar("É necessário um arquivo aberto.", "Erro de uso");
		emExecucao = true;
		
		while (leitor.hasNextLine() && emExecucao) {

			processarInstrucao(instrucao);
			instrucao = leitor.nextLine();
			tfComando.setText(instrucao);

		}

		leitor.close();
		leitor = null;
	}

	private void alertar(String mensagem, String topo) {
		Stage aviso = new Stage();
		aviso.initOwner(fundo);
		aviso.initModality(Modality.APPLICATION_MODAL);

		Label lAviso = new Label(mensagem);

		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.getChildren().add(lAviso);

		Scene scene = new Scene(root, 300, 100);
		aviso.setScene(scene);
		aviso.setTitle(topo);
		aviso.show();
	}
}
