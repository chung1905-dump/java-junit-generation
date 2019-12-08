package app;

public class CheckTriangle {
    public static void check(double l1, double l2, double l3)
    {
        if ((l1 + l2) > l3)
        {
            if((l1 + l3) > l2)
            {
                if(l2 + l3 > l1)
                {
                    System.out.println("\nThe Triangle is a Valid Triangle\n\n");
                }
                else
                {
                    System.out.println("\nThe Triangle is Not a Valid Triangle\n\n");
                }
            }
            else
            {
                System.out.println("\nThe Triangle is Not a Valid Triangle\n\n");
            }
        }
        else
        {
            System.out.println("\nThe Triangle is Not a Valid Triangle\n\n");
        }
    }

    public static java.util.Set trace = new java.util.HashSet();


    public static void newTrace()
    {
        trace = new java.util.HashSet();
    }


    public static java.util.Set getTrace()
    {
        return trace;
    }
}
