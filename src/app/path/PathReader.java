package app.path;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PathReader {
    public ArrayList<Branch> read(String pathFile) {
        ArrayList<Branch> ret = new ArrayList<Branch>();

        ArrayList<String> paths = new ArrayList<String>();
        try {
            File fileContainsPath = new File(pathFile);
            Scanner myReader = new Scanner(fileContainsPath);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                ArrayList<Integer> nodeData = reverse_transform(data.split(":|\\s+"));

                Branch b = arr2Branch(nodeData);
                ret.add(b);

                paths.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        paths.forEach(path -> System.out.println(path));
        ret.forEach(b -> System.out.println(b));

        return ret;
    }

    private ArrayList<Integer> reverse_transform(String[] arr) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i].equals("")) {
                continue;
            }
            ret.add(Integer.parseInt(arr[i]));
        }
        return ret;
    }

    private Branch arr2Branch(ArrayList<Integer> a) {
        Branch b = new Branch();
        for (Integer s : a) {
            Node n = new Node(s);
            b.add(n);
        }
        return b;
    }
}
