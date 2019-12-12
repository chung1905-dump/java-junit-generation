package app.path;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PathReader {
    public ArrayList<Branch> read(String pathFile) {
        ArrayList<Branch> branch = new ArrayList<Branch>();

        ArrayList<String> paths = new ArrayList<String>();

        ArrayList<String> endNodes = new ArrayList<>();
        ArrayList<String> normalNodes = new ArrayList<>();
        try {
            File fileContainsPath = new File(pathFile);
            Scanner myReader = new Scanner(fileContainsPath);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String endNode = data.replace(data.substring(data.indexOf(":")), "");
                endNodes.add(endNode);
                normalNodes.add(data.substring(data.indexOf(":") + 1));
                ArrayList<Integer> nodeData = reverse_transform(data.split(":|\\s+"));

                Branch b = arr2Branch(nodeData);
                branch.add(b);
                paths.add(data);
            }
            filterBranch(endNodes, normalNodes, branch);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        paths.forEach(System.out::println);
        branch.forEach(System.out::println);

        return branch;
    }

    private ArrayList<Integer> reverse_transform(String[] arr) {
        ArrayList<Integer> reverseTransformResult = new ArrayList<Integer>();
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i].equals("")) {
                continue;
            }
            reverseTransformResult.add(Integer.parseInt(arr[i]));
        }
        return reverseTransformResult;
    }

    private Branch arr2Branch(ArrayList<Integer> a) {
        Branch b = new Branch();
        for (Integer s : a) {
            Node n = new Node(s);
            b.add(n);
        }
        return b;
    }

    private void filterBranch(ArrayList<String> endNodes, ArrayList<String> normalNodes, ArrayList<Branch> branch) {
        for (int i = endNodes.size() - 1; i >= 0; i--) {
            for (String normalNode : normalNodes) {
                if (normalNode.contains(endNodes.get(i))) {
                    branch.remove(i);
                    break;
                }
            }
        }
    }
}
