package by.epam.second.validator;

import by.epam.second.validator.impl.XmlValidatorImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class XmlValidatorTest {
    @Test
    public void testValidate() {
        XmlValidator validator = new XmlValidatorImpl();
        boolean isRead = validator.validate("src\\main\\resources\\file\\fund.xml");
        Assert.assertTrue(isRead);
    }
}
