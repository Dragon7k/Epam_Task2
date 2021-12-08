package by.epam.second.factory;

import by.epam.second.builder.AbstractDeviceBuilder;
import by.epam.second.builder.DeviceDOMBuilder;
import by.epam.second.builder.DeviceSAXBuilder;
import by.epam.second.builder.DeviceStAXBuilder;
import by.epam.second.exception.DeviceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeviceBuilderFactory {
    private static final Logger log = LogManager.getLogger();

    private enum ParserType {
        SAX, STAX, DOM
    }

    public AbstractDeviceBuilder createDeviceBuilder(String parserType) throws DeviceException {
        ParserType type = ParserType.valueOf(parserType.toUpperCase());
        return switch (type) {
            case DOM -> new DeviceDOMBuilder();
            case SAX -> new DeviceSAXBuilder();
            case STAX -> new DeviceStAXBuilder();
            default -> {
                log.error("Unexpected parser type {}", type);
                throw new DeviceException("Wrong parser type. Can't create builder" + type);
            }
        };
    }
}
