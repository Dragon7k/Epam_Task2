package by.epam.second.entity;

import java.util.Objects;

public class GraphicsCard extends Device{
    private int size;
    private int power;
    private boolean isCooler;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public boolean isCooler() {
        return isCooler;
    }

    public void setCooler(boolean cooler) {
        isCooler = cooler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphicsCard that = (GraphicsCard) o;
        return size == that.size && power == that.power && isCooler == that.isCooler;
    }

    @Override
    public int hashCode() {
        return 31*(size+power+(isCooler ? 1231 : 1237));
    }
}
