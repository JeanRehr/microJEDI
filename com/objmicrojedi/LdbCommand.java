package com.objmicrojedi;

public class LdbCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public LdbCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        int value = memory.read(registers.PC.getValue() + 1);
        registers.B.setValue(value);
    }
}