package shortestpath;

public class Edge {
    Node startNode;
    Node endNode;
    Integer weight;
    
    public Edge(Node sn, Node en, Integer w) {
        startNode = sn;
        endNode = en;
        weight = w;
    }
    
    public void setWeight(Integer w) {
        weight = w;
    }
    
    public void setStartNode(Node n) {
        startNode = n;
    }
    
    public void setEndNode(Node n) {
        endNode = n;
    }
    
    public Integer getWeight() {
        return weight;
    }
    
    public Node getStartNode() {
        return startNode;
    }
    
    public Node getEndNode() {
        return endNode;
    }
}
