package app;

public class MinMax {
    public static void minMaxFunction(double a, double b, double c) {
        Double max = null;
        Double min = null;

        if (a > b) {
            max = a;
            min = b;
        } else {
            max = b;
            min = a;
        }

        if (max < c) {
            max = c;
        }

        if (max > c) {
            min = c;
        }

    }
}
