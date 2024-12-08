package com.objmicrojedi;

public class ALU {
    public void sum(Registers registers) {
        Register addedValue = new Register(registers.A.getValue());
        addedValue.add(registers.B.getValue());
        registers.C.setValue(addedValue.getValue());
    }
    
    public void sub(Registers registers) {
        Register subValue = new Register(registers.A.getValue());
        subValue.subtract(registers.B.getValue());
        registers.C.setValue(subValue.getValue());
    }

    public void mul(Registers registers) {
        Register mulValue = new Register(registers.A.getValue());
        mulValue.multiply(registers.B.getValue());
        registers.C.setValue(mulValue.getValue());
    }

    public void div(Registers registers) throws ArithmeticException {
        Register divValue = new Register(registers.A.getValue());
        divValue.divide(registers.B.getValue());
        registers.C.setValue(divValue.getValue());
    }

    public void com(Registers registers) {
        if (registers.A.getValue() == registers.B.getValue()) {
            registers.C.setValue(0);
        } else if (registers.A.getValue() < registers.B.getValue()) {
            registers.C.setValue(1);
        } else {
            registers.C.setValue(2);
        }
    }
}