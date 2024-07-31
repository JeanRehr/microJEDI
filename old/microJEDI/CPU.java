package microJEDI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.*;

public class CPU {
	private byte a, b, c;
	private byte pc;
	private byte[] mem = new byte[64];

	public void CU() { // Control Unit
		Scanner scanner = new Scanner(System.in);
		Show show = new Show();
		int userInput;
		short value;
		short pos;
		char regist;

		do {
			show.optionsUC();
			System.out.print("Option> ");
			while (!scanner.hasNextInt()) {
				System.out.println("Invalid option.");
				scanner.next();
				System.out.print("Option> ");
			}
			userInput = scanner.nextInt();
			scanner.nextLine(); // Consuming \n

			switch (userInput) {
			case 0: // Clear console
				if (System.console() != null)
				    show.clearConsole();
				break;
			case 1: // Load program from memory
				System.out.print("Program name? ");
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
					System.out.println("Current state of memory and registers:");
					showMemReg2(a, b, c, pc, mem);
					
				} catch (Exception e) {
					System.out.println("File not found.");
				}
				break;
			case 2: // Execute program

				/*
				short pc1;
				if (pc < mem.length - 1 && pc > -1)
					pc1 = bytesToUnsigned(mem[pc]);
				else
					pc1 = bytesToUnsigned(pc);
				*/

				if (pc != 0) {
					System.out.print(" >>> Warning! PC different from 0.\n"
									+ " >>> Press 0 to execute from memory address 0\n"
									+ " >>> Press other number to execute from current"
									+ " address pointed to by PC [" + bytesToUnsigned(pc) + "]."
									+ "\nOption:");
					while (!scanner.hasNextInt()) {
						scanner.next();
						System.out.print("Invalid Option.\nOption> ");
					}
					int codPC = scanner.nextInt();
					if (codPC == 0)
						pc = 0;

					scanner.nextLine(); // Consuming \n
				}
				String enter = null;
				System.out.println("***** Initianting Execution *****");
				while (true) {
					showMemReg2(a, b, c, pc, mem);

					byte pc2 = 0;
					if (pc > mem.length - 1) {
						pc2 = pc;
					}

					if (pc < 0 || bytesToUnsigned(pc2) > 14 || bytesToUnsigned(mem[pc]) > 14) {
						System.out.print("\n >>> Instruction PC[" + bytesToUnsigned(pc) +
										"] is not valid.\n >>> Terminating execution.\n" +
										" >>> Press Enter to continue");

						enter = scanner.nextLine();
						if (enter.equals("\n"))
							break;
						else
							break;
					}
					// STOP
					if (mem[pc] == 14) {
						System.out.print("\nSTOP: Terminating execution.\n" +
											">>> Press enter to continue.");
						scanner.nextLine();
						if (enter.equals("\n"))
							break;
						else
							break;
					}
					System.out.print("\nPress Enter to continue, a to (A)bort.\n"
									+ "> ");
					enter = scanner.nextLine();
					if (enter.equalsIgnoreCase("a"))
						break;

					CPU_com();
				}
				break;
			case 3: // Change value in memory
				System.out.print("Address (0 to " + (mem.length - 1) + ")> ");
				while (!scanner.hasNextShort()) {
					scanner.next();
					System.out.print("Invalid address.\nAddress> ");
				}
				pos = scanner.nextShort();

				System.out.print("Value (0 to 255)> ");
				while (!scanner.hasNextShort()) {
					scanner.next();
					System.out.print("Invalid value .\nValue> ");
				}
				value = scanner.nextShort();

				if (value >= 0 && value <= 255 && pos >= 0 && pos < mem.length)
					mem[pos] = (byte) value;
				else
					System.out.println("Invalid value or address.");

				break;
			case 4: // Change register value
				System.out.print("Register (A, B, C, PC)> ");
				regist = scanner.next().charAt(0);

				System.out.print("Value (0 to 255)> ");
				while (!scanner.hasNextShort()) {
					scanner.next();
					System.out.print("Invalid value .\nValue> ");
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
				//showMemReg(); old
				showMemReg2(a, b, c, pc, mem);
				break;
			case 6: // Show instructions
				System.out.print("Show up to which address?"
								+ " (0 to show all addresses)> ");

				while (!scanner.hasNextInt()) {
					scanner.next();
					System.out.print("\nInvalid address.\n"
									+ "Show up to which address? ");
				}
				int addr = scanner.nextInt();
				// new version
				if (addr == 0 || addr >= mem.length - 1) {
					addr = mem.length;
				}

				System.out.println("Address:\tInstruction:\tAddress:\tInstruction:");

				for (int i = 0; i < addr; i++) {
					if (i % 2 == 0 || i == 0) {
						getInstructionAtAddr(i, pc, mem);
						System.out.print("\t\t");
					} else {
						getInstructionAtAddr(i, pc, mem);
						System.out.print("\n");
					}
				}
				System.out.print("Press Enter to continue.");
				String cont = null;
				scanner.nextLine();
				while (true) {
					cont = scanner.nextLine();
					break;
				}
				/* old
				if (addr == 0 || addr > mem.length - 1) {
					int i = 0;
					while (i < mem.length) {
						getInstruction(i);
						if (mem[i] == 5 || mem[i] == 6 || mem[i] == 7 || mem[i] == 14)
							i++;
						else
							i++;;
					}
				} else {
					int i = 0;
					while (i < addr) {
						getInstruction(i);
						if (mem[i] == 5 || mem[i] == 6 || mem[i] == 7 || mem[i] == 14)
							i++;
						else
							i++;;
					}
				}
				*/
				break;
			case 7: // Save program
				System.out.print("File name? ");
				String pathname = scanner.nextLine();
				try {
					FileOutputStream fos = new FileOutputStream(pathname);
					fos.write(mem);
				} catch (Exception e) {
					System.out.println("Could not save memory.");
				}
				break;
			case 8: // Help
				show.clearConsole();
				show.help1();
				System.out.print("Press Enter to continue.");
				String contHelp = scanner.nextLine();
				if (contHelp == "")
					show.help2();
				else
					show.help2();
				break;
			case 9:
				System.exit(0);
			}
		} while (userInput != 9);
		scanner.close();
	}

