package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathBotTest {

  @Test
  public void testAddition() {
    MathBot matherator9000 = new MathBot();
    double output = matherator9000.add(10.5, 3);
    assertEquals(13.5, output, 0.01);
  }

  @Test
  public void testLargerNumbers() {
    MathBot matherator9001 = new MathBot();
    double output = matherator9001.add(100000, 200303);
    assertEquals(300303, output, 0.01);
  }

  @Test
  public void testSubtraction() {
    MathBot matherator9002 = new MathBot();
    double output = matherator9002.subtract(18, 17);
    assertEquals(1, output, 0.01);
  }

  // TODO: add more unit tests of your own
  @Test
  public void testNegativeOutput() {
    MathBot mathMagician = new MathBot();
    double output = mathMagician.subtract(1,20);
    assertEquals(-19,output,0.01);
  }

  @Test
  public void testNegativeInput() {
    MathBot mathMagician2 = new MathBot();
    double output = mathMagician2.add(-1,-20);
    assertEquals(-21,output,0.01);
  }

  @Test
  public void testAddThenSub() {
    MathBot mathMagician3 = new MathBot();
    double output = mathMagician3.subtract(mathMagician3.add(-1,-20),mathMagician3.add(-21,0));
    assertEquals(0,output,0.01);
  }

  @Test
  public void testSubThenAdd() {
    MathBot mathMagician4 = new MathBot();
    double output = mathMagician4.add(mathMagician4.subtract(-1,-20),mathMagician4.subtract(-21,0));
    assertEquals(-2,output,0.01);
  }
}
