package com.objmicrojedi;

public class JmpCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public JmpCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        int address = memory.read(registers.PC.getValue() + 1);
        // - 2 because as it is a parameterized command, it will be increased by 2
        registers.PC.setValue(address - 2);
    }
}