package microJEDI;

public class Show
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
						+ "\t[3] Change value in memory.\t[8] Help.\n"
						+ "\t[4] Change register.\t\t[9] Exit.\n"
						+ "\t[5] Show memory and registers.\t[0] Clear console.\n");
		System.out.println("-----------------------------------------------------------"
						+ "--------------------");
	}

	public void help()
	{
		System.out.println("STA  :  Stores content of register A to the memory position"
						+ " followed by the next\n\tmemory position instruction"
						+ " (a=pc+1).\n\tOpcode instruction: 0");

		System.out.println("LDA  :  Load to register A the content of memory position"
						+ " indicated in memory\n\tposition followed this instruction"
						+ " (pc+1=a).\n\tOpcode instruction: 1");

		System.out.println("STB  :  Stores content of register B to the memory position"
						+ " followed by the next\n\tmemory position instruction"
						+ "(b=pc+1).\n\tOpcode instruction: 2");

		System.out.println("LDB  :  Load to register B the content of memory position"
						+ " indicated in memory\n\tposition followed this instruction"
						+ " (pc+1=b).\n\tOpcode instruction: 3");

		System.out.println("STC  :  Stores content of register C to the memory position"
						+ " following by the next\n\tmemory position instruction"
						+ " (pc+1=c).\n\tOpcode instruction: 4");

		System.out.println("SUM  :  Sum content of registers A and B and store the"
						+ " result in register C.\n\tOpcode instruction: 5");

		System.out.println("SUB  :  Subtract content of register A with the value of"
						+ " register B and store the\n\tresult in register C.\n"
						+ "\tOpcode instruction: 6");

		System.out.println("COM  :  Compares content of registers A and B, store the"
						+ " result in register C.\n\tIf A=B then C=0; if A<B then C=1;"
						+ " if A>B then C=2.\n\tOpcode instruction: 7");

		System.out.println("JMP  :  Diverts the execution flow to the address contained"
						+ " at the position\n\tfollowing this memory instruction. This"
						+ " is an unconditional jump.\n\tThis instruction load to"
						+ " register PC the value of the memory position\n\tfollowing"
						+ " this instruction (pc=pc+1).\n\tOpcode instruction: 8");

		System.out.println("JPE  :  Diverts the execution flow to the address contained"
						+ " at the position\n\tfollowing this memory instruction. This"
						+ " is a conditional jump.\n\tWill only occur if A=B"
						+ " (register C=0). Must be preceded by COM instruction\n"
						+ "\t(pc=pc+1).\n\tOpcode instruction: 9");

		System.out.println("JPG  :  Diverts the execution flow to the address contained"
						+ " at the position\n\tfollowing this memory instruction. This"
						+ " is a conditional jump.\n\tWill only occur if A>B"
						+ " (register C=2). Must be preceded by COM instruction\n"
						+ "\t(pc=pc+1).\n\tOpcode instruction: 10");

		System.out.println("JPL  :  Diverts the execution flow to the address contained"
						+ " at the position\n\tfollowing this memory instruction. This"
						+ " is a conditional jump.\n\tWill only occur if A<B"
						+ " (register C=1). Must be preceded by COM instruction\n"
						+ "\t(pc=pc+1).\n\tOpcode instruction: 11");

		System.out.println("CONA :  Load the register A with the value following this "
						+ " next memory position.\n\tOpcode instruction: 12");

		System.out.println("CONB :  Load the register B with the value following this "
						+ " next memory position.\n\tOpcode instruction: 13");

		System.out.println("STOP :  Terminate execution.\n\tOpcode instruction: 14");
	}
}
