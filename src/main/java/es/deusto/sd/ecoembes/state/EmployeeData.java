package es.deusto.sd.ecoembes.state;

public class EmployeeData {
  private String employeeId;
  private String email;
  private String name;

  // Constructors
  public EmployeeData() {
  }

  public EmployeeData(String employeeId, String email, String name) {
    this.employeeId = employeeId;
    this.email = email;
    this.name = name;
  }

  // Getters and Setters
  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}