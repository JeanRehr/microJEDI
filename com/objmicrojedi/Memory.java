package com.objmicrojedi;

public class Memory {
    private int[] data;

    public Memory(int size) {
        this.data = new int[size];
    }

    public int read(int address) {
        if (isValidAddr(address)) {
            return this.data[address];
        }
        return -1; // Return invalid opcode if address is out of range
    }

    public void write(int address, int value) {
        if (isValidAddr(address)) {
            this.data[address] = value;
        }
    }

    public boolean isValidAddr(int address) {
        return address >= 0 && address < this.data.length;
    }
    
    public int size() {
        return this.data.length;
    }

    public void displayMemory(Registers registers) {
        System.out.println(
            "Memory: ----------------------------------------------------------------+\n" +
            "\t00\t01\t02\t03\t04\t05\t06\t07\t|\n\t\t\t\t\t\t\t\t\t|"
        );

        int count = 0;
        for (int i = 0; i < this.data.length; i++) { /* Memory values */
            if (i == 0 || i % 8 == 0) { /* Right numbers */
                System.out.printf("%02X:", i);
            }

            if (registers.PC.getValue() == i) {
                System.out.printf("\t<%X>", read(i));
            } else {
                System.out.printf("\t%X", read(i));
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
    }
}