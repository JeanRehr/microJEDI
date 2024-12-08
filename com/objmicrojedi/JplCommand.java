package com.objmicrojedi;

public class JplCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public JplCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        int address = memory.read(registers.PC.getValue() + 1);
        if (registers.C.getValue() == 1) {
            // - 2 because as it is a parameterized command, it will be increased by 2
            registers.PC.setValue(address - 2);
        }
    }
}