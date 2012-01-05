package org.picmate;

import java.lang.Math;


public final class Calc {

public static double EPSILON = 1E-4;
/** 
 * Given 3 points, it calculates radius of circle determined by these points
 * 
 * @returns Circle
 * 
 * If points lies on the line, there is no Circle leading through these points,
 * therefore the unvalid (0,0,0) is returned.
 * 
 */
public static final Circle radiusFun(
    double x1, double y1, 
    double x2, double y2, 
    double x3, double y3) {


    
    double r = 0;
    double cp_x = 0;
    double cp_y = 0;


    // Compute centers
    double c_ax = (x1 + x2)/2;
    double c_ay = (y1 + y2)/2;
    double c_bx = (x2 + x3)/2;
    double c_by = (y2 + y3)/2;
    double c_x = c_bx - c_ax;
    double c_y = c_by - c_ay;

    // Compute directions
    double nr_a = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1,2));
    double nr_b = Math.sqrt(Math.pow(x3-x2, 2) + Math.pow(y3-y2,2));

    if (Math.abs(nr_a)< EPSILON && Math.abs(nr_b)<EPSILON) {
    	return new Circle();
    }
    
    double s_ax = (y1 - y2)/nr_a;
    double s_ay = (x2 - x1)/nr_a;
    double s_bx = (y2 - y3)/nr_b;
    double s_by = (x3 - x2)/nr_b;

    //Compute intersection and radius;
    double detSys = s_ax*s_by - s_ay*s_bx;
    
    if (Math.abs(detSys) > EPSILON) {
        double u = (c_x*s_by - c_y*s_bx)/detSys;
        double v = (c_x*s_ay - c_y*s_ax)/detSys;
        if ((u>0 && v>0) || (u<0 && v<0)) {
            cp_x = c_ax + u*s_ax;
            cp_y = c_ay + u*s_ay;
            r = Math.sqrt(Math.pow(cp_x-x2,2) + Math.pow((cp_y-y2),2));
        }
    }

    return new Circle(r, cp_x, cp_y);
}

}


