package app.path;

import java.util.ArrayList;

public class Branch {
    private ArrayList<Node> nodes;

    public void add(Node node) {
        nodes.add(node);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
}
