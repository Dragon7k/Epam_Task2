package by.epam.second.handler;

import by.epam.second.deviceType.ConnectionType;
import by.epam.second.deviceType.DeviceType;
import by.epam.second.deviceType.PortType;
import by.epam.second.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceHandler extends DefaultHandler {

    private static final Logger log = LogManager.getLogger();
    private List<Device> devices;
    private Device currentDevice;
    private BaseInfo currentBaseInfo;
    private DeviceEnum currentTag;
    private Map<String, DeviceEnum> tagsOfProperties;
    private Map<String, DeviceEnum> tagsOfDevices;
    private Map<String, DeviceAttribute> deviceAttributes;

    public DeviceHandler() {
        devices = new ArrayList<>();
        tagsOfProperties = new HashMap<>();
        tagsOfDevices = new HashMap<>();
        deviceAttributes = new HashMap<>();
        for (DeviceEnum enumValue : DeviceEnum.values()) {
            if (enumValue.ordinal() > 0 && enumValue.ordinal() < 4) {
                tagsOfDevices.put(enumValue.getTitle(), enumValue);
            } else if (enumValue.ordinal() != 0) {
                tagsOfProperties.put(enumValue.getTitle(), enumValue);
            }
        }

    }
    public List<Device> getDevices() {
        return devices;
    }

    @Override
    public void startDocument() throws SAXException {
        devices.clear();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (tagsOfDevices.containsKey(qName)) {
            currentDevice = switch (tagsOfDevices.get(qName)) {
                case PROCESSOR -> new Processor();
                case GRAPHICS_CARD -> new GraphicsCard();
                case MOUSE -> new Mouse();
                default -> new Device();
            };
            for (int i = 0; i < attributes.getLength(); i++) {
                String attributeName = attributes.getQName(i);
                if (deviceAttributes.get(attributeName) == DeviceAttribute.ID) {
                    String attributeValue = attributes.getValue(i).substring(1);
                    long id = Long.parseLong(attributeValue);
                    currentDevice.setId(id);
                }
            }
        } else if (tagsOfProperties.containsKey(qName)) {
            if (qName.equals(DeviceEnum.BASE_INFO.getTitle())) {
                currentBaseInfo = new BaseInfo();
            } else {
                currentTag = tagsOfProperties.get(qName);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (tagsOfDevices.containsKey(qName)) {
            devices.add(currentDevice);
            currentDevice = null;
        } else if (qName.equals(DeviceEnum.BASE_INFO.getTitle())) {
            currentDevice.setBaseInfo(currentBaseInfo);
            currentBaseInfo = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String propertyValue = new String(ch, start, length);
        if (currentTag != null) {
            switch (currentTag) {
                case NAME -> currentDevice.setName(propertyValue);
                case PRODUCER -> currentBaseInfo.setProducer(propertyValue);
                case MODEL -> currentBaseInfo.setModel(propertyValue);
                case SERIAL -> currentBaseInfo.setSerial(propertyValue);
                case DEVICE_TYPE -> currentDevice.setType(DeviceType.valueOf(propertyValue.toUpperCase()));
                case ORIGIN -> currentDevice.setOrigin(propertyValue);
                case RELEASE -> currentDevice.setRelease(YearMonth.parse(propertyValue));
                case PRICE -> currentDevice.setPrice(Integer.parseInt(propertyValue));
                case IS_CRITICAL -> currentDevice.setCritical(Boolean.parseBoolean(propertyValue));
                case CODE_NAME -> ((Processor) currentDevice).setCodeName(propertyValue);
                case FREQUENCY -> ((Processor) currentDevice).setFrequency(Integer.parseInt(propertyValue));
                case POWER -> {
                    if (currentDevice instanceof Processor ) {
                        ((Processor) currentDevice).setPower(Integer.parseInt(propertyValue));
                    } else {
                        ((GraphicsCard) currentDevice).setPower(Integer.parseInt(propertyValue));
                    }
                }
                case SIZE -> ((GraphicsCard) currentDevice).setSize(Integer.parseInt(propertyValue));
                case IS_COOLER -> ((GraphicsCard) currentDevice).setCooler(Boolean.parseBoolean(propertyValue));
                case PORT_TYPE -> ((Mouse) currentDevice)
                        .setConnectionInterface(PortType.valueOf(propertyValue.toUpperCase()));
                case CONNECTION_TYPE -> ((Mouse) currentDevice)
                        .setConnectionType(ConnectionType.valueOf(propertyValue.toUpperCase()));
                default -> log.warn("Unexpected tagname apear: {}", currentTag);
            }
            currentTag = null;
        }
    }
}
