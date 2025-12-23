package es.deusto.sd.client.data;

public class Dumpster {
    private Long id;
    private String location;
    private Integer capacity;

    public Dumpster() {}

    public Dumpster(Long id, String location, Integer capacity) {
        this.id = id;
        this.location = location;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}