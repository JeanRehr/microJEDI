package com.objmicrojedi;

public class LdaCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public LdaCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        int value = memory.read(registers.PC.getValue() + 1);
        registers.A.setValue(value);
    }
}