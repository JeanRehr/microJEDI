package com.objmicrojedi;

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

    public void displayMenu(String menuName, String[] menuItems) {
        System.out.print(
            "--------------------------------------------------\n" +
            menuName + "\n" +
            "--------------------------------------------------\n"
        );

        for (int i = 0; i < menuItems.length; i++) {
            System.out.printf("[%1d] %s\n", i+1, menuItems[i]);
        }
        System.out.println("--------------------------------------------------");
    }

    public void displayMenuSide(String menuName, String[] menuItems) {
        System.out.print(
            "--------------------------------------------------\n" +
            menuName + "\n" +
            "--------------------------------------------------\n"
        );

        int j = menuItems.length / 2;
        for (int i = 0; i < menuItems.length / 2; i++) {
            System.out.printf("[%1d] %s\t", i+1, menuItems[i]);
            if (j < menuItems.length) {
                System.out.printf("[%1d] %s\n", j+1, menuItems[j]);
                j++;
            }
        }
        System.out.println("--------------------------------------------------");
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

    public void showHelp1() {
        System.out.print(
        "+======================================================================================+\n" + 
        "|                       HELP - MicroJEDI Architecture Simulator                        |\n" +
        "+======================================================================================+\n" +
        "| This program simulates a 64-byte memory CPU with various components:                 |\n" +
        "| - Arithmetic Logic Unit (ALU)                                                        |\n" +
        "| - Control Unit (CU)                                                                  |\n" +
        "| - Instruction Decoder Unit (IDU)                                                     |\n" +
        "| - Registers A, B, and C for ALU operations                                           |\n" +
        "| - Instruction Register (PC) for controlling execution flow                           |\n" +
        "+--------------------------------------------------------------------------------------+\n" +
        "| OPTIONS:                                                                             |\n" +
        "| [1] Load program: Accepts integers and '0x' hex (max FF, will overflow if more).     |\n" +
        "| [2] Execute program: Starts instruction execution from the address in the PC.        |\n" +
        "| [3] Set mem value: Modify the contents at a specific memory position.                |\n" +
        "| [4] Set register: Modify the contents of a register (A, B, C, or PC).                |\n" +
        "| [5] Display state: Displays current contents of all registers and memory.            |\n" +
        "| [6] Show instructions: View memory contents with instruction names.                  |\n" +
        "| [7] Save program: Save current memory contents to a file.                            |\n" +
        "| [8] Help: Display this help information.                                             |\n" +
        "| [9] Exit: End the execution of the program.                                          |\n" 
        );
    }

    public void showHelp2() {
        System.out.print(
        "+--------------------------------------------------------------------------------------+\n" +
        "| INSTRUCTION SET:                                                                     |\n" +
        "| PC increments by 1 if no parameters (SUM, SUB, MUL, DIV, COM), otherwise by 2.       |\n" +
        "|                                                                                      |\n" +
        "| STA  :  Stores the contents of register A in the memory location indicated by        |\n" +
        "|         the contents of the position following this instruction (mem[pc+1]=a).       |\n" +
        "|         Opcode instruction: 0                                                        |\n" +
        "| LDA  :  Loads into register A the contents of the position whose address is          |\n" +
        "|         in the memory location following this instruction (a=mem[pc+1]).             |\n" +
        "|         Opcode instruction: 1                                                        |\n" +
        "| STB  :  Stores the contents of register B in the memory location indicated by        |\n" +
        "|         the contents of the position following this instruction (mem[pc+1]=b).       |\n" +
        "|         Opcode instruction: 2                                                        |\n" +
        "| LDB  :  Loads into register B the contents of the position whose address is          |\n" +
        "|         in the memory location following this instruction (b=mem[pc+1]).             |\n" +
        "|         Opcode instruction: 3                                                        |\n" +
        "| STC  :  Stores the contents of register C in the memory location indicated by        |\n" +
        "|         the contents of the position following this instruction (mem[pc+1]=c).       |\n" +
        "|         Opcode instruction: 4                                                        |\n" +
        "| SUM  :  Adds the contents of registers A and B, stores the result in register C.     |\n" +
        "|         Opcode instruction: 5                                                        |\n" +
        "| SUB  :  Subtracts the content of register A with the value of register B and         |\n" +
        "|         stores the result in register C. Opcode instruction: 6                       |\n" +
        "| MUL  :  Multiply the contents of registers A and B, stores the result in register C. |\n" +
        "|         Opcode instruction: 7                                                        |\n"
        );
    }

    public void showHelp3() {
        System.out.print(
        "| DIV  :  Divides the content of register A with the value of register B and           |\n" +
        "|         stores the result in register C, dividing by zero signals a STOP.            |\n" +
        "|         Opcode instruction: 8                                                        |\n" +
        "| COM  :  Compares the values of registers A and B, store the result in register C.    |\n" +
        "|         If A=B, C=0; if A<B, C=1; if A>B, C=2.                                       |\n" +
        "|         Opcode instruction: 9                                                        |\n" +
        "| JMP  :  Jumps to address in the position following this instruction (pc=mem[pc+1]).  |\n" +
        "|         Opcode instruction: A                                                        |\n" +
        "| JPE  :  Jumps to address in the position following this instruction                  |\n" +
        "|         if A=B (register C=0).                                                       |\n" +
        "|         Opcode instruction: B                                                        |\n" +
        "| JPG  :  Jumps to address in the position following this instruction                  |\n" +
        "|         if A>B (register C=2).                                                       |\n" +
        "|         Opcode instruction: C                                                        |\n" +
        "| JPL  :  Jumps to address in the position following this instruction                  |\n" +
        "|         if A<B (register C=1).                                                       |\n" +
        "|         Opcode instruction: D                                                        |\n" +
        "| CONA :  Loads a constant value into register A from the memory position              |\n" +
        "|         following this instruction.                                                  |\n" +
        "|         Opcode instruction: E                                                        |\n" +
        "| CONB :  Loads a constant value into register B from the memory position              |\n" +
        "|         following this instruction.                                                  |\n" +
        "|         Opcode instruction: F                                                        |\n" +
        "| STOP :  Terminates execution.                                                        |\n" +
        "|         Opcode instruction: 10                                                       |\n" +
        "+======================================================================================+\n"
        );
    }
}
