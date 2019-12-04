package app.path;

import java.util.ArrayList;

public class Branch {
    private ArrayList<Node> nodes = new ArrayList<Node>();

    public void add(Node node) {
        nodes.add(node);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public String toString() {
        String s = "";
        for (Node n : nodes) {
            s = s + n.getValue() + "-";
        }

        return s;
    }
}
