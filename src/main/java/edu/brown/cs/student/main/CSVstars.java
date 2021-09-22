package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class CSVstars {
  private ArrayList<Star> stars = new ArrayList<Star>();

  /**
   * the constructor
   */
  public CSVstars() {
  }

  /**
   * creates an ArrayList of stars based on the data in the CSV file and set it as the field
   * @param fileName
   * @return
   */
  public ArrayList<Star> fillStars(String fileName) {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.equals("StarID,ProperName,X,Y,Z")) {
          continue;
        }
        String[] values = line.split(",");
        Star newStar = new Star(Integer.parseInt(values[0]),
                values[1],
                Double.parseDouble(values[2]),
                Double.parseDouble(values[3]),
                Double.parseDouble(values[4]));
        stars.add(newStar);
      }
    } catch (Exception e) {
      System.out.println("Error! File not found!");
    }
    return stars;
  }
}
