package com.objmicrojedi;

public class InvalidCommand implements Command {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public InvalidCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        System.out.println("INVALID OPCODE ENCOUNTERED AT INVALIDCOMMAND CLASS");
    }
}