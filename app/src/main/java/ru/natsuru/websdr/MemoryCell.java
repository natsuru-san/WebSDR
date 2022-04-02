//Copyright by Natsuru-san

package ru.natsuru.websdr;

public class MemoryCell {
    private final int mode;
    private final double minBorder;
    private final double maxBorder;
    private final double freq;
    private final int id;
    public MemoryCell(int mode, double minBorder, double maxBorder, double freq, int id) {
        this.mode = mode;
        this.minBorder = minBorder;
        this.maxBorder = maxBorder;
        this.freq = freq;
        this.id = id;
    }
    public int getMode() {
        return mode;
    }
    public double getMinBorder() {
        return minBorder;
    }
    public double getMaxBorder() {
        return maxBorder;
    }
    public double getFreq() {
        return freq;
    }
    public int getId(){
        return id;
    }
}
