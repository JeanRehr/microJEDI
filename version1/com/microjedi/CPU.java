package com.microjedi;

/* 
 * The microJEDI architecture is a hypothetical architecture developed for educational
 * purposes. It is based on an Arithmetic Logic Unit (ALU), a Control Unit (CU), an
 * Instruction Decoder Unit (IDU), 3 registers associated with the ALU (registers A, B,
 * and C) and an instruction register (PC).
 * Project almost the way the paper describes.
 * Maybe not the best OOP way to write it.
 */

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class CPU {
	// UnsignedByte is an int that serves as a wrapper to allow only 0 to 255 values.
	UnsignedByte a = new UnsignedByte(0);
	UnsignedByte b = new UnsignedByte(0);
	UnsignedByte c = new UnsignedByte(0);
	UnsignedByte pc = new UnsignedByte(0);

	int[] mem = new int[64];

	public boolean isAMutable = true;
	public boolean isBMutable = true;
	public boolean isStop = false;

	/* getters */
	public int getA() {
		return this.a.getValue();
	}

	public int getB() {
		return this.b.getValue();
	}

	public int getC() {
		return this.c.getValue();
	}

	public int getPC() {
		return this.pc.getValue();
	}

	/* Return value of mem at index. If index is oob in this.mem then return this.pc */
	public int getEleAtOrPC(int index) {
		if (index >= 0 && index < this.mem.length)
			return this.mem[index];

		return this.pc.getValue();
	}

	/* setters */
	public void setA(int newA) {
		if (isAMutable) {
			this.a.setValue(newA);
		}
	}

	public void setB(int newB) {
		if (isBMutable) {
			this.b.setValue(newB);
		}
	}

	public void setC(int newC) {
		this.c.setValue(newC);
	}

	public void setPC(int newPC) {
		this.pc.setValue(newPC);
	}

	public void setMemAtIndex(int newMem, int index) {
		if (index >= 0 && index < this.mem.length) {
			this.mem[index] = newMem;
		}
	}

	/* Formatting functions */
	public String parseIntToHex1F(int printValue) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%01X", printValue));
		return sb.toString();
	}

	public int parseHexToInt(String hex) {
		int value;
		return value = Integer.parseInt(hex, 16);
	}

	/* Test to see if hex values from user are able to be translated to an int */
	public boolean isHexParseable(String hex) {
		try {
			Integer.parseInt(hex, 16);
			return true;
		} catch (final NumberFormatException e) {
			System.out.println("Not a Hexadecimal number.");
			return false;
		}
	}

	public void cpu() { /* function called at main */
		ALU alu = new ALU();
		IDU idu = new IDU();
		Scanner scanner = new Scanner(System.in);
		ConsoleHandler consoleHandler = new ConsoleHandler(scanner);
		int userOpt = 0;
		while (userOpt != 9) {
			consoleHandler.options();

			System.out.print("Option> ");

			userOpt = consoleHandler.getUserOption(0, 9);
			consoleHandler.getNextLine(); // Consuming \n

			switch (userOpt) {
			case 0: /* Clear console */
				consoleHandler.clearConsole();
				break;
			case 1: /* Load program into memory */
				loadProgram(mem, consoleHandler);
				System.out.println("\nCurrent state of memory and registers:");
				displayState(a, b, c, pc, mem);
				break;
			case 2: /* Execute program */
				executeProgram(a, b, c, pc, mem, alu, idu, consoleHandler);
				break;
			case 3: /* Change value in memory */
				changeValueMem(mem, consoleHandler);
				break;
			case 4: /* Change value in register */
				changeValueReg(mem, consoleHandler);
				break;
			case 5: /* Show memory and registers */
				displayState(a, b, c, pc, mem);
				break;
			case 6: /* Show instructions */
				showInstructions(mem, consoleHandler);
				break;
			case 7: /* Save program in file */
				saveProgram(mem, consoleHandler);
				break;
			case 8: /* Help */
				showHelp(consoleHandler);
				break;
			case 9: /* Exit */
				break;
			}
		}
		scanner.close();
	}

	/* Option 1 */
	public void loadProgram(int[] mem, ConsoleHandler consoleHandler) {
		System.out.print("Program name? ");
		String name = consoleHandler.getNextLine();
		List<Integer> integers = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\s+");
                for (String token : tokens) {
                    try {
                        // Try to parse each token as an integer
                        integers.add(Integer.parseInt(token));
                    } catch (NumberFormatException e) {
                        // Ignore tokens that cannot be parsed as an integer
                    }
                }
			}

			if (integers.isEmpty()) {
				System.out.println("File is empty.");
				return;
			}

			int biggerSize = 0;
			if (integers.size() > mem.length) {
				biggerSize = integers.size();
			} else {
				biggerSize = mem.length;
			}
			for (int i = 0; i < biggerSize - 1; i++) {
				setMemAtIndex(integers.get(i), i);
			}

		} catch (FileNotFoundException fnfe) {
			System.out.println("File not found.");
		} catch (IOException ioe) {
			System.out.println("Couldn't read file.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Option 2 */
	public void executeProgram(
		UnsignedByte a,
		UnsignedByte b,
		UnsignedByte c,
		UnsignedByte pc,
		int[] mem,
		ALU alu,
		IDU idu,
		ConsoleHandler consoleHandler
	) {
		/* Resetting flags */
		isStop = false;
		isAMutable = true;
		isBMutable = true;

		if (getPC() != 0) {
			System.out.printf(
				" >>> Warning! PC different from 0.\n" +
				" >>> Press Enter or 0 to execute from memory address 0.\n" +
				" >>> Press any other key to execute from current " +
				"address pointed to by PC [%X].\nOption> ", getPC()
			);
			String resetPC = consoleHandler.getNextLine().trim();
        	if (resetPC.isEmpty() || resetPC.charAt(0) == '0') {
            	setPC(0);
        	}
		}

		System.out.println("***** Initianting Execution *****");
		while (!isStop) {
			/* These assignments are needed due to java's pass by value only nature */
			a.setValue(getA());
			b.setValue(getB());
			c.setValue(getC());
			pc.setValue(getPC());
			for (int i = 0; i < mem.length; i++) {
				mem[i] = getEleAtOrPC(i);
			}

			displayState(a, b, c, pc, mem);

			if (getPC() < 0 || getEleAtOrPC(getPC()) > 14) {
				System.out.printf(
					"\n >>> Instruction at PC[%X] is not valid, instruction <%X> " +
					"does not exist.\n >>> Terminating execution.\n" +
					" >>> Press any key to continue", getPC(), getEleAtOrPC(getPC())
				);

				consoleHandler.getNextLine();
				return;
			}

			System.out.print("\nPress Enter or any Key to continue, a to (A)bort.\n> ");

			String cont = consoleHandler.getNextLine().trim();

			if (cont.isEmpty() || Character.toLowerCase(cont.charAt(0)) != 'a') {
				controlUnit(a, b, c, pc, mem, alu, idu, consoleHandler);
			} else {
				break;
			}			
		}
	}

	/* Option 3 */
	public void changeValueMem(int[] mem, ConsoleHandler consoleHandler) {
		int value;
		int pos;
		String userInput;

		System.out.printf("Address (0 to %X)> ", (mem.length - 1));
		while (!isHexParseable(userInput = consoleHandler.getNextLine())) {
			System.out.print("Invalid address.\nAddress> ");
		}
		pos = parseHexToInt(userInput);

		if (pos > mem.length - 1) {
			System.out.println("Invalid address");
			return;
		}

		System.out.print("Value (0 to FF)> ");
		while (!isHexParseable(userInput = consoleHandler.getNextLine())) {
			System.out.print("Invalid value .\nValue> ");
		}
		value = parseHexToInt(userInput);
		if (value >= 0 && value <= 255 && pos >= 0 && pos < mem.length) {
			setMemAtIndex(value, pos);
		} else {
			System.out.println("Invalid value or address.");
		}
	}

	/* Option 4 */
	public void changeValueReg(int[] mem, ConsoleHandler consoleHandler) {
		System.out.println("Press 0 to zero all registers.");
		System.out.print("Register (A, B, C, PC)> ");
		char reg = consoleHandler.getNextLine().charAt(0);

		switch (reg) {
		case '0':
			setA(0);
			setB(0);
			setC(0);
			setPC(0);
			return;
		}

		System.out.print("Value (0 to FF)> ");

		String userInput;
		if (!isHexParseable(userInput = consoleHandler.getNextLine())) {
			System.out.print("Invalid value.");
			return;
		}

		int value = parseHexToInt(userInput);

		switch (reg) {
		case 'a':
		case 'A':
			if (value >= 0 && value <= 255)
				setA(value);
			else
				System.out.println("Invalid value.");
			break;
		case 'b':
		case 'B':
			if (value >= 0 && value <= 255)
				setB(value);
			else
				System.out.println("Invalid value.");
			break;
		case 'c':
		case 'C':
			if (value >= 0 && value <= 255)
				setC(value);
			else
				System.out.println("Invalid value.");
			break;
		case 'p':
		case 'P':
			if (value >= 0 && value <= 255)
				setPC(value);
			else
				System.out.println("Invalid value.");
			break;
		default:
			System.out.println("Register does not exit or invalid value.");
		}
	}

	/* Option 5 */
	public void displayState(UnsignedByte a, UnsignedByte b, UnsignedByte c, UnsignedByte pc, int[] mem) {
		System.out.printf(
            "Registers:\tA:%X\tB:%X\tC:%X\t[PC:%X]\n", getA(), getB(), getC(), getPC()
        );

        System.out.println("Memory: ----------------------------------------------------------------+");
        System.out.println("\t00\t01\t02\t03\t04\t05\t06\t07\t|");
        System.out.println("\t\t\t\t\t\t\t\t\t|");

		int count = 0;
		for (int i = 0; i < mem.length; i++) { /* Memory values */
			if (i == 0 || i % 8 == 0) { /* Right numbers */
				System.out.printf("%02X:", i);
			}

			if (getPC() == i) {
				System.out.printf("\t<%X>", getEleAtOrPC(getPC()));
			} else {
				System.out.printf("\t%X", getEleAtOrPC(i));
			}
			count++;

			if (count % 8 == 0) {
				System.out.println("\t|");
			}
		}

		if (count % 8 != 0) {
			System.out.println(
				"\n-----------------------------------" +
				"-------------------------------------+"
			);
		} else {
			System.out.println(
				"-----------------------------------" +
				"-------------------------------------+"
			);
		}
		System.out.print("Next instruction: " + getInstruction(getPC(), mem));
	}

	/* Option 6 */
	public void showInstructions(int[] mem, ConsoleHandler consoleHandler) {
		System.out.print("Show up to which address? (0 to show all addresses)> ");

		String userInput;
		while (!isHexParseable(userInput = consoleHandler.getNextLine())) {
			System.out.printf("Invalid address. Address from 0 to %X", (mem.length - 1));
			return;
		}

		int addr = parseHexToInt(userInput);

		if (addr == 0 || addr >= mem.length - 1) {
			addr = mem.length - 1;
		}

		System.out.println("Address:\tInstruction:\tAddress:\tInstruction:");

		int i;
		for (i = 0; i <= addr; i++) {
			if (i % 2 == 0 || i == 0) {
				System.out.print(getInstruction(i, mem) + "\t\t");
			} else {
				System.out.print(getInstruction(i, mem) + "\n");
			}
		}
	}

	/* Option 7 */
	public void saveProgram(int[] mem, ConsoleHandler consoleHandler) {
		System.out.print("File name? ");
		String name = consoleHandler.getNextLine();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(name))) {
			for (int i = 0; i < mem.length; i++) {
				writer.write(mem[i] + " ");
			}
		} catch (IOException ioe) {
			System.out.println("Could not save memory.");
		}
	}

	/* Option 8 */
	public void showHelp(ConsoleHandler consoleHandler) {
		consoleHandler.clearConsole();
		consoleHandler.help1();
		System.out.print("Press any Key to continue.");
		consoleHandler.getNextLine();
		consoleHandler.help2();
	}

	/* Abstracting getting only one instruction at addr */
	public String getInstruction(int addr, int[] mem) {
		if (addr > mem.length - 1 || addr < 0) {
			return String.format("[%X]:\t\tInvalid", addr);
		}

		String instru = null;
		int instructionCode = getEleAtOrPC(addr);
		switch (instructionCode) {
		case 0:
			instru = "STA";
			break;
		case 1:
			instru = "LDA";
			break;
		case 2:
			instru = "STB";
			break;
		case 3:
			instru = "LDB";
			break;
		case 4:
			instru = "STC";
			break;
		case 5:
			instru = "SUM";
			break;
		case 6:
			instru = "SUB";
			break;
		case 7:
			instru = "COM";
			break;
		case 8:
			instru = "JMP";
			break;
		case 9:
			instru = "JPE";
			break;
		case 10:
			instru = "JPG";
			break;
		case 11:
			instru = "JPL";
			break;
		case 12:
			instru = "CONA";
			break;
		case 13:
			instru = "CONB";
			break;
		case 14:
			instru = "STOP";
			break;
		default:
			instru = "Invalid";
			break;
		}

		if (instru == null) {
			return String.format("[%X]:\t\tInvalid", addr);
		} else if (getEleAtOrPC(addr) == 5 || getEleAtOrPC(addr) == 6 || getEleAtOrPC(addr) == 7 || getEleAtOrPC(addr) == 14) {/* addr that don't use param */
			return String.format("[%X]:\t\t%s", addr, instru);
		} else if (addr == mem.length - 1) {
			return String.format("[%X]:\t\t%s Invalid", addr, instru);
		} else {
			return String.format("[%X]:\t\t%s %X", addr, instru, getEleAtOrPC(addr + 1));
		}
	}

	public void controlUnit(
		UnsignedByte a,
		UnsignedByte b,
		UnsignedByte c,
		UnsignedByte pc,
		int[] mem,
		ALU alu,
		IDU idu,
		ConsoleHandler consoleHandler
	) {
		int instructionCode = getEleAtOrPC(getPC());
		switch (instructionCode) {
		case 0:
			idu.sta(getA(), getEleAtOrPC((getPC() + 1)));
			setPC(getPC() + 2);
			break;
		case 1:
			idu.lda(getPC());
			setPC(getPC() + 2);
			break;
		case 2:
			idu.stb(getB(), getEleAtOrPC(getPC() + 1));
			setPC(getPC() + 2);
			break;
		case 3:
			idu.ldb(getPC());
			setPC(getPC() + 2);
			break;
		case 4:
			idu.stc(getC(), getEleAtOrPC(getPC() + 1));
			setPC(getPC() + 2);
			break;
		case 5:
			setC(alu.sum(getA(), getB()));
			setPC((getPC() + 1));
			break;
		case 6:
			setC(alu.sub(getA(), getB()));
			setPC(getPC() + 1);
			break;
		case 7:
			setC(alu.com(getA(), getB()));
			setPC(getPC() + 1);
			break;
		case 8:
			idu.jmp(getPC());
			break;
		case 9:
			idu.jpe(getC(), getPC());
			break;
		case 10:
			idu.jpg(getC(), getPC());
			break;
		case 11:
			idu.jpl(getC(), getPC());
			break;
		case 12:
			idu.cona(getPC());
			setPC(getPC() + 2);
			break;
		case 13:
			idu.conb(getPC());
			setPC(getPC() + 2);
			break;
		case 14:
			System.out.print("STOP: Terminating execution.\n>>> Press any key to continue.");
			consoleHandler.getNextLine();
			idu.stop();
			break;
		}
	}
	
