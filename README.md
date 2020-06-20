# java-astar-search
Java based A* search implementation. A* is a common search algorithm from the field of artificial intelligence. 

It uses a heuristic evaluation to return the shortest path from start to goal. As long as the chosen heuristic function is both consistent and admissible, the algorithm WILL be guaranteed to return a solution if one exists, and it will be the optimal solution. In the implementation, the Euclidean distance is used as a heuristic function but it's up to the user to use any other function.

The implementation builds upon a model that contains a set of nodes. Each node has directed edges going in and/or out. Model, node, and edge are individual classes. There are also some helper methods included, such as s breadth first traversal to visit all nodes and test stuff. Each object has 2 dimensional point (x,y) coordinates to specify the location.

How you fill the model with nodes and edges is up to you. The implementation was used as a master thesis project in IT-engineering, working on the product from an undisclosed software company. So that part of the code is not provided since it could be classified or protected by copyright. But I can say that the model was filled by pulling data from a database which in turn got the values from an interactive GUI of the model (in the company's software).  
