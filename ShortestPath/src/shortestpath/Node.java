package shortestpath;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable{
    int label;
    int minDistance;
    Node previous;
    List<Edge> edgeList;
    
    Node(int n) {
        label = n;
        edgeList = new ArrayList<Edge>();
    }
    
    Node(int n, List<Edge> cn) {
        label = n;
        edgeList = cn; 
    }
    
    public void setLabel(int n) {
       label = n; 
    }
    
    public int getLabel() {
        return label;
    }
    
    public void setEdgeList(List<Edge> cn) {
        edgeList = cn;
    }
    
    public List<Edge> getEdgeList() {
        return edgeList;
    }
    
    public void setMinDistance(int md) {
        minDistance = md;
    }
    
    public int getMinDistance() {
        return minDistance;
    }
    
    public void setPrevious(Node n) {
        previous = n;
    }
    
    public Node getPrevious() {
        return previous;
    }
    
    public void addEdge(Node en, int w) {
        Edge edg = new Edge(this, en, w);
        for (int i = 0; i < edgeList.size(); i++) {
            if (edgeList.get(i).getEndNode() == en) {
                edgeList.get(i).setWeight(w);
                return;
            }
        }
        
        edgeList.add(edg);
    }
    
    public int findPath(int en) {
        for(int i = 0; i < edgeList.size(); i++) {
            if (en == edgeList.get(i).getEndNode().getLabel()) return i;
        }
        
        return -1;
    }
    
    @Override
    public int compareTo(Object o) {
        Node tempNode = (Node) o;
        return Integer.compare(minDistance, tempNode.getMinDistance());
    }
    
    public List<Edge> extractMinimum() {
        
        for (int i = 1; i < edgeList.size(); i++) {
            Edge e1 = edgeList.get(i);
            int w1 = e1.getWeight();
            for (int j = i - 1; j < edgeList.size() && edgeList.get(j).getWeight() > w1; j++) {
                
            }
            
        }
        return null;
    }
    
    public void sort() 
    {
        List<Edge> tempEL = new ArrayList<Edge>(edgeList);
        if (tempEL.size() < 1) return;
        
        for (int i = 1; i < tempEL.size(); ++i) { 
            Edge e1 = tempEL.get(i); 
            int w1 = e1.getWeight();
            int j = i - 1; 
  
            while (j >= 0 && tempEL.get(j).getWeight() > w1) { 
                int newJ = j + 1;
                tempEL.set(newJ, e1); 
                j = j - 1; 
            } 
            int j2 = j + 1;
            tempEL.set(j2, e1); 
        } 
    } 
    
    /*
    public void mergeSort(List<Edge> e, int len) {
        if (len < 2) return;
        
        int mid = len / 2;
        
        List<Edge> head = e.subList(0, mid);
        List<Edge> tail = e.subList(mid, len);
        
        for (int i = 0; i < len; i++) {
            if (i < mid) {
                head
            }
        }
    }
    */
}
