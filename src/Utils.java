/**
 * File containing single function to convert degrees to radians.
 *
 * @author Aditya B
 * @date 25 January 2023
 */

import java.lang.Math;
public class Utils {
    // Math function to convert degrees to radians
    public static double degreeToRads(double degree) {
        return degree * Math.PI / 180;
    }
}
