package com.objmicrojedi;

public class Registers {
    public Register A;
    public Register B;
    public Register C;
    public Register PC;

    public Registers() {
        this.A = new Register(0);
        this.B = new Register(0);
        this.C = new Register(0);
        this.PC = new Register(0);
    }

    public void displayRegisters0X() {
        System.out.printf(
            "Registers:\tA:%X\tB:%X\tC:%X\t[PC:%X]\n",
            this.A.getValue(), this.B.getValue(), this.C.getValue(), this.PC.getValue()
        );
    }
}