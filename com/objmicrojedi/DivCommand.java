package com.objmicrojedi;

public class DivCommand implements Command {
    private ControlUnit controlUnit;
    private Registers registers;
    // Need a Memory attr to allow all the commands being called by the same interface using reflection 
    private Memory memory;

    public DivCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() throws ArithmeticException {
        controlUnit.getALU().div(registers);
    }
}