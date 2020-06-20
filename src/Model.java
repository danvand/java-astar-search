/**
 * Model class
 * Written by: Daniel Vandolph 2020-02-05
 * A Master Thesis Project in Artificial Intelligence @ Umeå University
 * January 2020 - June 2020
 */

import java.util.Set;
import java.util.stream.Collectors;

public class Model {

    // Attributes
    private final Set<Node> nodes;
    private final String type;

    // Constructor
    public Model(Set<Node> nodes, String type) {
        this.nodes = Set.copyOf(nodes);
        this.type = type;
    }

    /**
     * Prints model info in JSON format
     * @return JSON string
     */
    String toJson() {
        return "{" +
                "\"type\": \"" + type + "\"," +
                "\"nodes\": " + nodes.stream().map(node -> node.toJson()).collect(Collectors.joining(",", "[", "]")) +
                "}";
    }

    /**
     * Gets nodes
     * @return set of nodes
     */
    public Set<Node> nodes() { return nodes; }

    /**
     * Gets type
     * @return type
     */
    public String type() { return type; }

}
