package app;

import java.util.*;

public class CheckTriangle {
    public static void check(double l1, double l2, double l3)
    {
        Scanner sc = new Scanner(System.in);
        l1 = sc.nextFloat();
        l2 = sc.nextFloat();
        l3 = sc.nextFloat();
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
    }}
