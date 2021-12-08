package by.epam.second.entity;

public class Processor extends Device {
    private String codeName;
    private int frequency;
    private int power;

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Processor other = (Processor) obj;
        if (codeName == null) {
            if (other.codeName != null)
                return false;
        } else if (!codeName.equals(other.codeName))
            return false;
        if (frequency != other.frequency)
            return false;
        if (power != other.power)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Processor [");
        stringBuilder.append(super.toString());
        stringBuilder.append(", codeName=");
        stringBuilder.append(codeName);
        stringBuilder.append(", frequency=");
        stringBuilder.append(frequency);
        stringBuilder.append(", power=");
        stringBuilder.append(power);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
