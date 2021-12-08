package by.epam.second.entity;

import by.epam.second.deviceType.ConnectionType;
import by.epam.second.deviceType.PortType;

import java.util.Objects;

public class Mouse extends Device {
    private PortType connectionInterface;
    private ConnectionType connectionType;

    public PortType getConnectionInterface() {
        return connectionInterface;
    }

    public void setConnectionInterface(PortType connectionInterface) {
        this.connectionInterface = connectionInterface;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mouse mouse = (Mouse) o;
        return connectionInterface == mouse.connectionInterface && connectionType == mouse.connectionType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Mouse{");
        sb.append("connectionInterface=").append(connectionInterface);
        sb.append(", connectionType=").append(connectionType);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return 31*(connectionInterface.hashCode()+connectionType.hashCode());
    }
}
