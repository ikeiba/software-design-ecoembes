package es.deusto.sd.ecoembes.dto;

public class TokenDTO {
  private String token;

  // Constructors
  public TokenDTO() {
  }

  public TokenDTO(String token) {
    this.token = token;
  }

  // Getter and Setter
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}