package com.microjedi;

import java.util.Scanner;

public class ConsoleHandler {
    private final Scanner scanner;

    public ConsoleHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void clearConsole() {
		/* ANSI CODE */
		// System.out.println("\033[2J\033[;H"); /* Works but not always */
		final String ANSI_CLS = "\u001b[2J"; /* Clear screen. */
		final String ANSI_HOME = "\u001b[H"; /* Cursor to the top right. */
		System.out.print(ANSI_CLS + ANSI_HOME);
		System.out.flush();
	}

	public void options() { 
		System.out.print(
						"\n-----------------------------------------------------------" +
						"--------------------\n" +
						"Options:" +
						"[1] Load program from file.\t[6] Show instructions.\n" +
						"\t[2] Execute program.\t\t[7] Save program in file.\n" +
						"\t[3] Change value in memory.\t[8] Help.\n" +
						"\t[4] Change register.\t\t[9] Exit.\n" +
						"\t[5] Show memory and registers.\t[0] Clear console.\n" +
						"-----------------------------------------------------------" +
						"--------------------\n");
	}

	    
	public String getNextLine() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.getMessage());
            return "";
        }
    }

	public int getInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid number.\n> ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public int getUserOption(int low, int high) {
        int userOpt = getInt();
        while (userOpt > high || userOpt < low) {
            System.out.print("Invalid option.\n> ");
            userOpt = getInt();
        }
        return userOpt;
    }

	public void help1()	{
		System.out.print(
						"After instruction execution, PC will be incremented by 1 or 2;\n" +
						"If no parameter is required (SUM, SUB, COM) it will be 1, otherwise 2.\n" +

						"STA  :  Stores the contents of register A in the memory location indicated by\n\t" +
						"the contents of the position following this instruction (mem[pc+1]=a)." +
						"\n\tOpcode instruction: 0\n" +

						"LDA  :  Loads into register A the contents of the position whose address is\n\t" +
						"in the memory location following this instruction (a=mem[pc+1])." +
						"\n\tOpcode instruction: 1\n" +

						"STB  :  Stores the contents of register B in the memory location indicated by\n\t" +
						"the contents of the position following this instruction (mem[pc+1]=b)" +
						"\n\tOpcode instruction: 2\n" +

						"LDB  :  Loads into register B the contents of the position whose address is\n\t" +
						"in the memory location following this instruction (b=mem[pc+1])." +
						"\n\tOpcode instruction: 3\n" +

						"STC  :  Stores the contents of register C in the memory location indicated by\n\t" +
						"the contents of the position following this instruction (mem[pc+1]=c)." +
						"\n\tOpcode instruction: 4\n" +

						"SUM  :  Adds the contents of registers A and B and stores the result in register C." +
						"\n\tOpcode instruction: 5\n" +

						"SUB  :  Subtracts the content of register A with the value of register B and\n\t" + 
						"stores the result in register C.\n\tOpcode instruction: 6\n" +

						"COM  :  Compares the values of registers A and B, store the result in register C.\n\t" +
						"If A=B, C=0; if A<B, C=1; if A>B, C=2.\n\tOpcode instruction: 7\n" +

						"JMP  :  Diverts the execution flow to the address contained at the position following\n\t" +
						"this memory instruction. This is an unconditional jump. This instruction loads to\n\t" +
						"register PC the value of the memory position following this instruction (pc=mem[pc+1])." + 
						"\n\tOpcode instruction: 8\n"
						);
	}

	public void help2() {
		System.out.print(
						"JPE  :  Diverts the execution flow to the address contained at the position following\n\t" +
						"this memory instruction. This is a conditional jump. Will only occur if A=B (register C=0).\n\t" +
						"Must be preceded by COM instruction (pc=mem[pc+1]).\n\tOpcode instruction: 9\n" +

						"JPG  :  Diverts the execution flow to the address contained at the position following\n\t" +
						"this memory instruction. This is a conditional jump. Will only occur if A>B (register C=2).\n\t" +
						"Must be preceded by COM instruction (pc=mem[pc+1]).\n\tOpcode instruction: A\n" +

						"JPL  :  Diverts the execution flow to the address contained at the position following\n\t" +
						"this memory instruction. This is a conditional jump. Will only occur if A<B (register C=1).\n\t" +
						"Must be preceded by COM instruction (pc=mem[pc+1]).\n\tOpcode instruction: B\n" +

						"CONA :  Loads a constant value into register A, which is the content of the memory\n\t" +
						"position following the instruction (mem[pc+1]=a (a turns to const))." +
						"\n\tOpcode instruction: C\n" +

						"CONB :  Loads a constant value into register B, which is the content of the memory\n\t" +
						"location following the instruction (mem[pc+1]=b (b turns to const))." +
						"\n\tOpcode instruction: D\n" +

						"STOP :  Terminate execution.\n\tOpcode instruction: E\n"
						);
	}
}
