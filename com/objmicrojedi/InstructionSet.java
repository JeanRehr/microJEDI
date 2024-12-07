package com.objmicrojedi;

public enum InstructionSet {
    STA(0, StaCommand.class),
    LDA(1, LdaCommand.class),
    STB(2, StbCommand.class),
    LDB(3, LdbCommand.class),
    STC(4, StcCommand.class),
    SUM(5, SumCommand.class),
    SUB(6, SubCommand.class),
    MUL(7, MulCommand.class),
    DIV(8, DivCommand.class),
    COM(9, ComCommand.class),
    JMP(10, JmpCommand.class), // A
    JPE(11, JpeCommand.class), // B
    JPG(12, JpgCommand.class), // C
    JPL(13, JplCommand.class), // D
    CONA(14, ConaCommand.class), // E
    CONB(15, ConbCommand.class), // F
    STOP(16, StopCommand.class), // 10
    INVALID(-1, InvalidCommand.class);

    private final int opcode;
    private final Class<? extends Command> commandClass;

    private InstructionSet(int opcode, Class<? extends Command> commandClass) {
        this.opcode = opcode;
        this.commandClass = commandClass;
    }

    public int getOpcode() {
        return opcode;
    }

    public Class<? extends Command> getCommandClass() {
        return commandClass;
    }

    public static InstructionSet fromOpcode(int opcode) {
        for (InstructionSet instr : values()) {
            if (instr.getOpcode() == opcode) {
                return instr;
            }
        }
        return INVALID;
    }

    @Override
    public String toString() {
        return name();
    }
}