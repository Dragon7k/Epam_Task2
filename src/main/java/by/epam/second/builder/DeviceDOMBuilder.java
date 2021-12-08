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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeviceDOMBuilder extends AbstractDeviceBuilder{
    private static final Logger log = LogManager.getLogger();
    private DocumentBuilder builder;
    private Map<String, DeviceEnum> tagsOfDevices;

    public DeviceDOMBuilder() throws DeviceException {
        this.devices = new ArrayList<>();
        this.tagsOfDevices = new HashMap<>();
        for (DeviceEnum deviceEnumName : DeviceEnum.values()) {
            if (deviceEnumName.ordinal() > 0 && deviceEnumName.ordinal() < 4) {
                tagsOfDevices.put(deviceEnumName.getTitle(), deviceEnumName);
            }
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("Error DOM Parser configuration", e);
            throw new DeviceException("Error DOM Parser configuration", e);
        }
    }

    public void buildDeviceList(String xmlFilePath) throws DeviceException {
        Document document = null;
        devices.clear();
        try {
            document = builder.parse(xmlFilePath);
            Element root = document.getDocumentElement();
            for (String tagName : tagsOfDevices.keySet()) {
                NodeList devicesNodesList = root.getElementsByTagName(tagName);
                for (int i = 0; i < devicesNodesList.getLength(); i++) {
                    Element deviceElement = (Element) devicesNodesList.item(i);
                    Device device = buildDevice(deviceElement);
                    devices.add(device);
                }
            }

        } catch (SAXException e) {
            log.error("SAX Parser error when parsing file {}", xmlFilePath, e);
            throw new DeviceException("SAX Parser error when parsing file " + xmlFilePath, e);
        } catch (IOException e) {
            log.error("Error reading xml file {}", xmlFilePath, e);
            e.printStackTrace();
            throw new DeviceException("Error reading xml file " + xmlFilePath, e);
        }
    }

    private Device buildDevice(Element deviceElement) {
        DeviceEnum tagName = tagsOfDevices.get(deviceElement.getTagName());
        Device device = switch (tagName) {
            case PROCESSOR -> {
                device = new Processor();

                String codeName = extractTagTextContent(deviceElement, DeviceEnum.CODE_NAME.getTitle());
                ((Processor) device).setCodeName(codeName);

                String frequencyValue = extractTagTextContent(deviceElement, DeviceEnum.FREQUENCY.getTitle());
                int frequency = Integer.parseInt(frequencyValue);
                ((Processor) device).setFrequency(frequency);

                String powerValue = extractTagTextContent(deviceElement, DeviceEnum.POWER.getTitle());
                int power = Integer.parseInt(powerValue);
                ((Processor) device).setPower(power);

                yield device;
            }
            case GRAPHICS_CARD -> {
                device = new GraphicsCard();

                String sizeValue = extractTagTextContent(deviceElement, DeviceEnum.SIZE.getTitle());
                ((GraphicsCard) device).setSize(Integer.parseInt(sizeValue));

                String isCooler = extractTagTextContent(deviceElement, DeviceEnum.IS_COOLER.getTitle());
                ((GraphicsCard) device).setCooler(Boolean.parseBoolean(isCooler));

                String powerValue = extractTagTextContent(deviceElement, DeviceEnum.POWER.getTitle());
                int power = Integer.parseInt(powerValue);
                ((GraphicsCard) device).setPower(power);

                yield device;
            }

            case MOUSE -> {
                device = new Mouse();

                String portType = extractTagTextContent(deviceElement, DeviceEnum.PORT_TYPE.getTitle());
                ((Mouse) device).setConnectionInterface(PortType.valueOf(portType.toUpperCase()));

                String connectionType = extractTagTextContent(deviceElement, DeviceEnum.CONNECTION_TYPE.getTitle());
                ((Mouse) device).setConnectionType(ConnectionType.valueOf(connectionType.toUpperCase()));

                yield device;
            }
            default -> {
                log.warn("Unexpected device tagName {}", tagName);
                yield new Device();
            }
        };
        BaseInfo baseInfo = new BaseInfo();

        String idName = deviceElement.getAttribute(DeviceAttribute.ID.getTitle());
        Long id = Long.parseLong(idName.substring(1));
        device.setId(id);

        String name = extractTagTextContent(deviceElement, DeviceEnum.NAME.getTitle());
        device.setName(name);

        NodeList nodeList = deviceElement.getElementsByTagName(DeviceEnum.BASE_INFO.getTitle());
        Element baseInfoElement = (Element) nodeList.item(0);

        String producer = extractTagTextContent(baseInfoElement, DeviceEnum.PRODUCER.getTitle());
        baseInfo.setProducer(producer);

        String model = extractTagTextContent(baseInfoElement, DeviceEnum.MODEL.getTitle());
        baseInfo.setModel(model);

        String serial = extractTagTextContent(baseInfoElement, DeviceEnum.SERIAL.getTitle());
        baseInfo.setSerial(serial);
        device.setBaseInfo(baseInfo);

        String type = extractTagTextContent(deviceElement, DeviceEnum.DEVICE_TYPE.getTitle());
        device.setType(DeviceType.valueOf(type.toUpperCase()));

        String origin = extractTagTextContent(deviceElement, DeviceEnum.ORIGIN.getTitle());
        device.setOrigin(origin);

        String release = extractTagTextContent(deviceElement, DeviceEnum.RELEASE.getTitle());
        device.setRelease(YearMonth.parse(release));

        String priceValue = extractTagTextContent(deviceElement, DeviceEnum.PRICE.getTitle());
        int price = Integer.parseInt(priceValue);
        device.setPrice(price);

        String isCritical = extractTagTextContent(deviceElement, DeviceEnum.IS_CRITICAL.getTitle());
        device.setCritical(Boolean.parseBoolean(isCritical));

        return device;
    }

    private String extractTagTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        Node node = nodeList.item(0);
        return node.getTextContent();
    }
}
