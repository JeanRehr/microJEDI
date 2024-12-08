package com.objmicrojedi;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        ConsoleHandler consoleHandler = new ConsoleHandler(scanner);
        // Max value size of memory is 256 because of Register class, it allows only 0-255 values
        int memSize = 64;
        CPU cpu = new CPU(memSize);

        /*
        // Example of loading an array into memory and executing
        int[] sampleProgram = {
            InstructionSet.LDA.getOpcode(), 10, // integer 10 = A
            InstructionSet.CONB.getOpcode(), 0x14,
            InstructionSet.SUM.getOpcode(),
            InstructionSet.STOP.getOpcode(), 0x00,
            0x08, 0x00, // Extra memory values are optional for testing
            0x00, 0x00, 0x00, 0x00,
        };
        cpu.loadArray(sampleProgram);
        cpu.executeProgram();
        */
        
        String[] optionsMenuItems = {
            "Load program.",
            "Execute program.",
            "Set mem value",
            "Set register.",
            "Display state.",
            "Show instructions.",
            "Save program.",
            "Help.",
            "Exit.",
            "Clear console."
        };

        while (true) {
            consoleHandler.displayMenuSide("Main menu", optionsMenuItems);
            System.out.print("Option> ");
            int userOpt = consoleHandler.getUserOption(0, optionsMenuItems.length);
            consoleHandler.getNextLine(); // Consuming \n
            if (userOpt == 9) {
                break;
            }
            handleMenuOptions(userOpt, cpu, consoleHandler);
        }

        scanner.close();
    }

    private static void handleMenuOptions(int userOpt, CPU cpu, ConsoleHandler consoleHandler) {
        switch (userOpt) {
        case 1: // Load program
            loadProgramInput(cpu, consoleHandler);
            break;
        case 2: // Execute program
            executeProgramInput(cpu, consoleHandler);
            break;
        case 3: // Set mem value
            setMemInput(cpu, consoleHandler);
            break;
        case 4: // Set Register
            setRegInput(cpu, consoleHandler);
            break;
        case 5: // Display State
            cpu.displayState();
            break;
        case 6: // Show Instructions
            displayInstructionInput(cpu, consoleHandler);
            break;
        case 7: // Save program
            saveProgram(cpu, consoleHandler);
            break;
        case 8: // Help
            showHelp(consoleHandler);
            break;
        case 9: // Exit
            break;
        case 10: // Clear Console
            consoleHandler.clearConsole();
            break;
        }
    }

    public static void loadProgramInput(CPU cpu, ConsoleHandler consoleHandler) {
        System.out.print("Program name> ");
        String name = consoleHandler.getNextLine();
        List<Integer> integers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\s+");
                for (String token : tokens) {
                    try {
                        // Try to parse each token as either decimal or hexadecimal
                        if (token.startsWith("0x") || token.startsWith("#")) {
                            integers.add(parseHexToInt(token));
                        } else {
                            integers.add(Integer.parseInt(token));
                        }
                    } catch (NumberFormatException e) {
                        // Ignore tokens that cannot be parsed as an integer
                    }
                }
            }

            if (integers.isEmpty()) {
                System.out.println("File is empty.");
                return;
            }

            cpu.loadProgram(integers);

        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found.");
        } catch (IOException ioe) {
            System.out.println("Couldn't read file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeProgramInput(CPU cpu, ConsoleHandler consoleHandler) {
        System.out.println("***** Initianting Execution *****");
        if (cpu.getPCValue() != 0) {
            System.out.printf(
                " >>> Warning! PC different from 0.\n" +
                " >>> Press Enter or 0 to execute from memory address 0.\n" +
                " >>> Press any other key to execute from current " +
                "address pointed to by PC [%X].\nOption> ", cpu.getPCValue()
            );
            String resetPC = consoleHandler.getNextLine().trim();
            if (resetPC.isEmpty() || resetPC.charAt(0) == '0') {
                cpu.resetPCValue();
            }
        }
        cpu.executeProgram();
    }

    public static void setMemInput(CPU cpu, ConsoleHandler consoleHandler) {
        String userInput;
        System.out.printf("Address (0 to %X)> ", (cpu.getMemSize() - 1));
        if (!isHexParseable(userInput = consoleHandler.getNextLine())) {
            System.out.println("Invalid hex address.");
            return;
        }

        int pos = parseHexToInt(userInput);

        if (pos > cpu.getMemSize() - 1 || pos < 0) {
            System.out.println("Invalid address");
            return;
        }

        System.out.print("Value (0 to FF)> ");
        if (!isHexParseable(userInput = consoleHandler.getNextLine())) {
            System.out.println("Invalid hex value.");
            return;
        }

        int value = parseHexToInt(userInput);
        if (value >= 0 && value <= 255) {
            cpu.setMem(pos, value);
        } else {
            System.out.println("Invalid value or address.");
        }
    }

    public static void setRegInput(CPU cpu, ConsoleHandler consoleHandler) {
        System.out.println("Press 0 to zero all registers.");
        System.out.print("Register (A, B, C, PC)> ");
        char reg = consoleHandler.getNextLine().charAt(0);

        if (reg == '0') {
            cpu.setReg('a', 0);
            cpu.setReg('b', 0);
            cpu.setReg('c', 0);
            cpu.setReg('p', 0);
            return;
        }

        System.out.print("Value (0 to FF)> ");

        String userInput;
        if (!isHexParseable(userInput = consoleHandler.getNextLine())) {
            System.out.println("Invalid hex value.");
            return;
        }

        int value = parseHexToInt(userInput);

        if (value < 0 || value > 255) {
            System.out.println("Invalid value. Value must be between 0 to FF");
            return;
        }

        switch (reg) {
        case 'a':
        case 'A':
            cpu.setReg('a', value);
            break;
        case 'b':
        case 'B':
            cpu.setReg('b', value);
            break;
        case 'c':
        case 'C':
            cpu.setReg('c', value);
            break;
        case 'p':
        case 'P':
            cpu.setReg('p', value);
            break;
        }
    }

    public static void displayInstructionInput(CPU cpu, ConsoleHandler consoleHandler) {
        System.out.print("Show up to which address? (0 to show all addresses)> ");

        String userInput;
        if (!isHexParseable(userInput = consoleHandler.getNextLine())) {
            System.out.printf("Invalid address. Address from 0 to %X", (cpu.getMemSize() - 1));
            return;
        }

        int addr = parseHexToInt(userInput);

        cpu.displayInstruction(addr);
    }

    public static void saveProgram(CPU cpu, ConsoleHandler consoleHandler) {
        System.out.print("File name? ");
        String name = consoleHandler.getNextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(name))) {
            for (int i = 0; i < cpu.getMemSize(); i++) {
                if (i % 8 == 0 && i != 0) {
                    //writer.write("\n");
                }
                writer.write("0x" + parseIntToHex(cpu.getMemValue(i)) + "\n");
            }
        } catch (IOException ioe) {
            System.out.println("Could not save memory.");
        }
    }

    public static void showHelp(ConsoleHandler consoleHandler) {
        consoleHandler.clearConsole();
        consoleHandler.showHelp1();
        System.out.print("Press any Key to continue.");
        consoleHandler.getNextLine();
        consoleHandler.showHelp2();
        System.out.print("Press any Key to continue.");
        consoleHandler.getNextLine();
        consoleHandler.showHelp3();
        System.out.print("Press any Key to continue.");
        consoleHandler.getNextLine();
    }

    public static int parseHexToInt(String hex) {
        if (hex.startsWith("0x") || hex.startsWith("0X")) {
            hex = hex.substring(2); // Remove '0x'
        }
        return Integer.parseInt(hex, 16);
    }

    public static String parseIntToHex(int value) {
        return Integer.toString(value, 16);
    }

    /* Test to see if hex values from user are able to be translated to an int */
    public static boolean isHexParseable(String hex) {
        try {
            Integer.parseInt(hex, 16);
            return true;
        } catch (final NumberFormatException e) {
            System.out.println("Not a Hexadecimal number.");
            return false;
        }
    }
}