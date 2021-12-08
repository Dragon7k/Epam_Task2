package by.epam.second.entity;

import by.epam.second.deviceType.DeviceType;

import java.time.YearMonth;
import java.util.Objects;

public class Device {
    private long id;
    private String name;
    private BaseInfo baseInfo;
    private DeviceType type;
    private String origin;
    private YearMonth release;
    private int price;
    private boolean isCritical;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(BaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public YearMonth getRelease() {
        return release;
    }

    public void setRelease(YearMonth release) {
        this.release = release;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Device{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", baseInfo=").append(baseInfo);
        sb.append(", type=").append(type);
        sb.append(", origin='").append(origin).append('\'');
        sb.append(", release=").append(release);
        sb.append(", price=").append(price);
        sb.append(", isCritical=").append(isCritical);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return id == device.id && price == device.price && isCritical == device.isCritical && Objects.equals(name, device.name) && Objects.equals(baseInfo, device.baseInfo) && type == device.type && Objects.equals(origin, device.origin) && Objects.equals(release, device.release);
    }

    @Override
    public int hashCode() {
        return 31*(name.hashCode()+ baseInfo.hashCode()+type.hashCode()+origin.hashCode()+ release.hashCode()+price+32+(isCritical ? 1231 : 1237));
    }
}
