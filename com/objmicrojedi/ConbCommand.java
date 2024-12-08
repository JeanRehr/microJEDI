package com.objmicrojedi;

public class ConbCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public ConbCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        registers.B.setValue(memory.read(registers.PC.getValue() + 1));
    }
}