package com.objmicrojedi;

import java.util.Scanner;

public class ControlUnit {
    private Memory memory;
    private Registers registers;
    private ALU alu;

    public ControlUnit(Memory memory, Registers registers) {
        this.memory = memory;
        this.registers = registers;
        this.alu = new ALU();
    }

    public ALU getALU() { // Get ALU by reference.
        return this.alu;
    }

    public void execute(InstructionDecoderUnit idu) {
        Scanner scanner = new Scanner(System.in);
        ConsoleHandler consoleHandler = new ConsoleHandler(scanner);
        while (true) {
            displayState();
            int pc = registers.PC.getValue();
            int instruction = memory.read(pc);
            InstructionSet commandSet = InstructionSet.fromOpcode(instruction);

            if (commandSet == InstructionSet.STOP) {
                System.out.println("STOP: Terminating execution.\n>>> Enter any key to continue.");
                consoleHandler.getNextLine();
                break;
            } else if (pc > memory.size() - 1) {
                System.out.printf(
                    "\n >>> Instruction at PC[%X] is not valid, PC is out of bounds.\n" +
                    " >>> Terminating execution.\n" +
                    " >>> Enter any key to continue", pc
                );
                consoleHandler.getNextLine();
                break;
            } else if (commandSet == InstructionSet.INVALID) {
                System.out.printf(
                    "\n >>> Instruction at PC[%X] is not valid, instruction <%X> " +
                    "does not exist.\n >>> Terminating execution.\n" +
                    " >>> Enter any key to continue", pc, memory.read(pc)
                );
                consoleHandler.getNextLine();
                break;
            }

            System.out.print("Press Enter or any Key to continue, a to (A)bort.\n> ");

            String cont = consoleHandler.getNextLine().trim();

            if (cont.isEmpty() || Character.toLowerCase(cont.charAt(0)) != 'a') {
                try {
                    idu.decodeAndExecute(this, registers);
                } catch (ArithmeticException ae) {
                    System.out.println("STOP: Terminating execution. Division by 0 not allowed.\n>>> Press any key to continue.");
                    consoleHandler.getNextLine();
                    break;
                }
            } else {
                break;
            }
        }
    }

    public void displayState() {
        registers.displayRegisters0X();
        memory.displayMemory(registers);
        int pc = registers.PC.getValue();
        int instruction = memory.read(pc);
        InstructionSet commandSet = InstructionSet.fromOpcode(instruction);
        if (ParameterizedCommand.class.isAssignableFrom(commandSet.getCommandClass())) {
            int parameter = memory.read(pc + 1);
            if (parameter == -1) {
                System.out.printf("Next instruction: [%X]\t%s INVALID\n", pc, commandSet);
            } else {
                System.out.printf("Next instruction: [%X]\t%s %X\n", pc, commandSet, parameter);
            }
        } else {
            System.out.printf("Next instruction: [%X]\t%s\n", pc, commandSet);
        }
    }

    public void displayInstruction(int upUntil) {
        if (upUntil == 0 || upUntil >= memory.size() - 1) {
            upUntil = memory.size();
        }

        System.out.println("Address:\tInstruction:\tAddress:\tInstruction:");

        int address = 0;

        while (address < upUntil) {
            int instruction = memory.read(address);
            InstructionSet command = InstructionSet.fromOpcode(instruction);
            String commandString;

            if (command == InstructionSet.INVALID) {
                commandString = "INVALID";
            } else if (ParameterizedCommand.class.isAssignableFrom(command.getCommandClass())) {
                int parameter = memory.read(address + 1);
                if (parameter == -1) {
                    commandString = String.format("%s INVALID", command);
                } else {
                    commandString = String.format("%s %X", command, parameter);
                }
            } else {
                commandString = command.toString();
            }

            if (address % 2 == 0 || address == 0) {
                System.out.printf("[%X]:\t\t%s\t\t", address, commandString);
                if (address == upUntil) {
                    System.out.print("\n");
                }
            } else {
                System.out.printf("[%X]:\t\t%s\n", address, commandString);
            }

            address++;
        }
    }
}