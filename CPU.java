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
		Show show = new Show();
		int userInput;
		short value;
		byte pos;
		char regist;

		do {
			show.optionsUC();
			System.out.print("Option:");
			while (!scanner.hasNextInt()) {
				System.out.println("Invalid option.");
				scanner.next();
				System.out.print("Option:");
			}
			userInput = scanner.nextInt();
			scanner.nextLine(); // Consuming \n

			switch (userInput) {
			case 0: // Clear console
				if (System.console() != null)
					show.clearConsole();
				break;
			case 1: // Load program from memory
				System.out.print("Program name?");
				String nome = scanner.nextLine();
				try {
					byte[] memUser = Files.readAllBytes(Paths.get(nome));
					if (memUser.length < mem.length) {
						for (int i = 0; i < memUser.length; i++)
							mem[i] = memUser[i];
					} else {
						for (int i = 0; i < mem.length; i++)
							mem[i] = memUser[i];
					}
					System.out.println("Current state of memory and registers:");
					showMemReg();
				} catch (Exception e) {
					System.out.println("File not found.");
				}
				break;
			case 2: // Execute program
				System.out.println("***** Initianting Execution *****");

				short pc1 = 0;
				if (pc < mem.length - 1 && pc > -1)
					pc1 = unsignedToBytes(mem[pc]);
				else
					pc1 = unsignedToBytes(pc);
				if (pc != 0) {
					System.out.print(" >>> Warning! PC different from 0.\n"
									+ " >>> Press 0 to execute from memory address 0\n"
									+ " >>> Press other number to execute from current"
									+ " address pointed to by PC [" + pc1 + "]."
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
					short pc2 = 0;
					if (pc < mem.length - 1 && pc > -1)
						pc2 = unsignedToBytes(mem[pc]);
					else
						pc2 = unsignedToBytes(pc);
					if (pc > mem.length - 1 || mem[pc2] > 14 || pc2 < 0 || pc < 0) {
						System.out.print(" >>> PC[" + pc2 + "] is not a valid"
										+ " instruction.\n >>> Terminating execution.\n"
										+ " >>> Press Enter to continue");
						enter = scanner.nextLine();
						if (enter.equals(""))
							break;
						else
							break;
					}
					// STOP
					if (mem[pc] == 14) {
						System.out.println("STOP: Terminating execution.");
						break;
					}
					UDI();
					showMemReg();
					System.out.print("Press Enter to continue, a to (A)bort."
									+ "\n--------------------");
					enter = scanner.nextLine();
					if (enter.equalsIgnoreCase("a"))
						break;
					else if (enter.equals(""))
						continue;

				} while (true);
				break;
			case 3: // Change value in memory
				System.out.print("Address (0 to 63):");
				while (!scanner.hasNextByte()) {
					scanner.next();
					System.out.print("Invalid address.\nAddress:");
				}
				pos = scanner.nextByte();

				System.out.print("Value (0 to 255):");
				while (!scanner.hasNextInt()) {
					scanner.next();
					System.out.print("Invalid value .\nValue:");
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

				System.out.print("Value (0 to 255):");
				while (!scanner.hasNextShort()) {
					scanner.next();
					System.out.print("Invalid value .\nValue:");
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
					System.out.println("Register does not exit or invalid value.");
				}
				break;
			case 5: // Show memory and registers
				showMemReg();
				break;
			case 6: // Show instructions
				System.out.print("Show up to which address?\n"
								+ "0 to show all addresses:");
				while (!scanner.hasNextInt()) {
					System.out.print("\nInvalid address.\n"
									+ "Show up to which address?");
				}
				int addr = scanner.nextInt();
				System.out.println("Address:\tInstruction:");
				if (addr == 0 || addr > 63) {
					int i = 0;
					while (i < mem.length) {
						getInstruction(i);
						if (mem[i] == 5 || mem[i] == 6 || mem[i] == 7 || mem[i] == 14)
							i++;
						else
							i += 2;
					}
				} else {
					int i = 0;
					while (i < addr) {
						getInstruction(i);
						if (mem[i] == 5 || mem[i] == 6 || mem[i] == 7 || mem[i] == 14)
							i++;
						else
							i += 2;
					}
				}

				break;
			case 7: // Save program
				System.out.print("File name?");
				Writer writer = null;
				String file = scanner.nextLine();
				try {
					writer = new BufferedWriter(new OutputStreamWriter(
									new FileOutputStream(file), "utf-8"));

					for (int i = 0; i < mem.length; i++)
						writer.write((mem[i]));

					writer.close();
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
		} while (userInput != 9);
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
		if (pc > mem.length - 1)
			return;

		switch (mem[pc]) {
		case 0: // STA
			mem[pc + 1] = a;
			if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 1: // LDA
			a = mem[pc + 1];
			if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 2: // STB
			mem[pc + 1] = b;
			if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 3: // LDB
			b = mem[pc + 1];
			if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 4: // STC
			c = mem[pc + 1];
			if (pc == 63)
				pc++;
			else
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
			pc = mem[pc + 1];
			break;
		case 9: // JPE
			if (c == 0)
				pc = mem[pc + 1];
			else if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 10: // JPG
			if (c == 2)
				pc = mem[pc + 1];
			else if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 11: // JPL
			if (c == 1)
				pc = mem[pc + 1];
			else if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 12: // CONA
			a = mem[pc + 1];
			if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 13: // CONB
			b = mem[pc + 1];
			if (pc == 63)
				pc++;
			else
				pc += 2;
			break;
		case 14: // STOP
			break;
		}
	}

	public void getInstruction(int i)
	{
		if (i > mem.length - 1 || i < 0)
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

		short pc2 = 0;
		if (pc < mem.length - 1 && pc > -1) {
			if (i == 63)
				pc2 = 0;
			else
				pc2 = unsignedToBytes(mem[i + 1]);
		}

		if (instru == null || pc > 63)
			System.out.print("[" + i + "]:\t\t" + "Invalid.\n");
		else if (mem[i] == 5 || mem[i] == 6 || mem[i] == 7 || mem[i] == 14)
			System.out.print("[" + i + "]:\t\t" + instru + "\n");
		else
			System.out.print("[" + i + "]:\t\t" + instru + " " + pc2 + "\n");
	}

	public void showMemReg()
	{
		int count = 0;
		short aUn = unsignedToBytes(a);
		short bUn = unsignedToBytes(b);
		short cUn = unsignedToBytes(c);
		short pcUn = unsignedToBytes(pc);
		System.out.print("Registers:\tA:" + aUn + "\tB:" + bUn + "\tC:" + cUn + "\t[PC:"
						+ pcUn + "]\n");
		System.out.println("Memory: --------------------------------------------------"
						+ "--------------+");
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
			if (pc < mem.length && pc >= 0) {
				pc1 = unsignedToBytes(mem[pc]);
				pc2 = unsignedToBytes(mem[i]);
				if (pc == i)
					System.out.print("\t" + "<" + pc1 + ">");
				else
					System.out.print("\t" + pc2);
			} else {
				int j = i;
				j = unsignedToBytes(mem[i]);
				System.out.print("\t" + j);
			}

			count++;
			if (count % 8 == 0)
				System.out.println("\t|");

		}
		System.out.println("-----------------------------------------------------------"
						+ "-------------+");

		if (pc < mem.length && pc > -1) {
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
