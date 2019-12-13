package app;

public class CheckTriangle {
    public static void check(double l1, double l2, double l3) {
        if ((l1 + l2) > l3) {
            if ((l1 + l3) > l2) {
                if (l2 + l3 > l1) {
                    System.out.println("The Triangle is a Valid Triangle");
                } else {
                    System.out.println("The Triangle is Not a Valid Triangle");
                }
            } else {
                System.out.println("The Triangle is Not a Valid Triangle");
            }
        } else {
            System.out.println("The Triangle is Not a Valid Triangle");
        }
    }

    public static void anotherCheck(int a, int b, int c, int d) {
        if (a > 20) {
            if (b > 5) {
                if (c < 30) {
                    if (d < 2) {
                        String x = "good varible";
                    } else {
                        String x = "d less than 5";
                    }
                } else {
                    String x = "C less than 30";
                }
            } else {
                String x = "b less than 5";
            }
        } else {
            String x = "a is big";
        }
    }
}
