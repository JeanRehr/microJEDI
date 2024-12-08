package com.objmicrojedi;

public class SubCommand implements Command {
    private ControlUnit controlUnit;
    private Registers registers;
    // Need a Memory attr to allow it being called by the reflection at runtime
    private Memory memory;

    public SubCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        controlUnit.getALU().sub(registers);
    }
}