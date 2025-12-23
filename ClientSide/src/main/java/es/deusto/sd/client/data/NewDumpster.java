package es.deusto.sd.client.data;

public class NewDumpster {
    private String location;
    private String postalCode;
    private Integer initialCapacity;

    public NewDumpster() {}

    public NewDumpster(String location, String postalCode, Integer initialCapacity) {
        this.location = location;
        this.postalCode = postalCode;
        this.initialCapacity = initialCapacity;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public Integer getInitialCapacity() { return initialCapacity; }
    public void setInitialCapacity(Integer initialCapacity) { this.initialCapacity = initialCapacity; }
}