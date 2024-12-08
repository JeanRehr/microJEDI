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
        "|         the contents of the position following this instruction.                     |\n" +
        "|         Opcode instruction: 0                                                        |\n" +
        "| LDA  :  Loads into register A the contents of the position whose address is          |\n" +
        "|         in the memory location following this instruction.                           |\n" +
        "|         Opcode instruction: 1                                                        |\n" +
        "| STB  :  Stores the contents of register B in the memory location indicated by        |\n" +
        "|         the contents of the position following this instruction.                     |\n" +
        "|         Opcode instruction: 2                                                        |\n" +
        "| LDB  :  Loads into register B the contents of the position whose address is          |\n" +
        "|         in the memory location following this instruction.                           |\n" +
        "|         Opcode instruction: 3                                                        |\n" +
        "| STC  :  Stores the contents of register C in the memory location indicated by        |\n" +
        "|         the contents of the position following this instruction.                     |\n" +
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
        "| JMP  :  Jumps to address in the position following this instruction.                 |\n" +
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

/*
UNISINOS
Universidade do Vale do Rio dos Sinos
Introduction to Computing and its Applications - GT-JEDi
Simulator of the microJEDI CPU architecture
Document version: 1.0 Mar 2009

The microJEDI architecture is a hypothetical architecture developed for educational purposes.
It is based on an Arithmetic Logic Unit (ALU), a Control Unit (CU), an Instruction Decoder Unit (UDI),
3 registers associated with the ALU (registers A, B and C) and an instruction register (PC).
The following diagram illustrates the structure of the architecture:

    C----------------------|
    ↑                      |
    |                      |
   ALU←---------CU---->PC  |
    ↑            ↑     ↑   |
____|____        |     |   |
|       |        ↓     |   |
A       B       IDU←---|   |
↑       ↑        ↑         |
|       |        |         |
‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
                           ↓
                       Data Addr

Registers:
1.    are 8 bits
2.    represent unsigned integer values ​​(0 to 255)
3.    A and B serve as input parameters for the ALU
4.    C is used to store the result of the operations performed by the ALU
5.    The contents of the PC (Program Counter) register represent the address of the next instruction
    to be executed by the CPU.
6.    After executing an instruction, the contents of the PC register are changed, being incremented
    by 2 or 1, depending on the last instruction executed; if the last instruction had no parameter
    (as in the cases of SUM, SUB, COM, for example), the increment is 1, otherwise it is 2.

PC = program counter, address of the next instruction to be executed

ALU - Arithmetic Logic Unit:
1. performs the operations of:
    a.    addition: when the SUM instruction is executed; it adds the contents of registers A and B
        and stores the result of the operation in register C; if the result of the addition exceeds
        255, an OVERFLOW event occurs, and the result stored in C will be (A+B) -256.
    b.    subtraction: when the SUB instruction is executed; it performs the operation A - B and stores
        the result of the operation in register C; if the result of the subtraction is less than
        0 (zero), an UNDERFLOW event occurs, and the result stored in C will be (A-B) + 256.
    c.    comparison: when the COM instruction is executed; it compares the values ​​of registers A and B;
        if A is equal to B, then register C is loaded by the ALU with the value 0 (zero);
        if A<B, C is loaded with the value 1 (one) and if A>B, C is loaded with the value 2.
2. The ALU is controlled by the CU, which is responsible for its activation.

CU - Control Unit:
receives the opcode
The control unit coordinates the activation of the various blocks that make up the CPU,
determining which operations should be performed and when they should be performed.
It controls the activation of the ALU and the IDU.

IDU - Instruction Decoder Unit:
logic of instructions
The instructions to be executed arrive from memory via the system bus.
This unit receives the code (opcode) of the instruction to be executed and decodes it,
informing the CU about the instruction to be executed.

The architecture simulator:
This program simulates a 64-byte memory accessed by the hypothetical CPU described above;
it allows the execution of the instructions contained in this memory,
allowing the reading and modification of the data contained therein.

The memory is represented by a matrix of 8 rows and 8 columns, as shown in the figure below:

Registers:      A:0     B:0     C:0     [PC:0]
Memory: ----------------------------------------------------------------+
        00      01      02      03      04      05      06      07      |
                                                                        |
00:     <0>     0       0       0       0       0       0       0       |
08:     0       0       0       0       0       0       0       0       |
10:     0       0       0       0       0       0       0       0       |
18:     0       0       0       0       0       0       0       0       |
20:     0       0       0       0       0       0       0       0       |
28:     0       0       0       0       0       0       0       0       |
30:     0       0       0       0       0       0       0       0       |
38:     0       0       0       0       0       0       0       0       |
------------------------------------------------------------------------+
Next instruction: [0]:          STA 0

Each line of the matrix represents 8 consecutive bytes of memory. The first line represents the
first 8 bytes, the second line the next 8 bytes, and so on.
The numbering of the columns and lines, as well as the values ​​of the memory positions, are all
presented in hexadecimal.

The options offered by the simulator are:
1.    load program into memory: allows the user to manually load data and instructions into memory
    or to load data and instructions from a file on disk;
2.    execute program: activates the instruction search and execution cycle; this cycle begins at
    the memory position whose address corresponds to the value of the PC register;
    when the STOP instruction is executed, this cycle is deactivated;
3.    change memory position: allows the user to change the contents of a specific memory position;
4.    change register: allows the user to change the contents of a specific register;
5.    show memory and registers: displays the contents of all registers as well as the matrix that
    represents the architecture's memory;
6.    show instructions: shows the contents of memory, but presents the user with the name of the
    instructions stored in each memory position;
7.    save program to file: allows the contents of memory to be saved (recorded) in a file on disk;
8.    help: presents a summary of the instructions supported by the CPU;
9.    exit: ends the execution of the program.

Example:
-------------------------------------------------------------------------------
Options:[1] Load program from file.     [6] Show instructions.
        [2] Execute program.            [7] Save program to file.
        [3] Change value in memory.     [8] Help.
        [4] Change register.            [9] Exit.
        [5] Show memory and registers.
-------------------------------------------------------------------------------

Instructions supported by the CPU:
+=============+==============+=====================================================================================+
| Instruction | Opcode (hex) |                                    What it does                                     |
+=============+==============+=====================================================================================+
| STA         | 0            | Stores the contents of register A in the memory location indicated                  |
|             |              | by the contents of the position following this instruction.                         |
+-------------+--------------+-------------------------------------------------------------------------------------+
| LDA         | 1            | Loads into register A the contents of the position whose address is in              |
|             |              | the memory location following this instruction.                                     |
+-------------+--------------+-------------------------------------------------------------------------------------+
| STB         | 2            | Stores the contents of register B in the memory location indicated                  |
|             |              | by the contents of the position following this instruction.                         |
+-------------+--------------+-------------------------------------------------------------------------------------+
| LDB         | 3            | Loads into register B the contents of the position whose address is in              |
|             |              | the memory location following this instruction.                                     |
+-------------+--------------+-------------------------------------------------------------------------------------+
| STC         | 4            | Stores the contents of register C in the memory location indicated                  |
|             |              | by the contents of the position following this instruction.                         |
+-------------+--------------+-------------------------------------------------------------------------------------+
| SUM         | 5            | Adds the contents of registers A and B and stores the result in register C.         |
+-------------+--------------+-------------------------------------------------------------------------------------+
| SUB         | 6            | Performs the operation A – B and stores the result in C.                            |
+-------------+--------------+-------------------------------------------------------------------------------------+
| COM         | 7            | Compares the values ​​of registers A and B, and places the result of the comparison   |
|             |              | in register C; if A=B, C=0; if A<B, C=1; if A>B, C=2.                               |
+-------------+--------------+-------------------------------------------------------------------------------------+
| JMP         | 8            | Diverts the execution flow to the address contained at the position following this  |
|             |              | memory instruction. This is an unconditional jump. This instruction loads to        |
|             |              | register PC the value of the memory position following this instruction.            |
+-------------+--------------+-------------------------------------------------------------------------------------+
| JPE         | 9            | Diverts the execution flow to the address contained at the position following this  |
|             |              | memory instruction. This is a conditional jump. Will only occur if A=B              |
|             |              | (register C=0). Must be preceded by COM instruction.                                |
+-------------+--------------+-------------------------------------------------------------------------------------+
| JPG         | A            | Diverts the execution flow to the address contained at the position following this  |
|             |              | memory instruction. This is a conditional jump. Will only occur if A>B              |
|             |              | (register C=2). Must be preceded by COM instruction.                                |
+-------------+--------------+-------------------------------------------------------------------------------------+
| JPL         | B            | Diverts the execution flow to the address contained at the position following this  |
|             |              | memory instruction. This is a conditional jump. Will only occur if A<B              |
|             |              | (register C=1). Must be preceded by COM instruction.                                |
+-------------+--------------+-------------------------------------------------------------------------------------+
| CONA        | C            | Loads a constant value into register A, which is the content of the memory          |
|             |              | location following the instruction.                                                 |
+-------------+--------------+-------------------------------------------------------------------------------------+
| CONB        | D            | Loads a constant value into register B, which is the content of the memory          |
|             |              | location following the instruction.                                                 |
+-------------+--------------+-------------------------------------------------------------------------------------+
| STOP        | E            | Terminate execution.                                                                |
+-------------+--------------+-------------------------------------------------------------------------------------+

INSTRUCTION SET:              
PC increments by 1 if no parameters (SUM, SUB, MUL, DIV, COM), otherwise by 2.
                              
STA  :  Stores the contents of register A in the memory location indicated by 
        the contents of the position following this instruction.
        Opcode instruction: 0 
LDA  :  Loads into register A the contents of the position whose address is   
        in the memory location following this instruction.      
        Opcode instruction: 1 
STB  :  Stores the contents of register B in the memory location indicated by 
        the contents of the position following this instruction.
        Opcode instruction: 2 
LDB  :  Loads into register B the contents of the position whose address is   
        in the memory location following this instruction.      
        Opcode instruction: 3 
STC  :  Stores the contents of register C in the memory location indicated by 
        the contents of the position following this instruction.
        Opcode instruction: 4 
SUM  :  Adds the contents of registers A and B, stores the result in register C.   
        Opcode instruction: 5 
SUB  :  Subtracts the content of register A with the value of register B and  
        stores the result in register C. Opcode instruction: 6                
MUL  :  Multiply the contents of registers A and B, stores the result in register C.
        Opcode instruction: 7                                                        
DIV  :  Divides the content of register A with the value of register B and    
        stores the result in register C, dividing by zero signals a STOP.     
        Opcode instruction: 8 
COM  :  Compares the values of registers A and B, store the result in register C.  
        If A=B, C=0; if A<B, C=1; if A>B, C=2.                                
         Opcode instruction: 9 
JMP  :  Jumps to address in the position following this instruction.
        Opcode instruction: A 
JPE  :  Jumps to address in the position following this instruction           
        if A=B (register C=0).
        Opcode instruction: B 
JPG  :  Jumps to address in the position following this instruction           
        if A>B (register C=2).
        Opcode instruction: C 
JPL  :  Jumps to address in the position following this instruction           
        if A<B (register C=1).
        Opcode instruction: D 
CONA :  Loads a constant value into register A from the memory position       
        following this instruction.                                           
        Opcode instruction: E 
CONB :  Loads a constant value into register B from the memory position       
        following this instruction.                                           
        Opcode instruction: F 
STOP :  Terminates execution. 
        Opcode instruction: 10
*/
