package app;

import app.path.PathGenerator;
import app.path.PathReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/CheckTriangle.oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);

        PathReader pathReader = new PathReader();
        ArrayList<String> paths = new ArrayList<String>();
        String pathFile = "/home/ducanh/IdeaProjects/test-generation/CheckTriangle.path";
        try {
            File fileContainsPath = new File(pathFile);
            Scanner myReader = new Scanner(fileContainsPath);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                paths.add(data);
//                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        paths.forEach(path -> System.out.println(path));



    }
}
