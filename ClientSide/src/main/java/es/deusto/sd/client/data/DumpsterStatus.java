package es.deusto.sd.client.data;

public class DumpsterStatus {
    private Long id;
    private String location;
    private String fillLevel; // "GREEN", "ORANGE", "RED"

    public DumpsterStatus() {}

    public DumpsterStatus(Long id, String location, String fillLevel) {
        this.id = id;
        this.location = location;
        this.fillLevel = fillLevel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getFillLevel() { return fillLevel; }
    public void setFillLevel(String fillLevel) { this.fillLevel = fillLevel; }
}