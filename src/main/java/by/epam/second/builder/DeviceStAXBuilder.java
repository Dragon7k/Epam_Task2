package by.epam.second.builder;

import by.epam.second.deviceType.ConnectionType;
import by.epam.second.deviceType.DeviceType;
import by.epam.second.deviceType.PortType;
import by.epam.second.entity.*;
import by.epam.second.exception.DeviceException;
import by.epam.second.handler.DeviceAttribute;
import by.epam.second.handler.DeviceEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeviceStAXBuilder extends AbstractDeviceBuilder{
    private static final Logger log = LogManager.getLogger();
    private XMLInputFactory inputFactory;
    private Map<String, DeviceEnum> tagsOfDevices;
    private Map<String, DeviceEnum> tagsOfProperties;
    private Device device;

    public DeviceStAXBuilder() {
        inputFactory = XMLInputFactory.newInstance();
        devices = new ArrayList<>();
        tagsOfDevices = new HashMap<>();
        tagsOfProperties = new HashMap<>();
        for (DeviceEnum enumValue : DeviceEnum.values()) {
            if (enumValue.ordinal() > 0 && enumValue.ordinal() < 4) {
                tagsOfDevices.put(enumValue.getTitle(), enumValue);
            } else if (enumValue.ordinal() != 0) {
                tagsOfProperties.put(enumValue.getTitle(), enumValue);
            }
        }
    }
    @Override
    public void buildDeviceList(String xmlFilePath) throws DeviceException {
        devices.clear();
        try (FileInputStream inputStream = new FileInputStream(new File(xmlFilePath))) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
            String name;
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    if (tagsOfDevices.keySet().contains(name)) {
                        buildDevice(reader);
                        devices.add(device);
                        device = null;
                    }
                }
            }
        } catch (XMLStreamException| IOException e) {
            log.error("StAX Parser error when parsing file {}", xmlFilePath, e);
            throw new DeviceException("SAX Parser error when parsing file " + xmlFilePath, e);
        }
    }


    private void buildDevice(XMLStreamReader reader) throws XMLStreamException {
        DeviceEnum tagName = tagsOfDevices.get(reader.getLocalName());
        switch (tagName) {
            case PROCESSOR -> {
                device = new Processor();
                extractAttributes(reader);
                extractCommonProperties(reader);
                extractProcessorProperties(reader);
            }
            case GRAPHICS_CARD -> {
                device = new GraphicsCard();
                extractAttributes(reader);
                extractCommonProperties(reader);
                extractMotherboardProperties(reader);
            }
            case MOUSE -> {
                device = new Mouse();
                extractAttributes(reader);
                extractCommonProperties(reader);
                extractMouseProperties(reader);
            }
            default -> {
                log.warn("Unexpected device tagName {}", tagName);
                device = new Device();
            }
        }
    }

    private void extractAttributes(XMLStreamReader reader) {
        String idName = reader.getAttributeValue(null, DeviceAttribute.ID.getTitle());
        long id = Long.parseLong(idName.substring(1));
        device.setId(id);
    }

    private void extractCommonProperties(XMLStreamReader reader) throws XMLStreamException {
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT -> {
                    name = reader.getLocalName();
                    DeviceEnum tagName = tagsOfProperties.get(name);
                    switch (tagName) {
                        case NAME -> device.setName(getXMLText(reader));
                        case BASE_INFO -> {
                            BaseInfo baseInfo = buildBaseInfo(reader);
                            device.setBaseInfo(baseInfo);
                        }
                        case DEVICE_TYPE -> {
                            String deviceType = getXMLText(reader).toUpperCase();
                            device.setType(DeviceType.valueOf(deviceType));
                        }
                        case ORIGIN -> device.setOrigin(getXMLText(reader));
                        case RELEASE -> {
                            String release = getXMLText(reader);
                            device.setRelease(YearMonth.parse(release));
                        }
                        case PRICE -> {
                            String priceValue = getXMLText(reader);
                            int price = Integer.parseInt(priceValue);
                            device.setPrice(price);
                        }
                        case IS_CRITICAL -> {
                            String isCritical = getXMLText(reader);
                            device.setCritical(Boolean.parseBoolean(isCritical));
                        }
                        default -> log.warn("Unexpected device tagName {}", tagName);
                    }

                }
                case XMLStreamConstants.END_ELEMENT -> {
                    name = reader.getLocalName();
                    if (name.equals(DeviceEnum.IS_CRITICAL.getTitle())) {
                        return;
                    }
                }
            }
        }
    }

    private void extractProcessorProperties(XMLStreamReader reader) throws XMLStreamException {
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT -> {
                    name = reader.getLocalName();
                    DeviceEnum tagName = tagsOfProperties.get(name);
                    switch (tagName) {
                        case CODE_NAME -> ((Processor) device).setCodeName(getXMLText(reader));
                        case FREQUENCY -> {
                            String frequencyValue = getXMLText(reader);
                            int frequency = Integer.parseInt(frequencyValue);
                            ((Processor) device).setFrequency(frequency);
                        }
                        case POWER -> {
                            String powerValue = getXMLText(reader);
                            int power = Integer.parseInt(powerValue);
                            ((Processor) device).setPower(power);
                        }
                        default -> log.warn("Unexpected tagName {}", tagName);
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    name = reader.getLocalName();
                    if (tagsOfDevices.containsKey(name)) {
                        return;
                    }
                }
            }
        }
    }

    private void extractMotherboardProperties(XMLStreamReader reader) throws XMLStreamException {
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT -> {
                    name = reader.getLocalName();
                    DeviceEnum tagName = tagsOfProperties.get(name);
                    switch (tagName) {
                        case SIZE -> {
                            String sizeType = getXMLText(reader);
                            ((GraphicsCard) device).setSize(Integer.parseInt(sizeType));
                        }
                        case IS_COOLER -> {
                            String isCooler = getXMLText(reader);
                            ((GraphicsCard) device).setCooler(Boolean.parseBoolean(isCooler));
                        }
                        case POWER -> {
                            String powerValue = getXMLText(reader);
                            int power = Integer.parseInt(powerValue);
                            ((GraphicsCard) device).setPower(power);
                        }
                        default -> log.warn("Unexpected tagName {}", tagName);
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    name = reader.getLocalName();
                    if (tagsOfDevices.containsKey(name)) {
                        return;
                    }
                }
            }
        }
    }

    private void extractMouseProperties(XMLStreamReader reader) throws XMLStreamException {
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT -> {
                    name = reader.getLocalName();
                    DeviceEnum tagName = tagsOfProperties.get(name);
                    switch (tagName) {
                        case PORT_TYPE -> {
                            String portType = getXMLText(reader);
                            ((Mouse) device).setConnectionInterface(PortType.valueOf(portType.toUpperCase()));
                        }
                        case CONNECTION_TYPE -> {
                            String connectionType = getXMLText(reader);
                            ((Mouse) device).setConnectionType(ConnectionType.valueOf(connectionType.toUpperCase()));
                        }
                        default -> log.warn("Unexpected tagName {}", tagName);
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    name = reader.getLocalName();
                    if (tagsOfDevices.containsKey(name)) {
                        return;
                    }
                }
            }

        }
    }

    private BaseInfo buildBaseInfo(XMLStreamReader reader) throws XMLStreamException {
        BaseInfo baseInfo = new BaseInfo();
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT -> {
                    name = reader.getLocalName();
                    DeviceEnum tagName = tagsOfProperties.get(name);
                    switch (tagName) {
                        case PRODUCER -> baseInfo.setProducer(getXMLText(reader));
                        case MODEL -> baseInfo.setModel(getXMLText(reader));
                        case SERIAL -> baseInfo.setSerial(getXMLText(reader));
                        default -> log.warn("Unexpected tagName {}", tagName);
                    }
                }
                case XMLStreamConstants.END_ELEMENT -> {
                    name = reader.getLocalName();
                    if (name.equals(DeviceEnum.BASE_INFO.getTitle())) {
                        return baseInfo;
                    }
                }
            }
        }
        return baseInfo;
    }

    private String getXMLText(XMLStreamReader reader) throws XMLStreamException {
        String text = "";
        if (reader.hasNext()) {
            reader.next();
            text = reader.getText();
        }
        return text;
    }
}
