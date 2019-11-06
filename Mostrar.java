package microJEDI;

public class Mostrar
{
	public void clearConsole()
	{
		// ANSI CODE
		// System.out.println("\033[2J\033[;H"); // Works but no
		final String ANSI_CLS = "\u001b[2J"; // Clear screen.
		final String ANSI_HOME = "\u001b[H"; // Cursor.
		System.out.print(ANSI_CLS + ANSI_HOME);
		System.out.flush();
	}

	public void optionsUC()
	{
		System.out.println("-----------------------------------------------------------"
						+ "--------------------");
		System.out.print("Options:"
						+ "[1] Load program from file.\t[6] Show instructions.\n"
						+ "\t[2] Execute program.\t\t[7] Save program in file.\n"
						+ "\t[3] Change position in memory.\t[8] Help.\n"
						+ "\t[4] Change register.\t\t[9] Exit.\n"
						+ "\t[5] Show memory and registers.\t[0] Clear console.\n");
		System.out.println("-----------------------------------------------------------"
						+ "--------------------");
	}

	public void help()
	{
		System.out.println("STA  :  Armazena o conteudo do reg. A na posicao de memoria"
						+ " indicada\n\tpelo conteudo da posicao de memoria seguinte a"
						+ " instrucao (pc+1=a).\n"
						+ "\tOpcode da instrucao: 0 (zero)");

		System.out.println("LDA  :  Carrega para o reg. A o conteudo da posicao de"
						+ " memoria indicada\n\tna posicao de memoria seguinte a"
						+ " instrucao (a=pc+1).\n"
						+ "\tOpcode da instrucao: 1");

		System.out.println("STB  :  Armazena o conteudo do reg. B na posicao de memoria"
						+ " indicada\n\tpelo conteudo da posicao de memoria seguinte a"
						+ " instrucao (pc+1=b).\n\tOpcode da instrucao: 2");

		System.out.println("LDB  :  Carrega para o reg. B o conteudo da posicao de"
						+ " memoria indicada\n\tna posicao de memoria seguinte a"
						+ " instrucao (b=pc+1).\n"
						+ "\tOpcode da instrucao: 3");

		System.out.println("STC  :  Armazena o conteudo do reg. C na posicao de memoria"
						+ " indicada\n\tpelo conteudo da posicao de memoria seguinte"
						+ " a instrucao (c=pc+1).\n"
						+ "\tOpcode da instrucao: 4");

		System.out.println("SUM  :  Sum the content of registers A and B and store"
						+ " the result\n\tin register C.\n"
						+ "\tOpcode of instruction: 5");

		System.out.println("SUB  :  Subtrai do conteudo do registrador A com o valor do"
						+ " reg. B e armazena\n\to resultado no registrador C.\n"
						+ "\tOpcode da instrucao: 6");

		System.out.println("COM  :  Compara o conteudo dos registradores A e B e"
						+ " armazena o resultado\n\tno registrador C. Se A=B entao C=0;"
						+ " se A<B entao C=1; se A>B entao C=2.\n"
						+ "\tOpcode da instrucao: 7");

		System.out.println("JMP  :  Desvia o fluxo de execucao para o endereco contido"
						+ " na posicao de\n\tmemoria seguinte a instrucao. Este um eh"
						+ " salto (desvio) incondicional.\n\tEsta instrucao carrega"
						+ " para o registrador PC o valor da posicao de\n"
						+ "\tmemoria seguinte a instrucao (pc=pc+1).\n"
						+ "\tOpcode da instrucao: 8");

		System.out.println("JPE  :  Desvia o fluxo de execucao para o endereco contido"
						+ " na posicao de\n\tmemoria seguinte a instrucao. Este um eh"
						+ " salto (desvio) condicional.\n\tSo ira ocorrer se"
						+ " A=B (registrador C=0). Deve ser precedida pela\n"
						+ "\tinstrucao COM (pc=pc+1).\n"
						+ "\tOpcode da instrucao: 9");

		System.out.println("JPG  :  Desvia o fluxo de execucao para o endereco contido"
						+ " na posicao de\n\tmemoria seguinte a instrucao. Este um eh"
						+ " salto (desvio) condicional.\n\tSo ira ocorrer se"
						+ " A>B (registrador C=2). Deve ser precedida pela\n"
						+ "\tinstrucao COM (pc=pc+1).\n"
						+ "\tOpcode da instrucao: 10");

		System.out.println("JPL  :  Desvia o fluxo de execucao para o endereco contido"
						+ " na posicao de\n\tmemoria seguinte a instrucao. Este eh um"
						+ " salto (desvio) condicional.\n"
						+ "\tSo ira ocorrer se A<B (registrador C=1)."
						+ " Deve ser precedida pela\n\tinstrucao COM (pc=pc+1).\n"
						+ "\tOpcode da instrucao: 11");

		System.out.println("CONA :  Carrega o registrador A com o valor da posicao"
						+ " seguinte de\n\tmemoria.\n"
						+ "\tOpcode da instrucao: 12");

		System.out.println("CONB :  Carrega o registrador B com o valor da posicao seguinte de\n"
						+ "\tmemoria.\n"
						+ "\tOpcode da instrucao: 13");

		System.out.println("STOP :  Encerra a execucao.\n"
						+ "\tOpcode da instrucao: 14");
	}
}