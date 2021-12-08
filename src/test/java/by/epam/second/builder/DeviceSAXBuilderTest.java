package by.epam.second.builder;

import by.epam.second.builder.AbstractDeviceBuilder;
import by.epam.second.deviceType.DeviceType;
import by.epam.second.entity.BaseInfo;
import by.epam.second.entity.Device;
import by.epam.second.entity.Processor;
import by.epam.second.exception.DeviceException;
import by.epam.second.factory.DeviceBuilderFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceSAXBuilderTest {
    private AbstractDeviceBuilder builder;


    public DeviceSAXBuilderTest() throws DeviceException {
        builder = new DeviceBuilderFactory().createDeviceBuilder("sax");

    }


    @Test
    public void testValidFile() throws DeviceException {
        String xmlFile = this.getClass().getResource("/file/devices.xml").getFile();
        builder.buildDeviceList(xmlFile);
        List<Device> actualList = builder.getDevices();
        Device processor = new Processor();
        processor.setId(1);
        processor.setName("processor1");
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setProducer("Intel");
        baseInfo.setModel("Core i5");
        baseInfo.setSerial("654682");
        processor.setBaseInfo(baseInfo);
        processor.setOrigin("Vietnam");
        processor.setPrice(50);
        processor.setRelease(YearMonth.parse("2019-01"));
        processor.setType(DeviceType.INTEGRATED);
        processor.setCritical(true);
        ((Processor) processor).setCodeName("codeName1");
        ((Processor) processor).setPower(30);
        ((Processor) processor).setFrequency(2400);
        System.out.println("processor: "+ processor);


        assertEquals(true,actualList.get(0).getBaseInfo().equals(processor.getBaseInfo()));
    }

}
