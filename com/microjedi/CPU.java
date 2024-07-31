package com.microjedi;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileOutputStream;


public class CPU {
	private byte a, b, c;
	private byte pc;
	private byte[] mem = new byte[64];

	public boolean isAMutable = true;
	public boolean isBMutable = true;
	public boolean isStop = false;

	/* getters */
	public byte getA() {
		return this.a;
	}

	public byte getB() {
		return this.b;
	}

	public byte getC() {
		return this.c;
	}

	public byte getPC() {
		return this.pc;
	}

	/* 
	 * Will return the value of mem at index. If index is
	 * out of bounds of this.mem return this.pc
	 */
	public byte getEleAtOrPC(short index) {
		if (index >= 0 && index < this.mem.length)
			return this.mem[index];

		return this.pc;
	}

	/* setters */
	public void setA(byte newA) {
		if (isAMutable) {
			this.a = newA;
		}
	}

	public void setB(byte newB) {
		if (isBMutable) {
			this.b = newB;
		}
	}

	public void setC(byte newC) {
		this.c = newC;
	}

	public void setPC(byte newPC) {
		this.pc = newPC;
	}

	public void setMemAtIndex(byte newMem, short index) {
		if (index >= 0 && index < this.mem.length) {
			this.mem[index] = newMem;
		}
	}

	/* Formatting functions */
	public short toUnsignedByte(byte printValue) {
		return (short) (printValue & 0xFF);
	}

