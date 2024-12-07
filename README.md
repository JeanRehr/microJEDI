
# microJEDI
A school project, emulates a simple cpu.

The version0 is not working as intended, keeping it for future reference.
Version1 is working but it's a mess, not maintainable.

The new version in com/objmicrojedi directory is working as intended and more maintainable, has support for multiplication and division.

To compile, go to the directory where ./com directory is located, then run:
```
$ javac com/objmicrojedi/*.java
```

To execute the program, run:
```
$ java com/objmicrojedi/Main
```

Translated text:
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
1. are 8 bits
2. represent unsigned integer values ​​(0 to 255)
3. A and B serve as input parameters for the ALU
4. C is used to store the result of the operations performed by the ALU
5. The contents of the PC (Program Counter) register represent the address of the next instruction
to be executed by the CPU.
6. After executing an instruction, the contents of the PC register are changed, being incremented
by 2 or 1, depending on the last instruction executed; if the last instruction had no parameter
(as in the cases of SUM, SUB, COM, for example), the increment is 1, otherwise it is 2.

PC = program counter, address of the next instruction to be executed

ALU - Arithmetic Logic Unit:
1. performs the operations of:
    a. addition: when the SUM instruction is executed; it adds the contents of registers A and B
    and stores the result of the operation in register C; if the result of the addition exceeds
    255, an OVERFLOW event occurs, and the result stored in C will be (A+B) -256.
    b. subtraction: when the SUB instruction is executed; it performs the operation A - B and stores
    the result of the operation in register C; if the result of the subtraction is less than
    0 (zero), an UNDERFLOW event occurs, and the result stored in C will be (A-B) + 256.
    c. comparison: when the COM instruction is executed; it compares the values ​​of registers A and B;
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
1. load program into memory: allows the user to manually load data and instructions into memory
    or to load data and instructions from a file on disk;
2. execute program: activates the instruction search and execution cycle; this cycle begins at
    the memory position whose address corresponds to the value of the PC register;
    when the STOP instruction is executed, this cycle is deactivated;
3. change memory position: allows the user to change the contents of a specific memory position;
4. change register: allows the user to change the contents of a specific register;
5. show memory and registers: displays the contents of all registers as well as the matrix that
    represents the architecture's memory;
6. show instructions: shows the contents of memory, but presents the user with the name of the
    instructions stored in each memory position;
7. save program to file: allows the contents of memory to be saved (recorded) in a file on disk;
8. help: presents a summary of the instructions supported by the CPU;
9. exit: ends the execution of the program.

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
|             |              | by the contents of the position following this instruction (mem[pc+1]=a).           |
+-------------+--------------+-------------------------------------------------------------------------------------+
| LDA         | 1            | Loads into register A the contents of the position whose address is in              |
|             |              | the memory location following this instruction (a=mem[pc+1]).                       |
+-------------+--------------+-------------------------------------------------------------------------------------+
| STB         | 2            | Stores the contents of register B in the memory location indicated                  |
|             |              | by the contents of the position following this instruction (mem[pc+1]=b).           |
+-------------+--------------+-------------------------------------------------------------------------------------+
| LDB         | 3            | Loads into register B the contents of the position whose address is in              |
|             |              | the memory location following this instruction (b=mem[pc+1]).                       |
+-------------+--------------+-------------------------------------------------------------------------------------+
| STC         | 4            | Stores the contents of register C in the memory location indicated                  |
|             |              | by the contents of the position following this instruction (c=mem[pc+1]).           |
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
|             |              | (register C=0). Must be preceded by COM instruction (pc=mem[pc+1]).                 |
+-------------+--------------+-------------------------------------------------------------------------------------+
| JPG         | A            | Diverts the execution flow to the address contained at the position following this  |
|             |              | memory instruction. This is a conditional jump. Will only occur if A>B              |
|             |              | (register C=2). Must be preceded by COM instruction (pc=mem[pc+1]).                 |
+-------------+--------------+-------------------------------------------------------------------------------------+
| JPL         | B            | Diverts the execution flow to the address contained at the position following this  |
|             |              | memory instruction. This is a conditional jump. Will only occur if A<B              |
|             |              | (register C=1). Must be preceded by COM instruction (pc=mem[pc+1]).                 |
+-------------+--------------+-------------------------------------------------------------------------------------+
| CONA        | C            | Loads a constant value into register A, which is the content of the memory          |
|             |              | location following the instruction (mem[pc+1]=a) (a turns to const).                |
+-------------+--------------+-------------------------------------------------------------------------------------+
| CONB        | D            | Loads a constant value into register B, which is the content of the memory          |
|             |              | location following the instruction (mem[pc+1]=b) (b turns to const).                |
+-------------+--------------+-------------------------------------------------------------------------------------+
| STOP        | E            | Terminate execution.                                                                |
+-------------+--------------+-------------------------------------------------------------------------------------+

![cupj-1](https://user-images.githubusercontent.com/49696706/120048850-ad90c600-bfee-11eb-9cec-6678d16fcf50.png)
![cupj-2](https://user-images.githubusercontent.com/49696706/120048871-c4371d00-bfee-11eb-9ccb-94efcfb282bc.png)
![cupj-3](https://user-images.githubusercontent.com/49696706/120048888-c8633a80-bfee-11eb-8b82-5dcc107c4c01.png)
![cupj-4](https://user-images.githubusercontent.com/49696706/120048890-c9946780-bfee-11eb-8afb-2254afbe0abe.png)//