	// Start Arithmethic-Logic Unit
	public void sum() {
		if (a + b > 255)
			c = (byte) ((a + b) - 256);
		else
			c = (byte) (a + b);

		pc += 1;
	}

	public void sub() {
		if (a - b < 0)
			c = (byte) ((a - b) + 256);
		else
			c = (byte) (a - b);

		pc += 1;
	}

	public void com() {
		if (a > b)
			c = 2;
		else if (a < b)
			c = 1;
		else
			c = 0;

		pc += 1;
	}
	// End ALU

	public void CPU_com() {
		if (pc > mem.length - 1)
			return;

		switch (mem[pc]) {
		case 0: // STA
			if (pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			mem[pc + 1] = a;
			pc += 2;
			break;
		case 1: // LDA
			if (pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			a = mem[pc + 1];
			pc += 2;
			break;
		case 2: // STB
			if (pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			mem[pc + 1] = b;
			pc += 2;
			break;
		case 3: // LDB
			if (pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			b = mem[pc + 1];
			pc += 2;
			break;
		case 4: // STC
			if (pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			mem[pc + 1] = c;
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
			if (pc >= mem.length - 1)
				pc += 2;
			else
				pc = mem[pc + 1];
			break;
		/*
		TODO:
		JPE, JPG and JPL will execute even if the instruction before is not COM
		however it must be preceded by COM
		idea:
		set flags based on comparasion, then jump based on flags
		if mem[pc - 1] == 7, execute cond jump
		*/
		case 9: // JPE
			if (c != 0 || pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			pc = mem[pc + 1];
			break;
		case 10: // JPG
			if (c != 2 || pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			pc = mem[pc + 1];
			break;
		case 11: // JPL
			if (c != 1 || pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			pc = mem[pc + 1];
			break;
		/*
		TODO:
		Make Register A and B not mutable after calling CONA and CONB
		PSEUDO CODE:
		fn setValue (int value)
			if isMutable
				this = value;
		*/
		case 12: // CONA
			if (pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			a = mem[pc + 1];
			pc += 2;
			break;
		case 13: // CONB
			if (pc >= mem.length - 1) {
				pc += 2;
				break;
			}
			b = mem[pc + 1];
			pc += 2;
			break;
		case 14: // STOP
			break;
		}
	}

	public void getInstructionAtAddr(int addr, byte pc, byte[] mem) {
		if (addr > mem.length - 1 || addr < 0)
			return;

		String instru = null;
		if (mem[addr] == 0)
			instru = "STA";
		else if (mem[addr] == 1)
			instru = "LDA";
		else if (mem[addr] == 2)
			instru = "STB";
		else if (mem[addr] == 3)
			instru = "LDB";
		else if (mem[addr] == 4)
			instru = "STC";
		else if (mem[addr] == 5)
			instru = "SUM";
		else if (mem[addr] == 6)
			instru = "SUB";
		else if (mem[addr] == 7)
			instru = "COM";
		else if (mem[addr] == 8)
			instru = "JMP";
		else if (mem[addr] == 9)
			instru = "JPE";
		else if (mem[addr] == 10)
			instru = "JPG";
		else if (mem[addr] == 11)
			instru = "JPL";
		else if (mem[addr] == 12)
			instru = "CONA";
		else if (mem[addr] == 13)
			instru = "CONB";
		else if (mem[addr] == 14)
			instru = "STOP";

		short pc2 = 0; // number in front of instruction, denoting the parameter of next instruction at <pc>
		if (addr == mem.length - 1)
			pc2 = 0;
		else
			pc2 = bytesToUnsigned(mem[addr + 1]);

		if (instru == null)
			System.out.print("[" + addr + "]:\t\t" + "Invalid");
		else if (mem[addr] == 5 || mem[addr] == 6|| mem[addr] == 7 || mem[addr] == 14) // SUM, SUB, COM and STOP does not use parameter
			System.out.print("[" + addr + "]:\t\t" + instru);
		else
			System.out.print("[" + addr + "]:\t\t" + instru + " " + pc2);
		
	}

	public void showMemReg2(byte a, byte b, byte c, byte pc, byte[] mem) {
		short count = 0;
		System.out.print("Registers:\tA:" + bytesToUnsigned(a) + "\tB:" + bytesToUnsigned(b) +
						"\tC:" + bytesToUnsigned(c) + "\t[PC:" + bytesToUnsigned(pc) + "]\n");

		System.out.println("Memory: ------------------------------------------------" +
						"----------------+");
		for (short i = 0; i <= 7; i++) { // top numbers
			System.out.print("\t0" + i);
			if (i == 7)
				System.out.print("\t|");
		}
		System.out.println("\n\t\t\t\t\t\t\t\t\t|");

		for (short i = 0; i < mem.length; i++) { // show memory values byte array
			if (i == 0 || i % 8 == 0) //right numbers
				System.out.print(i + ":");

			if (pc < mem.length && pc >=0) {
				if (pc == i)
					System.out.print("\t" + "<" + bytesToUnsigned(mem[pc]) + ">");
				else
					System.out.print("\t" + bytesToUnsigned(mem[i]));

			} else {
				System.out.print("\t" + bytesToUnsigned(mem[i]));
			}

			count++;

			if (count % 8 == 0)
				System.out.println("\t|");
		}
		if (count % 8 != 0)
			System.out.println("\n---------------------------------------------------" +
							"---------------------+");
		else
			System.out.println("-----------------------------------------------------" +
							"-------------------+");

		if (pc <= mem.length - 1 && pc >= 0) {
			System.out.print("Next instruction: ");
			getInstructionAtAddr(pc, pc, mem);
		} else {
			System.out.print("Next instruction: [" + bytesToUnsigned(pc) + "]:\t\t" + "Invalid");
		}
	}

	public void showMemReg() {
		short count = 0;
		short aUn = bytesToUnsigned(a);
		short bUn = bytesToUnsigned(b);
		short cUn = bytesToUnsigned(c);
		short pcUn = bytesToUnsigned(pc);
		System.out.print("Registers:\tA:" + aUn + "\tB:" + bUn + "\tC:" + cUn + "\t[PC:"
						+ pcUn + "]\n");
		System.out.println("Memory: --------------------------------------------------"
						+ "--------------+");
		for (short i = 0; i <= 7; i++) { // top numbers
			System.out.print("\t0" + i);
			if (i == 7)
				System.out.print("\t|");
		}
		System.out.println("\n\t\t\t\t\t\t\t\t\t|");
		for (short i = 0; i < mem.length; i++) { // Show the memory value
			if (i == 0 || i % 8 == 0) // right numbers
				System.out.print(i + ":");

			short pc1 = 0;
			short pc2 = 0;
			if (pc < mem.length && pc >= 0) {
				pc1 = bytesToUnsigned(mem[pc]);
				pc2 = bytesToUnsigned(mem[i]);
				if (pc == i)
					System.out.print("\t" + "<" + pc1 + ">");
				else
					System.out.print("\t" + pc2);
			} else {
				short pc3 = i;
				pc3 = bytesToUnsigned(mem[i]);
				System.out.print("\t" + pc3);
			}

			/* OLD ONE USED AT THE NEW VERSION STORED HERE JUST FOR COMPARASION
			if (pc >= 0 && pc < mem.length) {
				if (pc == i) {
					System.out.print("\t" + "<" + byteToHex1F((byte) toUnsignedByte(mem[pc])) + ">");
				} else {
					System.out.print("\t" + byteToHex1F((byte) toUnsignedByte(mem[i])));
				}
			} else {
				System.out.print("\t" + byteToHex1F((byte) toUnsignedByte(mem[i])));
			}
			*/

			count++;
			if (count % 8 == 0)
				System.out.println("\t|");
		}
		if (count % 8 != 0)
			System.out.println("\n-----------------------------------------------------"
							+ "-------------------+");
		else
			System.out.println("-------------------------------------------------------"
							+ "-----------------+");

		if (pc < mem.length && pc > -1) {
			System.out.print("Next instruction: ");
			getInstruction(pc);
		} else {
			System.out.println("Next instruction: ");
			getInstruction(pc);
		}
	}

	public void getInstruction(int i) {
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

		short pc2 = 0; // number in front of instruction, denoting the parameter of next instruction at <pc>
		if (pc < mem.length - 1 && pc > -1) {
			if (i == mem.length - 1)
				pc2 = 0;
			else
				pc2 = bytesToUnsigned(mem[i + 1]);
		}

		if (instru == null)
			System.out.print("[" + i + "]:\t\t" + "Invalid\n");
		else if (mem[i] == 5 || mem[i] == 6|| mem[i] == 7) // SUM, SUB and COM does not use parameters
			System.out.print("[" + i + "]:\t\t" + instru + "\n");
		else
			System.out.print("[" + i + "]:\t\t" + instru + " " + pc2 + "\n");
	}

	public short bytesToUnsigned(byte a)
	{
		short b = (short) (a & 0xFF);
		return b;
	}
}