//Copyright by Natsuru-san

package ru.natsuru.websdr.model;

public class MemoryCell {

    private final int mode;
    private final float minBorder;
    private final float maxBorder;
    private final float freq;
    private int id;

    public MemoryCell(int mode, float minBorder, float maxBorder, float freq, int id) {
        this.mode = mode;
        this.minBorder = minBorder;
        this.maxBorder = maxBorder;
        this.freq = freq;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }
    public float getMinBorder() {
        return minBorder;
    }
    public float getMaxBorder() {
        return maxBorder;
    }
    public float getFreq() {
        return freq;
    }
    public int getId(){
        return id;
    }
}
