/**
 * Node class
 * Written by: Daniel Vandolph 2020-02-05
 * A Master Thesis Project in Artificial Intelligence @ Umeå University
 * January 2020 - June 2020
 */

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.Math.abs;

public class Node implements Comparable<Node>{

    // Attributes
    private final Set<Edge> outEdges, inEdges;
    private final Point location;
    private final Dimension size;
    private final String type;
    private double g, f, h;
    private Node parent, child;
    private Edge parentEdge, childEdge;
    private double parentEdgeLength, childEdgeLength, origoDistance;
    private boolean visited = false;

    /**
     * Main Node object constructor
     * @param outEdges
     * @param inEdges
     * @param location
     * @param size
     * @param type
     */
    public Node(Set<Edge> outEdges, Set<Edge> inEdges, Point location, Dimension size, String type) {
        this.outEdges = Set.copyOf(outEdges);
        this.inEdges = Set.copyOf(inEdges);
        this.location = location;
        this.size = size;
        this.type = type;
    }

    /**
     * Helper constructor to be used when returning trivial node objects
     */
    public Node(){
        this.outEdges = null;
        this.inEdges = null;
        this.location = null;
        this.size = null;
        this.type = "";
        this.parent = null;
    }

    /**
     * Sets node edge path
     * @return
     */
    public Set<Edge> edges() {
        Set<Edge> all = new HashSet<>(outEdges);
        all.addAll(inEdges);
        return all;
    }

    /**
     * Calculates and sets the nodes distance to origo
     */
    public void calculateOrigoDistance(){
        double xOrigo = 0;
        double yOrigo = 0;
        double x = location.getX();
        double y = location.getY();
        setOrigoDistance(Math.sqrt((y - yOrigo) * (y - yOrigo) + (x - xOrigo) * (x - xOrigo)));
    }

    /**
     * Prints out node info to JSON format
     * @return
     */
    public String toJson() {
        return "{" +
                "\"type\": \"" + type + "\"," +
                "\"outEdges\": " + outEdges.stream().map(edge -> edge.toJson()).collect(Collectors.joining(", ", "[", "]")) + "," +
                "\"inEdges\": " + inEdges.stream().map(edge -> edge.toJson()).collect(Collectors.joining(", ", "[", "]")) + "," +
                "\"location\": " + "[" + location.x + ", " + location.y + "]" + "," +
                "\"size\": " + "[" + size.width + ", " + size.height + "]" +
                "}";
    }

    // Getters & Setters
    public double getParentEdgeLength(){ return parentEdgeLength; }

    public void setParentEdgeLength(double length){ this.parentEdgeLength = length; }

    public double getChildEdgeLength(){ return childEdgeLength; }

    public void setChildEdgeLength(double length){ this.childEdgeLength = length; }

    public Edge getParentEdge(){ return parentEdge; }

    public void setParentEdge(Edge e){ this.parentEdge = e; }

    public Edge getChildEdge(){ return childEdge; }

    public void setChildEdge(Edge e){ this.childEdge = e; }

    public boolean isVisited() { return visited; }

    public void setVisited(boolean visited) { this.visited = visited; }

    public double getG() { return g; }

    public void setG(double g) { this.g = g; }

    public double getF() { return f; }

    public void setF(double f) { this.f = f; }

    public double getH() { return h; }

    public void setH(double h) { this.h = h; }

    public Node getParent() { return parent; }

    public Node getChild() { return child; }

    public double getOrigoDistance() { return origoDistance; }

    public void setOrigoDistance(double origoDistance) { this.origoDistance = origoDistance; }

    public void setParent(Node parent) { this.parent = parent; }

    public void setChild(Node child) { this.child = child; }

    public Set<Edge> outEdges() {return outEdges; }

    public Set<Edge> inEdges() { return inEdges; }

    public Point location() { return location; }

    public Dimension size() { return size; }

    public String type() { return type; }

    @Override
    /**
     * Comparing nodes to other nodes in terms of total search cost
     */
    public int compareTo(Node other) {
        double otherNodesTotalCost = other.f;
        if (this.f > otherNodesTotalCost){ return 1;
        } else if (this.f == otherNodesTotalCost){ return 0;
        } else { return -1; }
    }
}