private class ALU {
	public int sum(int a, int b) {
		UnsignedByte sumValue = new UnsignedByte(a);
		sumValue.add(b);
		return sumValue.getValue();
	}

	public int sub(int a, int b) {
		UnsignedByte subValue = new UnsignedByte(a);
		subValue.subtract(b);
		return subValue.getValue();
	}

	public int com(int a, int b) {
		if (a < b) {
			System.out.println("A < B: " + a + " "+ b);
			return 1;
		} else if (a > b) {
			System.out.println("A > B: " + a + " "+ b);
			return 2;
		} else {
			System.out.println("A = B: " + a + " "+ b);
			return 0;
		}
	}
}
/*
private class ALU {
	public void sum(byte a, byte b) {
		setC((byte) (a + b));
	}

	public void sub(byte a, byte b) {
		setC((byte) (a - b));
	}

	public void com(byte a, byte b) {
		if (a < b) {
			System.out.println("A < B: " + a + " "+ b);
			setC((byte) 1);
		} else if (a > b) {
			System.out.println("A > B: " + a + " "+ b);
			setC((byte) 2);
		} else {
			System.out.println("A = B: " + a + " "+ b);
			setC((byte) 0);
		}
	}
}*/

private class IDU { /* instruction decoder unit */
	public void sta(int a, int index){
		setMemAtIndex(a, index);
	}

