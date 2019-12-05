package app;

import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/CheckTriangle.oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);

        PathReader pathReader = new PathReader();
        ArrayList<Branch> branches = pathReader.read("./CheckTriangle.path");

        CheckTriangle.check(1.1, 1.2, 1.3);

        java.util.Set set = CheckTriangle.getTrace();
        System.out.println(set);
    }
}
