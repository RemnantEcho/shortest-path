/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestpath;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author jly09
 */
public class Dijkstra {
    Graph g;
    List<Edge> edges;
    List<Node> nodesList;
    PriorityQueue<Node> nodeQueue;
    Set<Node> visitedNodes;
    
    long startTime;
    long endTime;
    double initialisationTime;
    double calculationTime;
            
    public Dijkstra() {

    }
    
    public int calculatePath(Graph graph, int sn, int en) {
        boolean pathExists = false;
        boolean startExists = false;
        boolean endExists = false;
        startTime = System.nanoTime();
        // Initialise variables
        g = graph;
        nodesList = g.getNodeList();
        nodeQueue = new PriorityQueue<Node>();
        Node targetNode = null;
        
        for (int i = 0; i < nodesList.size(); i++) {
            Node tempNode = nodesList.get(i);
            if (tempNode.getLabel() == sn) {
                startExists = true;
            }
            if (tempNode.getLabel() == en) {
                endExists = true;
            }
            if (tempNode.getLabel() != sn) {
                tempNode.setMinDistance(Integer.MAX_VALUE); // set min distance to max
                tempNode.setPrevious(null); // set previous node to null
            }
            else {
                tempNode.setMinDistance(0); // set min distance to 0
                tempNode.setPrevious(null); // set previous node to null
                nodeQueue.add(tempNode);    // add to queue
            }
        }
        
        endTime = System.nanoTime();
        
        initialisationTime = ((double) endTime - (double) startTime) / 1000000;  // calculate initialisation time
        System.out.println("Initialisation Time: " + initialisationTime + " milliseconds"); // output initialisation time
        
        if (startExists == false || endExists == false) {
            return -1;
        }
        
        startTime = System.nanoTime();
        
        /*
        while (!nodeQueue.isEmpty()) {
            Node u = nodeQueue.poll();
            visitedNodes.add(u);
            
            List<Node> adjNodes = 
        }
        */
        while (!nodeQueue.isEmpty()) {  // while queue is not empty
            Node u = nodeQueue.poll();  // get top priority value

            // Look through each edge of the node
            for (Edge e : u.getEdgeList())
            {
                Node v = e.getEndNode();
                if (v.getLabel() == en) {
                    pathExists = true;
                }
                
                int weight = e.getWeight();
                int alt = u.getMinDistance() + weight;
                
                if (alt < v.getMinDistance()) {     // compare min distance
                    nodeQueue.remove(u);    // remove from queue

                    v.setMinDistance(alt);  // swap min distance
                    v.setPrevious(u);   // swap previous node
                     
                    
                    nodeQueue.add(v);   // add best node to queue
                }
            }
        }
        
        
        endTime = System.nanoTime();
        calculationTime = ((double) endTime - (double) startTime) / 1000000; // calculate calculation time
        
        System.out.println("Calculation Time: " + calculationTime + " milliseconds"); // print calculation time
        
        for (int i = 0; i < nodesList.size(); i++) {
            Node tempNode = nodesList.get(i);
            if (tempNode.getLabel() == en) {
                targetNode = tempNode;  // reference to target node
            }
        }
        
        
        if (!pathExists) return -1;
        
        return targetNode.getMinDistance();   // print path distance from start to target node
    }
}
