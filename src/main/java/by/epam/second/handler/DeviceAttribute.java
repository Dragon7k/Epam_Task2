package by.epam.second.handler;

public enum DeviceAttribute {
    ID("id");

    private String title;
    DeviceAttribute(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;

    }
}