	public void lda(int pc) {
		setA(getEleAtOrPC(pc + 1));
	}

	public void stb(int b, int index) {
		setMemAtIndex(b, index);
	}

	public void ldb(int pc) {
		setB(getEleAtOrPC(pc + 1));
	}

	public void stc(int c, int index) {
		setMemAtIndex(c, index);
	}

	public void jmp(int pc) {
		setPC(getEleAtOrPC(pc + 1));
	}

	/*
	 * maybe needs to have COM at mem[pc-1] for cond jumps to work? not tested at original software
	 * idea:
	 * if (mem[pc-1] == 7 && c == 0) then jump
	 * else pc+2;
	 */
	public void jpe(int c, int pc) {
		if (getC() == 0) {
			setPC(getEleAtOrPC(pc + 1));
		} else {
			setPC(pc + 2);
		}
	}

	public void jpg(int c, int pc) {
		if (getC() == 2) {
			setPC(getEleAtOrPC(pc + 1));
		} else {
			setPC(pc + 2);
		}
	}

	public void jpl(int c, int pc) {
		if (getC() == 1) {
			setPC(getEleAtOrPC(pc + 1));
		} else {
			setPC(pc + 2);
		}
	}

	public void cona(int pc) {
		setA(getEleAtOrPC(pc + 1));
		isAMutable = false;
	}

	public void conb(int pc) {
		setB(getEleAtOrPC(pc + 1));
		isBMutable = false;
	}

	public void stop() {
		isStop = true;
	}
}
}
