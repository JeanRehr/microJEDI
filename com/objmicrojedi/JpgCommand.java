package com.objmicrojedi;

public class JpgCommand implements ParameterizedCommand {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public JpgCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        int address = memory.read(registers.PC.getValue() + 1);
        if (registers.C.getValue() == 2) {
            // - 2 because as it is a parameterized command, it will be increased by 2
            registers.PC.setValue(address - 2);
        }
    }
}