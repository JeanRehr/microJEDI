package com.microjedi;

public class UnsignedByte {
    private int value;

    public UnsignedByte(int value) {
        this.value = (int) (value & 0xFF);  // Ensure the initial value is in the range 0-255
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = (int) (value & 0xFF);  // Ensure the value is in the range 0-255
    }

    public void add(int addValue) {
        int result = (value & 0xFF) + (addValue & 0xFF);
        value = (int) (result % 256);  // Wrap around using modulo 256
    }

    public void subtract(int subValue) {
        int result = (value & 0xFF) - (subValue & 0xFF);
        value = (int) ((result + 256) % 256);  // Wrap around using modulo 256 with positive shift
    }

    @Override
    public String toString() {
        return String.format("%d", value & 0xFF);
    }
}
