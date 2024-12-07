package com.objmicrojedi;

public class StopCommand implements Command {
    private ControlUnit controlUnit;
    private Registers registers;
    private Memory memory;

    public StopCommand(ControlUnit controlUnit, Registers registers, Memory memory) {
        this.controlUnit = controlUnit;
        this.registers = registers;
        this.memory = memory;
    }

    @Override
    public void execute() {
        // The STOP command would signal the system to halt execution.
        System.out.println("STOP command executed. Halting the program.");
    }
}