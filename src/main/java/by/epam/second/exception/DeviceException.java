package by.epam.second.exception;

public class DeviceException extends Exception{
    public DeviceException() {
    }

    public DeviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceException(String message) {
        super(message);
    }
}
