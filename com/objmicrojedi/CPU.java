package com.objmicrojedi;

import java.util.List;

public class CPU {
    private ControlUnit controlUnit;
    private InstructionDecoderUnit idu;
    private Memory memory;
    private Registers registers;

    public CPU(int memSize) {
        this.memory = new Memory(memSize);
        this.registers = new Registers();
        this.controlUnit = new ControlUnit(this.memory, this.registers);
        this.idu = new InstructionDecoderUnit(this.memory);
    }

    public void loadProgram(List<Integer> program) {
        int size = Math.min(program.size(), this.memory.size() - 1);
        for (int i = 0; i < size; i++) {
            this.memory.write(i, program.get(i));
        }
    }

    public void executeProgram() {
        this.controlUnit.execute(this.idu);
    }

    public int getMemValue(int address) {
        return this.memory.read(address);
    }

    public void setMem(int address, int value) {
        this.memory.write(address, value);
    }

    public void setReg(char reg, int value) {
        switch (reg) {
        case 'a':
        case 'A':
            this.registers.A.setValue(value);
            break;
        case 'b':
        case 'B':
            this.registers.B.setValue(value);
            break;
        case 'c':
        case 'C':
            this.registers.C.setValue(value);
            break;
        case 'p':
        case 'P':
            this.registers.PC.setValue(value);
            break;
        }
    }

    public void displayState() {
        this.controlUnit.displayState();
    }

    public void displayInstruction(int upUntil) {
        this.controlUnit.displayInstruction(upUntil);
    }

    public int getMemSize() {
        return this.memory.size();
    }

    // Get and set PC value from cpu context
    public int getPCValue() {
        return this.registers.PC.getValue();
    }

    public void resetPCValue() {
        this.registers.PC.setValue(0);
    }

    public void loadArray(int[] array) {
        int size = Math.min(array.length, this.memory.size() - 1);
        for (int i = 0; i < size; i++) {
            this.memory.write(i, array[i]);
        }
    }
}