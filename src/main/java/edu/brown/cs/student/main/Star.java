package edu.brown.cs.student.main;

public class Star {
  private int id;
  private String properName;
  private Double x;
  private Double y;
  private Double z;

  public Star(int id, String properName, Double x, Double y, Double z) {
    this.id = id;
    this.properName = properName;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  public Double calcDistance(Double x1, Double y1, Double z1) {
    return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1) + (z - z1) * (z - z1));
  }

  public String getProperName() {
    return this.properName;
  }

  public Double getX() {
    return this.x;
  }

  public Double getY() {
    return this.y;
  }

  public Double getZ() {
    return this.z;
  }
}
