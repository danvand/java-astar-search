/**
 * A Star search algorithm. Uses heuristic evaluation in order to expand the model nodes in the best possible way
 * Written by: Daniel Vandolph 2020-02-05
 * A Master Thesis Project in Artificial Intelligence @ Umeå University
 * January 2020 - June 2020
 */

import java.awt.*;
import java.util.*;
import java.util.List;
import static java.lang.Math.abs;

/**
 * A* Class, handling the algorithm behavior and model expansion & traversal
 */
public class AStar {

    //----------------------------------------------------- Attributes ---------------------------------------------- //
    private Model inputModel;
    private List<Node> openList; //  All of the nodes that we are currently considering
    private List<Node> closedList;
    private Queue<Node> path;
    private ArrayList<Node> shortestPath;
    private Node startNode;
    private Node goalNode;

    //---------------------------------------------------- Constructor -----------------------------------------------//

    /**
     * Constructs an A* object by taking in a model to search. It finds the shortest path of nodes between start and
     * goal which are stored in an array list.
     * @param m model to search
     */
    public AStar(Model m){
        this.inputModel = m;
        initialize(inputModel);
        shortestPath = aStarSearch();
    }

    //------------------------------------------------------ Methods -------------------------------------------------//


    /**
     * Initialization: sets start and goal nodes of model
     * @param m input model
     */
    private void initialize(Model m){
        startNode = findStartNode(m.nodes());
        goalNode = findGoalNode(m.nodes());
    }


    /**
     * A* search: returns the shortest path between start and goal based on heuristic evaluation, in this case
     * Euclidean distance.
     * @return Array List of the nodes in order start to goal
     */
    private ArrayList<Node> aStarSearch(){

        // Initialization
        ArrayList<Node> shortestRoute = new ArrayList<>();
        openList = new LinkedList<>();
        closedList = new LinkedList<>();
        startNode.setF(0);

        // Add start node to open list
        openList.add(startNode);

        // While the open list is not empty
        while (!openList.isEmpty()){

            // Sort open list by total search cost so lowest comes first
            Collections.sort(openList);

            // Pop head of open list
            Node lowestTotalScoreNode = openList.remove(0);

            // Add to closed list
            closedList.add(lowestTotalScoreNode);

            // Goal check - if found, backtrack and return path
            if (lowestTotalScoreNode == goalNode){
                Node current = lowestTotalScoreNode;
                while (current != null){
                    shortestRoute.add(current);
                    current = current.getParent();
                }
                Collections.reverse(shortestRoute);
                return shortestRoute;
            }

            // generate successors and set their parent and parent edge to lowestTotalScoreNode and the connecting edge
            Set<Node> successors = new HashSet<>();
            for (Edge e : lowestTotalScoreNode.outEdges()){
                e.calculateLength();
                Node successor = findNodeForEdge(e, inputModel.nodes());
                successor.setParent(lowestTotalScoreNode);
                successor.setParentEdgeLength(e.getLength());
                successor.setParentEdge(e);
                successors.add(successor);
            }

            // for each successor
            for (Node successor : successors){

                // if successor is on closed list, skip loop
                for (Node n : closedList){
                    if (successor == n){ break; }
                }

                // Calculate g, f, h
                // successor.g = lowestTotalScoreNode.g + distance between successor and lowestTotalScoreNode
                successor.setG(lowestTotalScoreNode.getG() + successor.getParentEdgeLength());

                // successor.h = distance from goal to successor (heuristic approx euclidean distance)
                successor.setH(hHeuristicCost(successor,goalNode));

                // set successor's total cost
                successor.setF(successor.getG() + successor.getH());

                // if successor is on open list, skip loop
                for (Node n : openList){
                    if ((successor == n) && (successor.getG() >= n.getG())){ break; }
                }
                openList.add(successor);
            }
        }
        return shortestPath;
    }



    /**
     * Visits all nodes in the model by breadth first search
     */
    protected void breadthFirstSearch(){

        path = new LinkedList<Node>();
        Node startNode = findStartNode(inputModel.nodes());
        path.add(startNode);
        startNode.setF(0);
        startNode.setG(0);
        startNode.setH(0);
        startNode.setVisited(true);

        // Start traversing
        while (!path.isEmpty()){

            // Get queue head
            Node currentNode = path.poll();

            Queue<Edge> tempEdges = new LinkedList<>();
            for (Edge e : currentNode.outEdges()){
                tempEdges.add(e);
            }

            for (Edge e : tempEdges){

                // find neighbour connected to edge
                Node tempNeighbor = findNodeForEdge(e, inputModel.nodes());

                // add neighbour to path to be visited
                if (!tempNeighbor.isVisited()){
                    path.add(tempNeighbor);
                    tempNeighbor.setVisited(true);
                }
            }
         }
    }

