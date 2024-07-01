package com.example.lab9;

public class Coin {
    private int denomination;
    private boolean isReal;

    public Coin(int denomination, boolean isReal) {
        this.denomination = denomination;
        this.isReal = isReal;
    }

    public int getDenomination() {
        return denomination;
    }

    public boolean isReal() {
        return isReal;
    }

    public int getValue() {
        return isReal ? denomination : 0;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public void setReal(boolean isReal) {
        this.isReal = isReal;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "denomination=" + denomination +
                ", isReal=" + isReal +
                ", value=" + getValue() +
                '}';
    }
}