	/* 0 in front when printValue less than 10 */
	public String byteToHex2F(byte printValue) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02X", printValue));
		return sb.toString();
	}

	public String byteToHex1F(byte printValue) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%01X", printValue));
		return sb.toString();
	}

	public short parseHexToShort(String hex) {
		short value;
		return value = (short) Integer.parseInt(hex, 16);
	}

	public byte parseHexToByte(String hex) {
		byte value;
		return value = (byte) Integer.parseInt(hex, 16);
	}

	/* Test to see if entered hex values from user are able to be translated to an int */
	public boolean isHexParseable(String hex) {
		try {
			Integer.parseInt(hex, 16);
			return true;
		} catch (final NumberFormatException e) {
			System.out.println("Not a Hexadecimal number");
			return false;
		}
	}

	public void cpu() { /* function called at main */
		ALU alu = new ALU();
		IDU idu = new IDU();
		Text text = new Text();
		Scanner scanner = new Scanner(System.in);

		short userOpt = 10;

		while (userOpt != 9) {
			text.options();

			System.out.print("Option> ");

			while (!scanner.hasNextShort()) {
				System.out.println("Invalid option.");
				scanner.next();
				System.out.print("Option> ");
			}
			userOpt = scanner.nextShort();
			scanner.nextLine(); // Consuming \n

			switch (userOpt) {
			case 0: /* Clear console */
				text.clearConsole();
				break;
			case 1: /* Load program into memory */
				loadProgram(mem, scanner);
				System.out.println("\nCurrent state of memory and registers:");
				showMemReg(a, b, c, pc, mem);
				break;
			case 2: /* Execute program */
				executeProgram(a, b, c, pc, mem, alu, idu, scanner);
				break;
			case 3: /* Change value in memory */
				changeValueMem(mem, scanner);
				break;
			case 4: /* Change register */
				changeValueReg(mem, scanner);
				break;
			case 5: /* Show memory and registers */
				showMemReg(a, b, c, pc, mem);
				break;
			case 6: /* Show instructions */
				showInstructions(mem, scanner);
				break;
			case 7: /* Save program in file */
				saveProgram(mem, scanner);
				break;
			case 8: /* Help */
				showHelp(text, scanner);
				break;
			case 9: /* Exit */
				break;
			}
		}
	}

	/* Option 1 */
	public void loadProgram(byte[] mem, Scanner scanner) {
		System.out.print("The loaded file must be in bytes.\nProgram name? ");
		String name = scanner.nextLine();
		try {
			byte[] memUser = Files.readAllBytes(Paths.get(name));

			for (short i = 0; i < mem.length; i++) {
				setMemAtIndex(memUser[i], i);
			}
		} catch (Exception e) {
			System.out.println("File not found.");
		}
	}

	/* Option 2 */
	public void executeProgram(byte a, byte b, byte c, byte pc, byte[] mem, ALU alu, IDU idu, Scanner scanner) {
		String cont;
		byte pc2; 

		/* Resetting flags */
		isStop = false;
		isAMutable = true;
		isBMutable = true;

		if (pc != 0) {
			System.out.print(" >>> Warning! PC different from 0.\n" +
							" >>> Press 0 to execute from memory address 0\n" +
							" >>> Press any other number to execute from current" +
							" address pointed to by PC [" +
							byteToHex1F((byte) toUnsignedByte(pc)) + "]." +
							"\nOption> ");

			while (!scanner.hasNextShort()) {
				scanner.next();
				System.out.print("Invalid Option.\nOption> ");
			}

			int codPC;
			if ((codPC = scanner.nextShort()) == 0)
				setPC((byte) 0);

			scanner.nextLine(); // Consuming \n
		}

		System.out.println("***** Initianting Execution *****");
		while (!isStop) {
			/* The following assignments are needed because of java's pass by value only nature */
			a = getA();
			b = getB();
			c = getC();
			pc = getPC();
			for (short i = 0; i < mem.length; i++) {
				mem[i] = getEleAtOrPC(i);
			}

			showMemReg(a, b, c, pc, mem);

			if (pc < 0 || toUnsignedByte(getEleAtOrPC(pc)) > 14) {
				System.out.print("\n >>> Instruction at PC[" +
								byteToHex2F((byte) toUnsignedByte(pc)) +
								"] is not valid, instruction <" +
								byteToHex2F((byte) getEleAtOrPC(pc)) + 
								"> does not exist.\n >>> Terminating execution.\n" +
								" >>> Press Enter to continue");

				cont = scanner.nextLine();
				if (cont.equals("\n"))
					break;
				else
					break;
			}

			System.out.print("\nPress Enter to continue, a to (A)bort.\n" +
							"> ");
			cont = scanner.nextLine();
			if (cont.equalsIgnoreCase("a"))
				break;

			controlUnit(a, b, c, pc, mem, alu, idu, scanner);
		}
	}

	/* Option 3 */
	public void changeValueMem(byte[] mem, Scanner scanner) {
		short value;
		short pos;
		String userInput;

		System.out.print("Address (0 to " + byteToHex1F((byte) (mem.length - 1)) + ")> ");
		while (!isHexParseable(userInput = scanner.nextLine())) {
			System.out.print("Invalid address.\nAddress> ");
		}
		pos = parseHexToShort(userInput);

		System.out.print("Value (0 to FF)> ");
		while (!isHexParseable(userInput = scanner.nextLine())) {
			System.out.print("Invalid value .\nValue> ");
		}
		value = parseHexToShort(userInput);

		System.out.println("test: Value to be assigned: "+ value +"\nPosition: " + pos);

		if (value >= 0 && value <= 255 && pos >= 0 && pos < mem.length) {
			setMemAtIndex((byte) toUnsignedByte((byte) value), pos);
		} else {
			System.out.println("Invalid value or address.");
		}
	}

	/* Option 4 */
	public void changeValueReg(byte[] mem, Scanner scanner) {
		char reg;
		short value;
		String userInput;

		System.out.print("Register (A, B, C, PC)> ");
		reg = scanner.next().charAt(0);
		scanner.nextLine(); /* Consuming \n */

		System.out.print("Value (0 to FF)> ");

		while (!isHexParseable(userInput = scanner.nextLine())) {
			System.out.print("Invalid value .\nValue> ");
		}

		value = parseHexToShort(userInput);

		System.out.println("test: Value to be assigned: "+ value);
		switch (reg) {
		case 'a':
		case 'A':
			if (value >= 0 && value <= 255)
				setA((byte) toUnsignedByte((byte) value));
			else
				System.out.println("Invalid value.");
			break;
		case 'b':
		case 'B':
			if (value >= 0 && value <= 255)
				setB((byte) toUnsignedByte((byte) value));
			else
				System.out.println("Invalid value.");
			break;
		case 'c':
		case 'C':
			if (value >= 0 && value <= 255)
				setC((byte) toUnsignedByte((byte) value));
			else
				System.out.println("Invalid value.");
			break;
		case 'p':
		case 'P':
			if (value >= 0 && value <= 255)
				setPC((byte) toUnsignedByte((byte) value));
			else
				System.out.println("Invalid value.");
			break;
		default:
			System.out.println("Register does not exit or invalid value.");
		}
	}

	/* Option 5 */
	public void showMemReg(byte a, byte b, byte c, byte pc, byte[] mem) {
		short count = 0;
		System.out.print("Registers:\tA:" + byteToHex1F((byte) toUnsignedByte(a)) +
						"\tB:" + byteToHex1F((byte) toUnsignedByte(b)) +
						"\tC:" + byteToHex1F((byte) toUnsignedByte(c)) +
						"\t[PC:" + byteToHex1F((byte) toUnsignedByte(pc)) + "]\n");

		System.out.println("Memory: ------------------------------------------------" +
						"----------------+");
		for (short i = 0; i <= 7; i++) { /* Top numbers */
			System.out.print("\t0" + i);
			if (i == 7)
				System.out.print("\t|");
		}
		System.out.println("\n\t\t\t\t\t\t\t\t\t|");

		for (short i = 0; i < mem.length; i++) { /* Show memory values byte array */
			if (i == 0 || i % 8 == 0) { /* Right numbers */
				System.out.print(byteToHex2F((byte) i) + ":");
			}

			if (pc == i)
				System.out.print("\t" + "<" + byteToHex1F((byte) toUnsignedByte(getEleAtOrPC(pc))) + ">");
			else if (pc < 0 || pc >= mem.length)
				System.out.print("\t" + byteToHex1F((byte) toUnsignedByte(getEleAtOrPC(i))));
			else
				System.out.print("\t" + byteToHex1F((byte) toUnsignedByte(getEleAtOrPC(i))));

			count++;

			if (count % 8 == 0) {
				System.out.println("\t|");
			}
		}

		if (count % 8 != 0) {
			System.out.println("\n---------------------------------------------------" +
							"---------------------+");
		} else {
			System.out.println("-----------------------------------------------------" +
							"-------------------+");
		}

		if (pc < mem.length && pc >= 0) {
			System.out.print("Next instruction: " + getInstruction(pc, mem));
		} else {
			System.out.print("Next instruction: [" + byteToHex1F((byte) toUnsignedByte(pc)) + "]:\t\t" + "Invalid");
		}
	}

	/* Option 6 */
	public void showInstructions(byte[] mem, Scanner scanner) {
		System.out.print("Show up to which address?" +
						" (0 to show all addresses)> ");

		while (!scanner.hasNextShort()) {
			scanner.next();
			System.out.print("\nInvalid address.\n" +
						    "Show up to which address? ");
		}
		short addr = scanner.nextShort();

		if (addr == 0 || addr >= mem.length - 1) {
			addr = (short) mem.length;
		}

		System.out.println("Address:\tInstruction:\tAddress:\tInstruction:");

		short i;
		for (i = 0; i < addr; i++) {
			if (i % 2 == 0 || i == 0) {
				System.out.print(getInstruction(i, mem) + "\t\t");
			} else {
				System.out.print(getInstruction(i, mem) + "\n");
			}
		}

		if (i % 2 == 0)
			System.out.print("Press Enter to continue.");
		else
			System.out.print("\nPress Enter to continue.");

		scanner.nextLine(); /* Consuming \n */
		while (scanner.nextLine() == "") {
			break;
		}
	}

	/* Option 7 */
	public void saveProgram(byte[] mem, Scanner scanner) {
		System.out.print("The saved file will be in bytes.\nFile name? ");
		String pathname = scanner.nextLine();
		try {
			FileOutputStream fos = new FileOutputStream(pathname);
			fos.write(mem);
		} catch (Exception e) {
			System.out.println("Could not save memory.");
		}
	}

	/* Option 8 */
	public void showHelp(Text text, Scanner scanner) {
		text.clearConsole();
		text.help1();
		System.out.print("Press Enter to continue.");
		if (scanner.nextLine() == "")
			text.help2();
		else
			text.help2();
	}

	/* Abstracting getting only one instruction at addr */
	public String getInstruction(short addr, byte[] mem) {
		if (addr > mem.length - 1 || addr < 0)
			return "\nInstruction called with an invalid address";

		String instru = null;
		if (getEleAtOrPC(addr) == 0)
			instru = "STA";
		else if (getEleAtOrPC(addr) == 1)
			instru = "LDA";
		else if (getEleAtOrPC(addr) == 2)
			instru = "STB";
		else if (getEleAtOrPC(addr) == 3)
			instru = "LDB";
		else if (getEleAtOrPC(addr) == 4)
			instru = "STC";
		else if (getEleAtOrPC(addr) == 5)
			instru = "SUM";
		else if (getEleAtOrPC(addr) == 6)
			instru = "SUB";
		else if (getEleAtOrPC(addr) == 7)
			instru = "COM";
		else if (getEleAtOrPC(addr) == 8)
			instru = "JMP";
		else if (getEleAtOrPC(addr) == 9)
			instru = "JPE";
		else if (getEleAtOrPC(addr) == 10)
			instru = "JPG";
		else if (getEleAtOrPC(addr) == 11)
			instru = "JPL";
		else if (getEleAtOrPC(addr) == 12)
			instru = "CONA";
		else if (getEleAtOrPC(addr) == 13)
			instru = "CONB";
		else if (getEleAtOrPC(addr) == 14)
			instru = "STOP";

		if (instru == null)
			return "[" + byteToHex1F((byte) toUnsignedByte((byte) addr)) + "]:\t\t" + "Invalid";
		else if (getEleAtOrPC(addr) == 5 || getEleAtOrPC(addr) == 6 || getEleAtOrPC(addr) == 7 || getEleAtOrPC(addr) == 14) /* addr that don't use param */
			return "[" + byteToHex1F((byte) toUnsignedByte((byte) addr)) + "]:\t\t" + instru;
		else if (addr == mem.length - 1)
			return "[" + byteToHex1F((byte) toUnsignedByte((byte) addr)) + "]:\t\t" + instru + " " + "0";
		else
			return "[" + byteToHex1F((byte) toUnsignedByte((byte) addr)) + "]:\t\t" + instru + " " + byteToHex1F((byte) toUnsignedByte(getEleAtOrPC((byte) (addr + 1))));
	}

	public void controlUnit(byte a, byte b, byte c, byte pc, byte[] mem, ALU alu, IDU idu, Scanner scanner) {
		switch (toUnsignedByte(getEleAtOrPC(pc))) {
		case 0:
			idu.sta(a, getEleAtOrPC((short) (pc + 1)));
			setPC((byte) (pc + 2));
			break;
		case 1:
			idu.lda(pc);
			setPC((byte) (pc + 2));
			break;
		case 2:
			idu.stb(b, getEleAtOrPC((short) (pc + 1)));
			setPC((byte) (pc + 2));
			break;
		case 3:
			idu.ldb(pc);
			setPC((byte) (pc + 2));
			break;
		case 4:
			idu.stc(c, getEleAtOrPC((short) (pc + 1)));
			setPC((byte) (pc + 2));
			break;
		case 5:
			alu.sum(a, b);
			setPC((byte) (pc + 1));
			break;
		case 6:
			alu.sub(a, b);
			setPC((byte) (pc + 1));
			break;
		case 7:
			alu.com(a, b);
			setPC((byte) (pc + 1));
			break;
		case 8:
			idu.jmp(pc);
			break;
		case 9:
			idu.jpe(c, pc);
			break;
		case 10:
			idu.jpg(c, pc);
			break;
		case 11:
			idu.jpl(c, pc);
			break;
		case 12:
			idu.cona(pc);
			setPC((byte) (pc + 2));
			break;
		case 13:
			idu.conb(pc);
			setPC((byte) (pc + 2));
			break;
		case 14:
			System.out.print(" STOP: Terminating execution.\n" +
							" >>> Press enter to continue.");
			if (scanner.nextLine() == "") {
				idu.stop();
				break;
			}
			idu.stop();
			break;
		}
	}

