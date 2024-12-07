package com.objmicrojedi;

public class StcCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public StcCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        int address = memory.read(registers.PC.getValue() + 1);
        memory.write(address, registers.C.getValue());
    }
}