    /**
     * Heuristic approximation based on straight line distance to goal node
     * @param currentNode the current position
     * @param goalNode the goal position
     * @return the straight line distance
     */
    protected double hHeuristicCost(Node currentNode, Node goalNode){
        Point current = currentNode.location();
        Point goal = goalNode.location();
        // √ (y2 − y1)^2 + (x2 − x1)^2  EUCLIDEAN DISTANCE
        return Math.sqrt((goal.y - current.y) * (goal.y - current.y) + (goal.x - current.x) * (goal.x - current.x));
    }



    /**
     * Searches all nodes to find the one that is the parent to the edge input parameter
     * @param e output edge who's node to find
     * @param allNodes set of all nodes in model
     * @return matching node or trivial node when no match
     */
    private Node findParentNodeForEdge(Edge e, Set<Node> allNodes){
        for (Node tempNode : allNodes){
            for (Edge tempEdge : tempNode.outEdges()){
                if (tempEdge == e){
                    return tempNode;
                }
            }
        }
        Node notFound = new Node();
        return notFound;
    }


    /**
     * Searches all nodes to find the one that is connected to the edge input parameter
     * @param e input edge who's node to find
     * @param allNodes set of all nodes in model
     * @return matching node or trivial node when no match
     */
    private Node findNodeForEdge(Edge e, Set<Node> allNodes){
        for (Node tempNode : allNodes){
            for (Edge tempEdge : tempNode.inEdges()){
                if (tempEdge == e){
                    return tempNode;
                }
            }
        }
        Node notFound = new Node();
        return notFound;
    }

    /**
     * Finds starting node, under the assumption that it doesn't have inEdges, and is closest to origo of all nodes
     * @param graphNodes Set of all nodes
     * @return the most likely starting node
     */
    private Node findStartNode(Set<Node> graphNodes) {
        List<Node> potentialStartNodes = new ArrayList<>();

        // Sorting out the nodes that have inEdges (and no out edges)
        for (Node n : graphNodes) {
            if (n.inEdges().isEmpty() && !n.outEdges().isEmpty()) {
                potentialStartNodes.add(n);
            }
        }

        // Queue to order nodes in ascending order
        //TODO: maybe use potentialstartnodes as size instead of all nodes
        PriorityQueue<Node> pq = new PriorityQueue<Node>(graphNodes.size(), new StartNodeComparator());

        // Sorting the potential start nodes
        for (Node n : potentialStartNodes) {
            n.calculateOrigoDistance();
            pq.add(n);
        }

        return pq.peek();
    }

    /** //TODO: test
     * Finds the goal node, under the assumption that it has in edges, no out edges, and is farthest from origo
     * @param graphNodes all nodes in graph
     * @return the farthest node from origo that fulfils the criteria
     */
    private Node findGoalNode(Set<Node> graphNodes){
        List<Node> potentialGoalNodes = new ArrayList<>();

        // Sorting out the nodes that don't fit the criteria (must have inEdges and no outEdges)
        for (Node n : graphNodes) {
            if (!n.inEdges().isEmpty() && n.outEdges().isEmpty()) {
                potentialGoalNodes.add(n);
            }
        }

        // Queue to order nodes in descending order //TODO: maybe use potential start nodes as size instead of all nodes
        PriorityQueue<Node> pq = new PriorityQueue<Node>(graphNodes.size(), new GoalNodeComparator());

        // Sorting the potential goal nodes
        for (Node n : potentialGoalNodes) {
            n.calculateOrigoDistance();
            pq.add(n);
        }

        return pq.peek();
    }



    //----------------------------------------------- Private inner classes ------------------------------------------//

    /**
     * Private inner class that compares nodes origo distance for priority queue ordering
     */
    private class StartNodeComparator implements Comparator<Node> {
        // Overriding compare()method of Comparator
        // for ascending order of distance to origo
        public int compare(Node n1, Node n2) {
            if (n1.getOrigoDistance() > n2.getOrigoDistance())
                return 1;
            else if (n1.getOrigoDistance() < n2.getOrigoDistance())
                return -1;
            return 0;
        }
    }

    /**
     * Private inner class that compares nodes origo distance for priority queue ordering
     */
    private class GoalNodeComparator implements Comparator<Node> {
        // Overriding compare()method of Comparator
        // for descending order of distance to origo
        public int compare(Node n1, Node n2) {
            if (n1.getOrigoDistance() < n2.getOrigoDistance())
                return 1;
            else if (n1.getOrigoDistance() > n2.getOrigoDistance())
                return -1;
            return 0;
        }
    }



}

