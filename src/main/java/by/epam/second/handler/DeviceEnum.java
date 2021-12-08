package by.epam.second.handler;

public enum DeviceEnum {

    DEVICES("Devices"),
    PROCESSOR("Processor"),
    GRAPHICS_CARD("GraphicsCard"),
    MOUSE("Mouse"),
    NAME("name"),
    BASE_INFO("baseInfo"),
    PRODUCER("producer"),
    MODEL("model"),
    SERIAL("serial"),
    DEVICE_TYPE("deviceType"),
    ORIGIN("origin"),
    RELEASE("release"),
    PRICE("price"),
    IS_CRITICAL("isCritical"),
    CODE_NAME("codeName"),
    FREQUENCY("frequency"),
    POWER("power"),
    SIZE("size"),
    ID("Id"),
    IS_COOLER("isCooler"),
    PORT_TYPE("portType"),
    CONNECTION_TYPE("connectionType");
    private String title;

    DeviceEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;

    }
}