private class ALU { /* arithmetic-logic unit */
	public void sum(byte a, byte b) {
		setC((byte) (a + b));
	}

	public void sub(byte a, byte b) {
		setC((byte) (a - b));
	}

	public void com(byte a, byte b) {
		if (a < b) {
			setC((byte) 1);
		} else if (a > b) {
			setC((byte) 2);
		} else {
			setC((byte) 0);
		}
	}
}

private class IDU { /* instruction decoder unit */
	public void sta(byte a, short index){
		setMemAtIndex(a, index);
	}

	public void lda(byte pc) {
		setA(getEleAtOrPC((short) (pc + 1)));
	}

	public void stb(byte b, short index) {
		setMemAtIndex(b, index);
	}

	public void ldb(byte pc) {
		setB(getEleAtOrPC((short) (pc + 1)));
	}

	public void stc(byte c, short index) {
		setMemAtIndex(c, index);
	}

	public void jmp(byte pc) {
		setPC(getEleAtOrPC((short) (pc + 1)));
	}

	/*
	 * maybe needs to have COM at mem[pc-1] for cond jumps to work? not tested at the uni software.
	 * idea:
	 * if (mem[pc-1] == 7) then jump
	 * else pc+2;
	 */
	public void jpe(byte c, byte pc) {
		if (c == 0) {
			setPC(getEleAtOrPC((short) (pc + 1)));
		}
	}

	public void jpg(byte c, byte pc) {
		if (c == 2) {
			setPC(getEleAtOrPC((short) (pc + 1)));
		}
	}

	public void jpl(byte c, byte pc) {
		if (c == 1) {
			setPC(getEleAtOrPC((short) (pc + 1)));
		}
	}

	public void cona(byte pc) {
		setA(getEleAtOrPC((short) (pc + 1)));
		isAMutable = false;
	}

	public void conb(byte pc) {
		setB(getEleAtOrPC((short) (pc + 1)));
		isBMutable = false;
	}

	public void stop() {
		isStop = true;
	}
}
}