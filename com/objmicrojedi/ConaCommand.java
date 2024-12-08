package com.objmicrojedi;

public class ConaCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public ConaCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        registers.A.setValue(memory.read(registers.PC.getValue() + 1));
    }
}