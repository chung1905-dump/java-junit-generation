package app;

public class CheckTriangle {
    public static void check(double l1, double l2, double l3) {
        if ((l1 + l2) > l3) {
            if ((l1 + l3) > l2) {
                if (l2 + l3 > l1) {
                    boolean isTriangle = true;
                    System.out.println("\nThe Triangle is a Valid Triangle\n\n");
                } else {
                    boolean isTriangle = false;
                    System.out.println("\nThe Triangle is Not a Valid Triangle\n\n");
                }
            } else {
                boolean isTriangle = false;
                System.out.println("\nThe Triangle is Not a Valid Triangle\n\n");
            }
        } else {
            boolean isTriangle = false;
            System.out.println("\nThe Triangle is Not a Valid Triangle\n\n");
        }
    }

    public static void anotherCheck(double a, double b, double c, double d) {

        if (a > 20) {
            if (b > 5) {
                if (c < 30) {
                    if (d > 5) {
                        String x = "good varible";
                        System.out.println(x);
                    } else {
                        String x = "d less than 5";
                        System.out.println(x);
                    }
                } else {
                    String x = "C less than 30";
                    System.out.println(x);
                }
            } else {
                String x = "b less than 5";
                System.out.println(x);
            }
        } else {
            String x = "a is big";
            System.out.println(x);
        }
    }

    public static java.util.Set trace = new java.util.HashSet();


    public static void newTrace() {
        trace = new java.util.HashSet();
    }


    public static java.util.Set getTrace() {
        return trace;
    }
}
