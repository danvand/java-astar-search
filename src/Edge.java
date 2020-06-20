/**
 * Edge Class
 * Written by: Daniel Vandolph 2020-02-05
 * A Master Thesis Project in Artificial Intelligence @ Umeå University
 * January 2020 - June 2020
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.Math.abs;

public class Edge {

    // Attributes
    private final String type;
    private List<Point> path;
    private double length;

    // Constructor
    public Edge(String type, List<Point> path) {
        this.type = type;
        this.path = List.copyOf(path);
    }

    // Methods

    /**
     * Custom method to connect to start node
     * @param n node to connect
     */
    public void connectStartToNode(Node n){
        this.path.get(0).x = n.location().x + n.size().width;
        this.path.get(0).y = n.location().y + n.size().height/2;
    }
    /**
     * Custom method to connect to end node
     * @param n node to connect
     */
    public void connectEndToNode(Node n){
        this.path.get(1).x = n.location().x;
        this.path.get(1).y = n.location().y + n.size().height/2;
    }


    /**
     * Calculates the total length of the path my adding the manhattan distance between each point
     */
    public void calculateLength(){
        Point temp, next;

        // find out if edge is straight
        if (path.size() > 2) {
            // (x1, y1) and p2 at (x2, y2), it is |x1 - x2| + |y1 - y2|
            double distance = 0;
            for (int i = 0; i < path.size()-1; i++) {
                temp = path.get(i);
                next = path.get(i + 1);
                distance = distance + abs(temp.x - next.x) + abs(temp.y - next.y);
            }
            this.length = distance;

        // if edge is just a straight line with 2 points
        } else {
            temp = path.get(0);
            next = path.get(1);

            // If points are vertically aligned, use y distance
            if (temp.x == next.x){ this.length = abs(next.y - temp.y); }

            // If points are horizontally aligned, use x distance
            if (temp.y == next.y){ this.length = abs(next.x - temp.x); }
        }
    }


    /**
     * method to print out edge data as JSON
     * @return
     */
    public String toJson() {
        return "{" +
                "\"type\": \"" + type + "\"," +
                "\"path\": " + path.stream().map(point -> "[" + point.x + ", " + point.y + "]").collect(Collectors.joining(", ", "[", "]")) +
                "}";
    }

    // Getters & Setters

    public double getLength(){ return length; }

    public String type() {
        return type;
    }

    public List<Point> path() {
        return path;
    }

    public void setPath(List<Point> p){ this.path = p; }

}
