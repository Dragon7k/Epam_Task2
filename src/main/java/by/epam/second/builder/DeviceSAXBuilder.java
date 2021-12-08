package by.epam.second.builder;

import by.epam.second.exception.DeviceException;
import by.epam.second.handler.DeviceHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class DeviceSAXBuilder extends AbstractDeviceBuilder{
    private static final Logger log = LogManager.getLogger();
    private DeviceHandler handler;
    private XMLReader reader;

    public DeviceSAXBuilder() throws DeviceException {
        handler = new DeviceHandler();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            reader = parser.getXMLReader();
            reader.setContentHandler(handler);
        } catch (ParserConfigurationException e) {
            log.error("Error congiguration SAX XML parser", e);
            throw new DeviceException("Error congiguration SAX XML parser", e);
        } catch (SAXException e) {
            log.error("SAX Parser error when creating SAX Builder", e);
            throw new DeviceException("SAX Parser error when creating SAX Builder", e);
        }
    }
    @Override
    public void buildDeviceList(String xmlFilePath) throws DeviceException {
        try {
            reader.parse(xmlFilePath);
        } catch (IOException e) {
            log.error("Error reading xml file {}", xmlFilePath, e);
            throw new DeviceException("Error reading xml file " + xmlFilePath, e);
        } catch (SAXException e) {
            log.error("SAX Parser error when parsing file {}", xmlFilePath, e);
            throw new DeviceException("SAX Parser error when parsing file " + xmlFilePath, e);
        }
        devices = handler.getDevices();
    }
}
