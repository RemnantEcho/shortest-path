package shortestpath;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Node> nodeList;
    List<Edge> edgeList;
    
    public Graph() {
        nodeList = new ArrayList<Node>();
        edgeList = new ArrayList<Edge>();
    }
    
    public List<Node> getNodeList() {
        return nodeList;
    }
    
    public List<Edge> getEdgeList() {
        return edgeList;
    }
    
    public void addEdge(int sn, int en, int w) {
        for (int i = 0; i < edgeList.size(); i++) {
            Node tempSN = edgeList.get(i).getStartNode();
            Node tempEN = edgeList.get(i).getEndNode();
            if (tempSN.getLabel() == sn && tempEN.getLabel() == en) {
                edgeList.get(i).setWeight(w);
            }
        }
    }
    
    public void addEdge(Node sn, Node en, int w) {
        edgeList.add(new Edge(sn, en, w));
    }
    
    public void deleteEdge(int sn, int en) {
        for (int i = 0; i < edgeList.size(); i++) {
            Node tempSN = edgeList.get(i).getStartNode();
            Node tempEN = edgeList.get(i).getEndNode();
            if (tempSN.getLabel() == sn && tempEN.getLabel() == en) {
                edgeList.remove(i);
            }
        }
    }
}
