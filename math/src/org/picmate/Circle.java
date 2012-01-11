package org.picmate;

public final class Circle {    
  public double r;
  public double x;
  public double y;

  public Circle() {
      this.r = 0d;
      this.x = 0d;
      this.y = 0d;
  }
  
  public Circle(double r, double x, double y) {
      this.r = r;
      this.x = x;
      this.y = y;
  }
  
  public boolean equals(Circle other) {
	  return this.r == other.r && this.x == other.x && this.y == other.y;
  }
}

