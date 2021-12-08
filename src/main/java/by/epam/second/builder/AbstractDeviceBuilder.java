package by.epam.second.builder;

import by.epam.second.entity.Device;
import by.epam.second.exception.DeviceException;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDeviceBuilder {

    protected List<Device> devices;

    public AbstractDeviceBuilder() {
        devices = new ArrayList<>();
    }

    public List<Device> getDevices() {
        return devices;
    }

    public abstract void buildDeviceList(String filename) throws DeviceException;
}
