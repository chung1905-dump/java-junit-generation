package app;

import app.path.PathGenerator;

public class Main {
    public static void main(String[] args) {
        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/CheckTriangle.oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);
    }
}
