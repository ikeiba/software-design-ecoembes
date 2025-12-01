package es.deusto.sd.plas.Entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "assignments")
public class Assignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDate date;

  private int nDumpster;

  private int nContainer;

  private String token;

  public Assignment() {
  }

  public Assignment(LocalDate date, int nDumpster, int nContainer, String token) {
    this.date = date;
    this.nDumpster = nDumpster;
    this.nContainer = nContainer;
    this.token = token;
  }

  public Long getId() {
    return id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public int getnDumpster() {
    return nDumpster;
  }

  public void setnDumpster(int nDumpster) {
    this.nDumpster = nDumpster;
  }

  public int getnContainer() {
    return nContainer;
  }

  public void setnContainer(int nContainer) {
    this.nContainer = nContainer;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
