package com.objmicrojedi;

import java.lang.reflect.InvocationTargetException;

public class InstructionDecoderUnit {
    private Memory memory;

    public InstructionDecoderUnit(Memory memory) {
        this.memory = memory;
    }

    public void decodeAndExecute(ControlUnit controlUnit, Registers registers) throws ArithmeticException {
        int pc = registers.PC.getValue();
        int opcode = memory.read(pc);
        InstructionSet instruction = InstructionSet.fromOpcode(opcode);

        try {
            // Create an instance of the command class using reflection
            Command command = instruction.getCommandClass()
                .getDeclaredConstructor(ControlUnit.class, Registers.class, Memory.class)
                .newInstance(controlUnit, registers, memory);

            command.execute();

            if (command instanceof ParameterizedCommand) {
                registers.PC.setValue(registers.PC.getValue() + 2);
            } else {
                registers.PC.setValue(registers.PC.getValue() + 1);
            }
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
    }
}