#Requerimentos do trabalho de Sistemas Operacionais

## Implementar um gerenciador de memória virtual

Requerimentos do programa principal:
	- Ler em arquivos as informações sobre a "vida" do processo
		- cada processo contém o tamanho de sua imagem e uma sequência de operações de acesso à memória principal que ele realiza
	- Permitir configuração do gerenciamento pelo ajuste de:
		- Tamanho das páginas e quadros de página
		- Tamanho em bits do endereço lógico
		- Tamanho da Memória física que deve ser múltiplo de tamanho do quadro
		- Tamanho máximo da memória secundária
>confl> - Tamanho da imagem de cada processo a ser executado
	-  Utilizar um algoritmo de substituição de páginas, para alocar um quadro a uma página não residente em memória em caso da MP estar toda alocada:
		- Algoritmo de substituição de páginas LRU 
		- relógio com somente o bit de utilização u
	    - Escopo de substituição global

Requisitos para a interface:
	 Especificar as estruturas de dados necessárias para implementar cada política de substituição e escopo. 
	 Uma saída (interface amigável) deve mostrar:

	 - o que está acontecendo tanto na memória principal, quanto na secundária;
	 - a tabela de páginas associada a cada processo em execução;
	 - as ocorrências de falta de página;
	 - estado dos processos


Os tipos de acesso são: leitura (R); e gravação (W)

<número-do-processo> <tipo-de-acesso (R ou W)> <endereço-lógico>

Ainda, para cada processo novo, uma linha com o tag C (criação) representa que o processo deve ser criado considerando o tamanho especificado nesta linha. A unidade de armazenamento deve ser especificada pelo projetista.

<número-do-processo>  C  <tamanho-em-alguma-unidade>

Indicando instrução a ser executada pela CPU
<número-do-processo>  P  <operando>

Indicando instrução de I/O
<número-do-processo>  I  <dispositivo>


Exemplo de um arquivo de entrada:

##### início do arquivo ######
P1 C 500
P1 R (0)2
P1 R (1024)2
P1 P  (1)2
P1 R (2)2
P1 P (2)2
P1 W  (1024)2
P7 C 1000
P7 R (4095)2
P7 R  (800)2
P7 I  (2)2
P7 R (801)2
P7 W  (4096)2
P1 R (3)2
P1 R  (4)2
P1 W (1025)2
P1 W  (1026)2
……etc
##### final do arquivo ######

