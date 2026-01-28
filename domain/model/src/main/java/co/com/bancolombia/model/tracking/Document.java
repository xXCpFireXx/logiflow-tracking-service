package co.com.bancolombia.model.tracking;

public class Document {
    private final String name;
    private final String format;
    private final String size;

    public Document(String name, String format, String size) {
        this.name = name;
        this.format = format;
        this.size = size;
    }

    public String getName() { return name; }
    public String getFormat() { return format; }
    public String getSize() { return size; }
}