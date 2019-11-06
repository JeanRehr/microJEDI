package microJEDI;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class CPU
{
	private byte a, b, c;
	private byte pc; // Program counter
	private byte[] mem = new byte[64];

	public void UC()
	{
		Scanner scanner = new Scanner(System.in);
		Mostrar show = new Mostrar();
		int userInput;
		byte pos;
		short value;
		char regist;

		do {
			show.optionsUC();
			System.out.print("Option:");
			while (!scanner.hasNextInt()) {
				System.out.print("Invalid Option\n");
				scanner.next();
				System.out.print("Option:");
			}
			userInput = scanner.nextInt();
			scanner.nextLine(); // Consuming \n

			switch (userInput) {
			case 0: // Clear console
				show.clearConsole();
				break;
			case 1: // Load program from file
				System.out.print("Program name?");
				String name = scanner.nextLine();
				try {
					byte[] memUser = Files.readAllBytes(Paths.get(name));
					if (memUser.length < mem.length) {
						for (int i = 0; i < memUser.length; i++)
							mem[i] = memUser[i];
					} else {
						for (int i = 0; i < mem.length; i++)
							mem[i] = memUser[i];
					}
				} catch (Exception e) {
					System.out.println("File not found.");
				}
				System.out.print("State of memory and registers:");
				showMemReg();
				break;
			case 2: // Execute program
				System.out.println("***** Initiating Execution *****");
				if (pc != 0) {
					System.out.print(" >>> Atention! PC different from 0.\n"
									+ " >>> Press 0 to execute from memory address 0\n"
									+ " >>> Press other number to execute from current"
									+ " address pointed to by PC [" + pc + "]."
									+ "\nOption:");
					while (!scanner.hasNextInt()) {
						scanner.next();
						System.out.print("Invalid Option.\nOption:");
					}
					int codPC = scanner.nextInt();
					if (codPC == 0)
						pc = 0;

					scanner.nextLine(); // Consuming \n
				}

					String enter = null;
					do {
						if (pc > mem.length - 1 || mem[pc] > 14 || mem[pc] < 0) {
							System.out.print(" >>> PC[" + pc + "] is not a valid"
											+ " instruction.\n >>> Ending execution.\n"
											+ " >>> Press Enter to continue");
							enter = scanner.nextLine();
							if (enter.equals(""))
								break;
							else
								break;
						}
						// if STOP end
						if (mem[pc] == 14) {
							System.out.println("STOP: Ending execution.");
							break;
						}
						UDI();
						showMemReg();
						System.out.print("Press Enter to continue, other key to abort."
										+ "\n--------------------");
						enter = scanner.nextLine();
					} while (enter.equals(""));
				break;
			case 3: // Change value in mem
				System.out.print("Address (0 to 63):");
				while (!scanner.hasNextShort()) {
					scanner.next();
					System.out.print("Invalid address.\nAddress:");
				}
				pos = (byte) scanner.nextShort();

				System.out.print("Value (0 from 255):");
				while (!scanner.hasNextInt()) {
					scanner.next();
					System.out.print("Invalid value.\nValue:");
				}
				value = scanner.nextShort();

				if (value >= 0 && value <= 255 && pos >= 0 && pos < mem.length)
					mem[pos] = (byte) value;
				else
					System.out.println("Invalid value or address.");
				break;
			case 4: // Change register value 
				System.out.print("Register (A, B, C, PC):");
				regist = scanner.next().charAt(0);

				System.out.print("Value (0 from 255):");
				while (!scanner.hasNextShort()) {
					scanner.next();
					System.out.print("Invalid value.\nValue:");
				}
				value = scanner.nextShort();

				switch (regist) {
				case 'a':
				case 'A':
					if (value >= 0 && value <= 255)
						a = (byte) value;
					else
						System.out.println("Invalid value.");
					break;
				case 'b':
				case 'B':
					if (value >= 0 && value <= 255)
						b = (byte) value;
					else
						System.out.println("Invalid value.");
					break;
				case 'c':
				case 'C':
					if (value >= 0 && value <= 255)
						c = (byte) value;
					else
						System.out.println("Invalid value.");
					break;
				case 'p':
				case 'P':
					if (value >= 0 && value <= 255)
						pc = (byte) value;
					else
						System.out.println("Invalid value.");
					break;
				default:
					System.out.println("Register does not exist or invalid value.");
				}
				break;
			case 5: // Show mem and register
				showMemReg();
				break;
			case 6: // Show instructions
				System.out.println("Address.\tInstruction.");
				for (int i = 0; i < mem.length; i++)
					getInstruction(i);
				break;
			case 7: // Save mem
				System.out.print("File name?");
				Writer w = null;
				String arquivo = scanner.nextLine();
				try {
					w = new BufferedWriter(new OutputStreamWriter(
									new FileOutputStream(arquivo), "utf-8"));
					for (int i = 0; i < mem.length; i++)
						w.write((mem[i]));
					w.close();
				} catch (Exception e) {
					System.out.println("Could not save memory.");
				}
				break;
			case 8: // Help
				show.help();
				break;
			case 9:
				System.exit(0);
			}
		} while (userInput != 1);
		scanner.close();
	}

	// Start ULA
	public void sum()
	{
		if (a + b > 255)
			c = (byte) ((a + b) - 256);
		else
			c = (byte) (a + b);

		pc += 1;
	}

	public void sub()
	{
		if (a - b < 0)
			c = (byte) ((a - b) + 256);
		else
			c = (byte) (a - b);

		pc += 1;
	}

	public void com()
	{
		if (a > b)
			c = 2;
		else if (a < b)
			c = 1;
		else
			c = 0;

		pc += 1;
	}
	// End ULA

	public void UDI()
	{
		if (pc >= mem.length - 1 || pc > mem.length)
			return;

		switch (mem[pc]) {
		case 0: // STA
			mem[pc + 1] = a;
			pc += 2;
			break;
		case 1: // LDA
			a = mem[pc + 1];
			pc += 2;
			break;
		case 2: // STB
			mem[pc + 1] = b;
			pc += 2;
			break;
		case 3: // LDB
			b = mem[pc + 1];
			pc += 2;
			break;
		case 4: // STC
			c = mem[pc + 1];
			pc += 2;
			break;
		case 5: // SUM
			sum();
			break;
		case 6: // SUB
			sub();
			break;
		case 7: // COM
			com();
			break;
		case 8: // JMP
			pc = mem[++pc];
			break;
		case 9: // JPE
			if (c == 0)
				pc = mem[++pc];
			else
				pc += 2;
			break;
		case 10: // JPG
			if (c == 2)
				pc = mem[++pc];
			else
				pc += 2;
			break;
		case 11: // JPL
			if (c == 1)
				pc = mem[++pc];
			else
				pc += 2;
			break;
		case 12: // CONA
			a = mem[pc + 1];
			pc += 2;
			break;
		case 13: // CONB
			b = mem[pc + 1];
			pc += 2;
			break;
		case 14: // STOP
			break;
		}
	}

	public void getInstruction(int i)
	{
		if (i > mem.length - 1)
			return;

		String instru = null;
		if (mem[i] == 0)
			instru = "STA";
		else if (mem[i] == 1)
			instru = "LDA";
		else if (mem[i] == 2)
			instru = "STB";
		else if (mem[i] == 3)
			instru = "LDB";
		else if (mem[i] == 4)
			instru = "STC";
		else if (mem[i] == 5)
			instru = "SUM";
		else if (mem[i] == 6)
			instru = "SUB";
		else if (mem[i] == 7)
			instru = "COM";
		else if (mem[i] == 8)
			instru = "JMP";
		else if (mem[i] == 9)
			instru = "JPE";
		else if (mem[i] == 10)
			instru = "JPG";
		else if (mem[i] == 11)
			instru = "JPL";
		else if (mem[i] == 12)
			instru = "CONA";
		else if (mem[i] == 13)
			instru = "CONB";
		else if (mem[i] == 14)
			instru = "STOP";

		if (instru == null || i > mem.length) {
			System.out.println("[" + i + "]:\t" + "Invalido.");
		} else {
			System.out.println("[" + i + "]:\t" + instru);
		}
	}

	public void showMemReg()
	{
		int count = 0;
		short aUn = unsignedToBytes(a);
		short bUn = unsignedToBytes(b);
		short cUn = unsignedToBytes(c);
		short pcUn = unsignedToBytes(pc);
		System.out.print("Registradores:\tA:" + aUn + "\tB:" + bUn + "\tC:" + cUn + "\t[PC:"
						+ pcUn + "]\n");
		System.out.println("Memoria: --------------------------------------------------"
						+ "-------------+");
		for (int i = 0; i <= 7; i++) {
			System.out.print("\t0" + i);
			if (i == 7)
				System.out.print("\t|");
		}
		System.out.println("\n\t\t\t\t\t\t\t\t\t|");
		for (int i = 0; i < mem.length; i++) {
			if (i == 0 || i % 8 == 0)
				System.out.print(i + ":");

			short pc1 = 0;
			short pc2 = 0;
			if (pc < mem.length - 1) {
				pc1 = unsignedToBytes(mem[pc]);
				pc2 = unsignedToBytes(mem[i]);
			}

			if (pc == i)
				System.out.print("\t" + "<" + pc1 + ">");
			else
				System.out.print("\t" + pc2);

			count++;
			if (count % 8 == 0)
				System.out.println("\t|");

		}
		System.out.println("-----------------------------------------------------------"
						+ "-------------+");

		if (pc < mem.length) {
			System.out.print("Next instruction: ");
			getInstruction(pc);
		} else {
			System.out.println("Next instruction: ");
			getInstruction(pc);
		}
	}

	public short unsignedToBytes(byte a)
	{
	    short b = (short) (a & 0xFF);
	    return b;
	}
}